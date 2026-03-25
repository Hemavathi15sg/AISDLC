# Exercise 15 — Security Review

**Duration**: 4 minutes  
**Copilot Feature**: Security Prompt File  
**Goal**: Run a structured OWASP Top 10 security review of the codebase and fix critical findings.

---

## Background

Security is not an afterthought in modern SDLC — it's a gate before deployment. GitHub Copilot can audit code against the **OWASP Top 10** and propose fixes. By encoding the review checklist in a prompt file, the entire team runs the same consistent security audit every sprint.

This exercise uses the pre-built `.github/prompts/security-review.prompt.md` to drive the review.

---

## Step 1 — Open the Security Review Prompt

Open `.github/prompts/security-review.prompt.md` and read the OWASP checklist it contains. This file was created in the workshop setup.

Notice it covers:
- A01: Broken Access Control
- A02: Cryptographic Failures
- A03: Injection attacks
- A04: Insecure Design
- And remaining OWASP categories through A10: SSRF

---

## Step 2 — Run the Security Review

In Copilot Chat (default Agent), type `/` and select **Security Review**, or send:

```
Run the security review defined in .github/prompts/security-review.prompt.md against the codebase in src/.

For each OWASP category:
1. List every file and line that has a potential vulnerability
2. Explain the risk in plain language
3. Provide the exact fix

Start with A01 (Broken Access Control) and A03 (Injection) as they are highest priority for API applications.
Give me a severity rating (CRITICAL / HIGH / MEDIUM / LOW) for each finding.
```

---

## Step 3 — Apply Critical Fixes

After the review, send this prompt to apply the most important fixes:

```
Apply all CRITICAL and HIGH severity findings from that security review.

For each fix:
1. Show the vulnerable code (before)
2. Show the fixed code (after)
3. Explain why the fix closes the vulnerability

Don't apply MEDIUM or LOW findings yet — add a comment with // SECURITY-TODO: [finding summary] instead.
```

Watch Copilot work through the findings and edit the source files in `src/`.

---

## Step 4 — Verify Common Patterns

Send this targeted prompt to check the most common injection vectors:

```
Audit every database query in src/repositories/ for SQL injection vulnerabilities.
Specifically check:
1. Are all queries using parameterized statements? (no string template literals with user input)
2. Are all user-supplied IDs validated as UUIDs before use in queries?
3. Are numeric page/limit parameters bounds-checked before use in LIMIT/OFFSET clauses?

For each repository file, report: SAFE or VULNERABLE with the specific line if vulnerable.
Fix any VULNERABLE patterns immediately.
```

---

## Step 5 — Generate a Security Findings Report

Send:

```
Create a doc/security-review-report.md summarizing:
- Date of review: today
- Reviewer: GitHub Copilot
- Files reviewed: list all files in src/
- Findings summary table: Severity | OWASP Category | File | Status (Fixed/Open)
- Fixed issues: description of each fix applied
- Open issues: remaining MEDIUM/LOW items with recommended fix
- Overall security posture assessment
```

---

## Key Takeaway

> Security built into the workflow — not bolted on after — is what OWASP recommends as "Shift Left Security." By encoding the OWASP checklist in a reusable prompt file, your team runs the same security review every sprint, every PR. The prompt captures institutional knowledge about *what to look for* so no security engineer knowledge needs to be in every developer's head.

---

**Next**: [Exercise 16 — Build & Debug](exercise-16-build-debug.md)
