import { Request, Response, NextFunction } from 'express';
import { randomUUID } from 'crypto';
import { AsyncLocalStorage } from 'async_hooks';
import logger from '../logger';

// ---------------------------------------------------------------------------
// Request-scoped storage – carries the request context through async calls
// ---------------------------------------------------------------------------

export interface RequestContext {
  requestId: string;
  userId?: string;
  operation?: string;
}

export const requestContextStorage = new AsyncLocalStorage<RequestContext>();

/**
 * Returns the active request context for the current async call chain,
 * or `undefined` when called outside of a request.
 */
export function getRequestContext(): RequestContext | undefined {
  return requestContextStorage.getStore();
}

// ---------------------------------------------------------------------------
// Secret masking
// ---------------------------------------------------------------------------

/** Header names whose values must never appear in logs. */
const SENSITIVE_HEADERS = new Set([
  'authorization',
  'cookie',
  'set-cookie',
  'x-api-key',
  'x-auth-token',
  'proxy-authorization',
]);

/** Body field names whose values must never appear in logs. */
const SENSITIVE_BODY_FIELDS = new Set([
  'password',
  'confirmPassword',
  'confirm_password',
  'secret',
  'token',
  'accessToken',
  'access_token',
  'refreshToken',
  'refresh_token',
  'apiKey',
  'api_key',
  'creditCard',
  'credit_card',
  'cvv',
  'ssn',
]);

const MASKED = '***REDACTED***';

function maskHeaders(headers: Record<string, unknown>): Record<string, unknown> {
  const safe: Record<string, unknown> = {};
  for (const [key, value] of Object.entries(headers)) {
    safe[key] = SENSITIVE_HEADERS.has(key.toLowerCase()) ? MASKED : value;
  }
  return safe;
}

function maskBody(body: unknown): unknown {
  if (!body || typeof body !== 'object' || Array.isArray(body)) {
    return body;
  }
  const safe: Record<string, unknown> = {};
  for (const [key, value] of Object.entries(body as Record<string, unknown>)) {
    if (SENSITIVE_BODY_FIELDS.has(key)) {
      safe[key] = MASKED;
    } else if (value && typeof value === 'object' && !Array.isArray(value)) {
      safe[key] = maskBody(value);
    } else {
      safe[key] = value;
    }
  }
  return safe;
}

// ---------------------------------------------------------------------------
// Middleware
// ---------------------------------------------------------------------------

/**
 * Express middleware that:
 *  1. Assigns a unique `X-Request-ID` to every request.
 *  2. Stores request context in AsyncLocalStorage so downstream services
 *     can retrieve it without explicit prop-drilling.
 *  3. Emits a structured JSON log entry after the response is sent.
 *
 * Log fields follow TSD Section 7 Logging Standards:
 *   request_id, timestamp, method, path, status_code, duration_ms,
 *   user_id (if authenticated), operation, error (if status >= 400)
 */
export function requestLogger(req: Request, res: Response, next: NextFunction): void {
  const requestId = (req.headers['x-request-id'] as string | undefined) ?? randomUUID();
  const startTime = Date.now();

  // Propagate request ID downstream via response header and async context
  res.setHeader('X-Request-ID', requestId);

  // Build initial context (userId / operation may be enriched by auth middleware later)
  const context: RequestContext = { requestId };
  requestContextStorage.run(context, () => {
    // Allow downstream middleware to attach userId / operation to the context
    (req as RequestWithContext).requestContext = context;

    const rawPath = req.path;

    res.on('finish', () => {
      const durationMs = Date.now() - startTime;
      const statusCode = res.statusCode;

      // Retrieve userId/operation that auth or routing middleware may have set
      const userId = context.userId ?? (req as RequestWithContext).user?.id;
      const operation = context.operation ?? deriveOperation(req);

      const logEntry: LogEntry = {
        request_id: requestId,
        timestamp: new Date().toISOString(),
        method: req.method,
        path: rawPath, // path only – no query string to avoid leaking sensitive params
        status_code: statusCode,
        duration_ms: durationMs,
        ...(userId !== undefined && { user_id: userId }),
        operation,
        ...(statusCode >= 400 && {
          error: res.locals['errorMessage'] ?? `HTTP ${statusCode}`,
        }),
      };

      if (statusCode >= 500) {
        logger.error(logEntry);
      } else if (statusCode >= 400) {
        logger.warn(logEntry);
      } else {
        logger.info(logEntry);
      }
    });

    next();
  });
}

// ---------------------------------------------------------------------------
// Helpers
// ---------------------------------------------------------------------------

/** Derive a human-readable operation name from the HTTP method + path. */
function deriveOperation(req: Request): string {
  const method = req.method.toUpperCase();
  // Normalise path segments: remove IDs (:uuid / numeric / mongo-style) for readability
  const cleanedPath = req.path
    .split('/')
    .filter(Boolean)
    .map((segment) =>
      /^[0-9a-f-]{8,}$/i.test(segment) || /^\d+$/.test(segment) ? ':id' : segment,
    )
    .join('/');

  const resourceParts = cleanedPath
    .split('/')
    .map((p) => p.charAt(0).toUpperCase() + p.slice(1));

  const operationMap: Record<string, string> = {
    GET: 'List',
    POST: 'Create',
    PUT: 'Update',
    PATCH: 'Patch',
    DELETE: 'Delete',
  };

  const verb = operationMap[method] ?? method;
  const resource = resourceParts.filter((p) => p !== ':id').join('');
  return resource ? `${verb}${resource}` : verb;
}

// ---------------------------------------------------------------------------
// Type augmentation
// ---------------------------------------------------------------------------

export interface LogEntry {
  request_id: string;
  timestamp: string;
  method: string;
  path: string;
  status_code: number;
  duration_ms: number;
  user_id?: string;
  operation: string;
  error?: string;
}

export interface RequestWithContext extends Request {
  requestContext: RequestContext;
  /** Minimal shape – a real auth middleware would populate this. */
  user?: { id: string };
}

export { maskHeaders, maskBody, SENSITIVE_HEADERS, SENSITIVE_BODY_FIELDS };
