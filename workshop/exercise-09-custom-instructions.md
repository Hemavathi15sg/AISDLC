# Exercise 09 — Create Custom Instructions for Your Language

**Duration**: 3 minutes  
**Copilot Feature**: Workspace Instructions (`copilot-instructions.md`)  
**Goal**: Set up always-on coding standards that Copilot follows in every conversation.

---

## Background

A **`copilot-instructions.md`** file in `.github/` is loaded automatically into every Copilot Chat session. Unlike custom agents (which you manually select), instructions are **always active** — they set the baseline for how Copilot writes code for your project.

Think of it as your team's coding standards, permanently injected into Copilot's context.

---

## Step 1 — Decide Your Language & Framework

Choose the tech stack you want to build the ITMS application with:

| Option | Language | Framework | Database |
|--------|----------|-----------|----------|
| A | TypeScript / Node.js | Express or Fastify | PostgreSQL or SQL of your choice |
| B | Python | FastAPI or Django REST | PostgreSQL or SQL of your choice |
| C | Java | Spring Boot | PostgreSQL or Oracle or SQL of your choice |
| D | C# / .NET | ASP.NET Core | PostgreSQL or SQL Server or SQL of your choice |

> The rest of the workshop will use whichever you pick. **Option A (TypeScript/Node.js)** is the default reference if you're unsure.

---

## Step 2 — Generate Your Instructions File

Switch back to the **default Copilot agent** (not a custom agent).

Copy and paste this prompt, **replacing `[YOUR CHOICE]`** with your selection:

```
I am building a REST API application using [YOUR CHOICE — e.g., TypeScript with Express and YOUR CHOICE DATABASE].

Create a .github/copilot-instructions.md file with workspace-wide coding standards for this project.

Include instructions covering:
1. Language & framework conventions (naming, file structure, module organization)
2. API design rules: versioning (/api/v1/), consistent response envelope { success, data, error, meta }
3. Error handling: custom error classes, centralized error middleware, proper HTTP status codes
4. Security: always validate input with a schema library, never trust user input, use parameterized queries only
5. Database: use migrations for schema changes, use a query builder or ORM — no raw string SQL queries
6. Testing: every new function must have a unit test, integration tests for all API endpoints
7. Logging: structured JSON logs with request ID, user ID, and operation name
8. Code style: no console.log in production code, no TODO comments without ticket numbers, functions under 30 lines
9. Documentation: JSDoc/docstring for all exported functions
10. Git: conventional commits (feat:, fix:, docs:, test:, chore:)
```

---

## Step 3 — Verify the File

Open `.github/copilot-instructions.md` and check:

- [ ] File exists at `.github/copilot-instructions.md`
- [ ] Contains your chosen language/framework
- [ ] Includes rules about input validation and parameterized queries
- [ ] Has error handling conventions
- [ ] Mentions the `/api/v1/` URL convention

---

## Step 4 — Test That Instructions Are Active

Close and reopen Copilot Chat. Send this test prompt in the **local agent**:

```
Write a simple function that takes a user ID and returns user details from the database.
```

Observe that Copilot:
- Uses your chosen language and SQL database
- Uses parameterized queries (not string concatenation)
- Includes error handling
- Follows the response envelope format you defined

---

## Key Takeaway

> Instructions set the **floor** for code quality. Once written, you never have to repeat "use TypeScript", "use parameterized queries", or "follow REST conventions" — Copilot applies these automatically to every piece of code it generates in this workspace.

---

**Next**: [Exercise 10 — Plan Mode for Implementation](exercise-10-plan-mode.md)
