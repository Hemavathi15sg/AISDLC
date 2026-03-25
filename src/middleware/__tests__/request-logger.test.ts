import { Request, Response, NextFunction } from 'express';
import {
  requestLogger,
  getRequestContext,
  requestContextStorage,
  maskHeaders,
  maskBody,
  SENSITIVE_HEADERS,
  SENSITIVE_BODY_FIELDS,
  RequestWithContext,
  LogEntry,
} from '../request-logger';
import logger from '../../logger';

// ---------------------------------------------------------------------------
// Helpers
// ---------------------------------------------------------------------------

function makeReq(overrides: Partial<Request> = {}): Request {
  return {
    method: 'GET',
    path: '/tasks',
    headers: {},
    query: {},
    body: {},
    ...overrides,
  } as unknown as Request;
}

function makeRes(overrides: Partial<Response> = {}): Response {
  const listeners: Record<string, Array<() => void>> = {};
  return {
    statusCode: 200,
    locals: {},
    setHeader: jest.fn(),
    on: jest.fn((event: string, cb: () => void) => {
      listeners[event] = listeners[event] ?? [];
      listeners[event].push(cb);
    }),
    _emit: (event: string) => listeners[event]?.forEach((cb) => cb()),
    ...overrides,
  } as unknown as Response;
}

// ---------------------------------------------------------------------------
// Spy on logger
// ---------------------------------------------------------------------------

let loggerInfoSpy: jest.SpyInstance;
let loggerWarnSpy: jest.SpyInstance;
let loggerErrorSpy: jest.SpyInstance;

beforeEach(() => {
  loggerInfoSpy = jest.spyOn(logger, 'info').mockImplementation(() => logger);
  loggerWarnSpy = jest.spyOn(logger, 'warn').mockImplementation(() => logger);
  loggerErrorSpy = jest.spyOn(logger, 'error').mockImplementation(() => logger);
});

afterEach(() => {
  jest.restoreAllMocks();
});

// ---------------------------------------------------------------------------
// Tests
// ---------------------------------------------------------------------------

describe('requestLogger middleware', () => {
  test('sets X-Request-ID response header', () => {
    const req = makeReq();
    const res = makeRes();
    const next: NextFunction = jest.fn();

    requestLogger(req, res, next);

    expect(res.setHeader).toHaveBeenCalledWith('X-Request-ID', expect.any(String));
  });

  test('reuses an existing X-Request-ID from the request header', () => {
    const existingId = 'existing-id-1234';
    const req = makeReq({ headers: { 'x-request-id': existingId } });
    const res = makeRes();
    const next: NextFunction = jest.fn();

    requestLogger(req, res, next);

    expect(res.setHeader).toHaveBeenCalledWith('X-Request-ID', existingId);
  });

  test('calls next()', () => {
    const req = makeReq();
    const res = makeRes();
    const next: NextFunction = jest.fn();

    requestLogger(req, res, next);

    expect(next).toHaveBeenCalled();
  });

  test('attaches requestContext to req object', () => {
    const req = makeReq() as RequestWithContext;
    const res = makeRes();
    const next: NextFunction = jest.fn(() => {
      expect(req.requestContext).toBeDefined();
      expect(typeof req.requestContext.requestId).toBe('string');
    });

    requestLogger(req, res, next);
  });

  describe('log entry on finish', () => {
    function runAndFinish(
      req: Request,
      res: ReturnType<typeof makeRes>,
      next: NextFunction = jest.fn(),
    ): void {
      requestLogger(req, res as unknown as Response, next);
      (res as unknown as { _emit: (e: string) => void })._emit('finish');
    }

    test('logs required fields: request_id, timestamp, method, path, status_code, duration_ms, operation', () => {
      const req = makeReq({ method: 'POST', path: '/tasks' });
      const res = makeRes({ statusCode: 201 });

      runAndFinish(req, res);

      const entry: LogEntry = (loggerInfoSpy.mock.calls[0] as [LogEntry])[0];
      expect(entry.request_id).toBeDefined();
      expect(entry.timestamp).toMatch(/^\d{4}-\d{2}-\d{2}T/);
      expect(entry.method).toBe('POST');
      expect(entry.path).toBe('/tasks');
      expect(entry.status_code).toBe(201);
      expect(typeof entry.duration_ms).toBe('number');
      expect(entry.operation).toBeDefined();
    });

    test('includes user_id when set on request context', () => {
      const req = makeReq({ method: 'GET', path: '/tasks' });
      const res = makeRes({ statusCode: 200 });
      const next: NextFunction = jest.fn(() => {
        (req as RequestWithContext).requestContext.userId = 'user-abc';
      });

      runAndFinish(req, res, next);

      const entry: LogEntry = (loggerInfoSpy.mock.calls[0] as [LogEntry])[0];
      expect(entry.user_id).toBe('user-abc');
    });

    test('includes user_id from req.user when set', () => {
      const req = makeReq({ method: 'GET', path: '/tasks' }) as RequestWithContext;
      req.user = { id: 'user-xyz' };
      const res = makeRes({ statusCode: 200 });

      runAndFinish(req as unknown as Request, res);

      const entry: LogEntry = (loggerInfoSpy.mock.calls[0] as [LogEntry])[0];
      expect(entry.user_id).toBe('user-xyz');
    });

    test('omits user_id when not authenticated', () => {
      const req = makeReq({ method: 'GET', path: '/tasks' });
      const res = makeRes({ statusCode: 200 });

      runAndFinish(req, res);

      const entry: LogEntry = (loggerInfoSpy.mock.calls[0] as [LogEntry])[0];
      expect(entry).not.toHaveProperty('user_id');
    });

    test('includes error field for 4xx responses', () => {
      const req = makeReq({ method: 'GET', path: '/tasks/99' });
      const res = makeRes({ statusCode: 404, locals: { errorMessage: 'Task not found' } });

      runAndFinish(req, res);

      const entry: LogEntry = (loggerWarnSpy.mock.calls[0] as [LogEntry])[0];
      expect(entry.error).toBe('Task not found');
      expect(entry.status_code).toBe(404);
    });

    test('includes default error string for 4xx when no errorMessage set', () => {
      const req = makeReq({ method: 'DELETE', path: '/tasks/1' });
      const res = makeRes({ statusCode: 403 });

      runAndFinish(req, res);

      const entry: LogEntry = (loggerWarnSpy.mock.calls[0] as [LogEntry])[0];
      expect(entry.error).toBe('HTTP 403');
    });

    test('includes error field for 5xx responses and calls logger.error', () => {
      const req = makeReq({ method: 'POST', path: '/tasks' });
      const res = makeRes({ statusCode: 500, locals: { errorMessage: 'Internal error' } });

      runAndFinish(req, res);

      expect(loggerErrorSpy).toHaveBeenCalled();
      const entry: LogEntry = (loggerErrorSpy.mock.calls[0] as [LogEntry])[0];
      expect(entry.error).toBe('Internal error');
    });

    test('omits error field for successful responses', () => {
      const req = makeReq({ method: 'GET', path: '/tasks' });
      const res = makeRes({ statusCode: 200 });

      runAndFinish(req, res);

      const entry: LogEntry = (loggerInfoSpy.mock.calls[0] as [LogEntry])[0];
      expect(entry).not.toHaveProperty('error');
    });

    test('uses logger.warn for 4xx responses', () => {
      const req = makeReq({ method: 'GET', path: '/tasks/99' });
      const res = makeRes({ statusCode: 401 });

      runAndFinish(req, res);

      expect(loggerWarnSpy).toHaveBeenCalled();
      expect(loggerInfoSpy).not.toHaveBeenCalled();
      expect(loggerErrorSpy).not.toHaveBeenCalled();
    });

    test('uses logger.info for 2xx responses', () => {
      const req = makeReq({ method: 'GET', path: '/tasks' });
      const res = makeRes({ statusCode: 200 });

      runAndFinish(req, res);

      expect(loggerInfoSpy).toHaveBeenCalled();
      expect(loggerWarnSpy).not.toHaveBeenCalled();
      expect(loggerErrorSpy).not.toHaveBeenCalled();
    });

    test('path does not include query string', () => {
      const req = makeReq({
        method: 'GET',
        path: '/tasks',
        query: { status: 'IN_PROGRESS', assignee: 'alice' },
      });
      const res = makeRes({ statusCode: 200 });

      runAndFinish(req, res);

      const entry: LogEntry = (loggerInfoSpy.mock.calls[0] as [LogEntry])[0];
      expect(entry.path).toBe('/tasks');
      expect(entry.path).not.toContain('status');
      expect(entry.path).not.toContain('assignee');
    });
  });

  describe('operation derivation', () => {
    function getOperation(method: string, path: string): string {
      const req = makeReq({ method, path });
      const res = makeRes({ statusCode: 200 });
      requestLogger(req, res as unknown as Response, jest.fn());
      (res as unknown as { _emit: (e: string) => void })._emit('finish');
      const entry: LogEntry = (loggerInfoSpy.mock.calls[0] as [LogEntry])[0];
      return entry.operation;
    }

    test('GET /tasks -> ListTasks', () => {
      expect(getOperation('GET', '/tasks')).toBe('ListTasks');
    });

    test('POST /tasks -> CreateTasks', () => {
      expect(getOperation('POST', '/tasks')).toBe('CreateTasks');
    });

    test('PUT /tasks/:id -> UpdateTasks (UUID segment ignored)', () => {
      const op = getOperation('PUT', '/tasks/550e8400-e29b-41d4-a716-446655440000');
      expect(op).toBe('UpdateTasks');
    });

    test('DELETE /tasks/:id -> DeleteTasks', () => {
      const op = getOperation('DELETE', '/tasks/42');
      expect(op).toBe('DeleteTasks');
    });

    test('GET /tasks/:id/dependencies -> ListTasksDependencies', () => {
      const op = getOperation('GET', '/tasks/123/dependencies');
      expect(op).toBe('ListTasksDependencies');
    });

    test('custom operation set on context overrides derivation', () => {
      const req = makeReq({ method: 'POST', path: '/tasks' });
      const res = makeRes({ statusCode: 201 });
      const next: NextFunction = jest.fn(() => {
        (req as RequestWithContext).requestContext.operation = 'CreateTask';
      });

      requestLogger(req, res as unknown as Response, next);
      (res as unknown as { _emit: (e: string) => void })._emit('finish');

      const entry: LogEntry = (loggerInfoSpy.mock.calls[0] as [LogEntry])[0];
      expect(entry.operation).toBe('CreateTask');
    });
  });
});

// ---------------------------------------------------------------------------
// maskHeaders
// ---------------------------------------------------------------------------

describe('maskHeaders', () => {
  test('masks Authorization header', () => {
    const result = maskHeaders({ Authorization: 'Bearer secret-token' });
    expect(result['Authorization']).toBe('***REDACTED***');
  });

  test('masks cookie header (case-insensitive key check)', () => {
    const result = maskHeaders({ Cookie: 'session=abc' });
    expect(result['Cookie']).toBe('***REDACTED***');
  });

  test('masks x-api-key header', () => {
    const result = maskHeaders({ 'x-api-key': 'my-key' });
    expect(result['x-api-key']).toBe('***REDACTED***');
  });

  test('preserves safe headers', () => {
    const result = maskHeaders({ 'content-type': 'application/json', accept: '*/*' });
    expect(result['content-type']).toBe('application/json');
    expect(result['accept']).toBe('*/*');
  });

  test('SENSITIVE_HEADERS set contains expected values', () => {
    expect(SENSITIVE_HEADERS.has('authorization')).toBe(true);
    expect(SENSITIVE_HEADERS.has('cookie')).toBe(true);
    expect(SENSITIVE_HEADERS.has('x-api-key')).toBe(true);
    expect(SENSITIVE_HEADERS.has('content-type')).toBe(false);
  });
});

// ---------------------------------------------------------------------------
// maskBody
// ---------------------------------------------------------------------------

describe('maskBody', () => {
  test('masks password field', () => {
    const result = maskBody({ username: 'alice', password: 's3cr3t' }) as Record<string, unknown>;
    expect(result['password']).toBe('***REDACTED***');
    expect(result['username']).toBe('alice');
  });

  test('masks token field', () => {
    const result = maskBody({ token: 'my-token' }) as Record<string, unknown>;
    expect(result['token']).toBe('***REDACTED***');
  });

  test('masks apiKey field', () => {
    const result = maskBody({ apiKey: 'key123' }) as Record<string, unknown>;
    expect(result['apiKey']).toBe('***REDACTED***');
  });

  test('preserves non-sensitive fields', () => {
    const result = maskBody({ title: 'Task 1', priority: 'HIGH' }) as Record<string, unknown>;
    expect(result['title']).toBe('Task 1');
    expect(result['priority']).toBe('HIGH');
  });

  test('returns non-object body unchanged', () => {
    expect(maskBody('plain-string')).toBe('plain-string');
    expect(maskBody(null)).toBeNull();
    expect(maskBody([1, 2])).toEqual([1, 2]);
  });

  test('SENSITIVE_BODY_FIELDS set contains expected values', () => {
    expect(SENSITIVE_BODY_FIELDS.has('password')).toBe(true);
    expect(SENSITIVE_BODY_FIELDS.has('apiKey')).toBe(true);
    expect(SENSITIVE_BODY_FIELDS.has('token')).toBe(true);
    expect(SENSITIVE_BODY_FIELDS.has('title')).toBe(false);
  });

  test('recursively masks sensitive fields in nested objects', () => {
    const result = maskBody({
      user: { name: 'alice', password: 'secret' },
      meta: { token: 'tok123', info: 'ok' },
    }) as Record<string, Record<string, unknown>>;
    expect(result['user']['password']).toBe('***REDACTED***');
    expect(result['user']['name']).toBe('alice');
    expect(result['meta']['token']).toBe('***REDACTED***');
    expect(result['meta']['info']).toBe('ok');
  });
});

// ---------------------------------------------------------------------------
// AsyncLocalStorage / request context propagation
// ---------------------------------------------------------------------------

describe('requestContextStorage', () => {
  test('getRequestContext returns undefined outside a request', () => {
    expect(getRequestContext()).toBeUndefined();
  });

  test('getRequestContext returns context inside a request', (done) => {
    const req = makeReq();
    const res = makeRes();
    const next: NextFunction = jest.fn(() => {
      const ctx = getRequestContext();
      expect(ctx).toBeDefined();
      expect(typeof ctx!.requestId).toBe('string');
      done();
    });

    requestLogger(req, res as unknown as Response, next);
  });

  test('request_id in log matches X-Request-ID header', () => {
    const req = makeReq();
    const res = makeRes({ statusCode: 200 });
    let capturedRequestId: string | undefined;

    const next: NextFunction = jest.fn(() => {
      capturedRequestId = getRequestContext()?.requestId;
    });

    requestLogger(req, res as unknown as Response, next);
    (res as unknown as { _emit: (e: string) => void })._emit('finish');

    const setHeaderCall = (res.setHeader as jest.Mock).mock.calls[0] as [string, string];
    expect(setHeaderCall[0]).toBe('X-Request-ID');
    expect(setHeaderCall[1]).toBe(capturedRequestId);

    const entry: LogEntry = (loggerInfoSpy.mock.calls[0] as [LogEntry])[0];
    expect(entry.request_id).toBe(capturedRequestId);
  });
});
