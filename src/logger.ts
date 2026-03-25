import winston from 'winston';
import path from 'path';

const { combine, timestamp, json, colorize, simple } = winston.format;

/**
 * Log level driven by LOG_LEVEL env var (default: 'info').
 * In production (NODE_ENV=production) defaults to 'info',
 * in development defaults to 'debug'.
 */
const isProduction = process.env.NODE_ENV === 'production';
const logLevel = process.env.LOG_LEVEL ?? (isProduction ? 'info' : 'debug');

const transports: winston.transport[] = [
  new winston.transports.Console({
    format: isProduction ? combine(timestamp(), json()) : combine(colorize(), simple()),
  }),
];

if (isProduction) {
  transports.push(
    new winston.transports.File({
      filename: path.join(process.env.LOG_DIR ?? 'logs', 'app.log'),
      format: combine(timestamp(), json()),
    }),
  );
}

const logger = winston.createLogger({
  level: logLevel,
  transports,
  // Prevent winston from calling process.exit on uncaughtException
  exitOnError: false,
});

export default logger;
