---
name: Security Review
description: "Use when you need to identify and fix security vulnerabilities in the codebase. Triggered by: security review, find vulnerabilities, OWASP audit, security scan, fix security issues, identify security flaws."
---

# Security Review — OWASP Top 10 Analysis

Perform a comprehensive security review of the codebase, identifying vulnerabilities aligned with the **OWASP Top 10** (2021 edition).

## Scope

Scan all source files in `src/` and configuration files in the workspace root and `infra/`.

## Review Checklist

For each category below, identify affected files and lines, explain the risk, and provide a specific fix:

### A01 – Broken Access Control
- [ ] Are all API endpoints protected by authentication middleware?
- [ ] Is role-based access control (RBAC) enforced server-side (not just UI)?
- [ ] Can a user access another user's data by manipulating IDs in requests?
- [ ] Are directory traversal attacks prevented?
- [ ] Are CORS headers properly restricted?

### A02 – Cryptographic Failures
- [ ] Are passwords hashed using bcrypt/argon2 (not MD5/SHA1)?
- [ ] Is sensitive data (PII, tokens) encrypted at rest?
- [ ] Are secrets (API keys, DB passwords) stored in environment variables, not code?
- [ ] Is TLS enforced for all external connections?
- [ ] Are JWT tokens using RS256 or HS256 with strong secrets?

### A03 – Injection
- [ ] Are all database queries parameterized (no string concatenation)?
- [ ] Are ORM queries used with proper escaping?
- [ ] Is user input sanitized before use in queries, commands, or templates?
- [ ] Are stored procedures using bind variables, not dynamic SQL?
- [ ] Is output HTML-encoded to prevent XSS?

### A04 – Insecure Design
- [ ] Are rate limits implemented on authentication endpoints?
- [ ] Are brute-force protections in place (account lockout)?
- [ ] Is business logic validated server-side (not just client-side)?

### A05 – Security Misconfiguration
- [ ] Are default credentials removed?
- [ ] Are debug/stack traces hidden from API responses in production?
- [ ] Are unnecessary HTTP methods disabled?
- [ ] Are security headers set (CSP, HSTS, X-Frame-Options)?

### A06 – Vulnerable Components
- [ ] Are all dependencies up to date? (check package.json / requirements.txt / pom.xml)
- [ ] Are there known CVEs in used libraries? (suggest running `npm audit` / `pip-audit` / `mvn dependency-check`)

### A07 – Identification & Authentication Failures
- [ ] Are session tokens sufficiently random and invalidated on logout?
- [ ] Is multi-factor authentication supported for admin roles?
- [ ] Are failed login attempts logged?

### A08 – Software & Data Integrity Failures
- [ ] Are dependency integrity hashes verified?
- [ ] Are CI/CD pipeline secrets protected?

### A09 – Security Logging & Monitoring Failures
- [ ] Are authentication events logged (login, logout, failure)?
- [ ] Are authorization failures (403) logged with user context?
- [ ] Are logs stored centrally and not accessible to end users?

### A10 – Server-Side Request Forgery (SSRF)
- [ ] Are all URLs fetched by the server validated against an allowlist?
- [ ] Are internal network addresses blocked from user-supplied URLs?

---

## Output Format

For each finding:

```
### [SEVERITY: CRITICAL/HIGH/MEDIUM/LOW] — A0X: [Category]

**File**: `src/path/to/file.ts` (line N)
**Issue**: [Description of the vulnerability]
**Risk**: [What an attacker can do]
**Fix**:
[Specific code change or configuration fix]
```

---

## After the Review

1. Summarize findings in a table: Severity | Category | File | Status
2. Create a prioritized fix list (Critical → High → Medium → Low)
3. Apply all CRITICAL and HIGH severity fixes immediately
4. Add TODO comments for MEDIUM/LOW items with the security label
