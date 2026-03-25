# Exercise 16 — Build & Debug with the Local Agent

**Duration**: 5 minutes  
**Copilot Feature**: Local Agent + Terminal Tool  
**Goal**: Use Copilot to build the application in the terminal, interpret errors, and iteratively fix them until all tests pass.

---

## Background

The local agent can run terminal commands, read their output, diagnose errors, and make code changes — all in one conversation. This creates a tight **build → error → fix → build** loop that compresses debugging time significantly.

---

## Step 1 — Start a Build-and-Fix Session

In Copilot Chat (Agent mode), send:

```
Let's build and run the ITMS application. 

1. First, install all dependencies using the appropriate package manager
2. Then run the build/compile step if applicable (TypeScript tsc, Maven compile, etc.)
3. Then start the application
4. Report any errors you encounter

For each error:
- Show me the exact error message
- Identify the root cause
- Apply the fix directly to the source file
- Re-run the build to confirm the fix worked

Continue until the application starts successfully and GET /api/v1/health returns { status: "ok" }
```

The agent will run commands in the terminal, read the output, and iterate on fixes. You'll see it:
1. Run `npm install` (or equivalent)
2. Run `npm run build`(or equivalent)
3. Fix any TypeScript errors or import issues
4. Start the server
5. Test the health endpoint

---

## Step 2 — Run Unit Tests

Once the app builds, send:

```
Run the unit tests:
npm run test:unit

 or any equivalent command for the tech stack.

For any failing tests:
1. Show the test name and failure message
2. Determine if it's a code bug or a test bug
3. Fix the root cause, not just the test
4. Re-run until all unit tests pass

Show me the final coverage report.
```

---

## Step 3 — Debug a Specific Scenario

Try a deliberate debugging exercise. Send:

```
Make a POST request to /api/v1/tasks using curl with:
- A valid assignedUserId from src/data/users.json
- A task due next Friday
- priority: HIGH, title: "Implement Payment API"

Then immediately call GET /api/v1/tasks/:id using the returned task ID.

Show me the curl commands, the responses, and the server logs.
If the POST response is not a 201 with status "TO_DO", or the GET returns 404, diagnose and fix the issue.
```

If the request fails, Copilot will:
1. Look at the server logs
2. Trace the request through the controller → service → repository
3. Identify the bug (e.g., date validation blocking valid future dates, or missing FK constraint handling)
4. Apply the fix and re-test

---

## Step 4 — Run Integration Tests

Send:

```
Run the integration tests: npm run test:integration

Note: The integration tests need the application running. Start it in the background first, then run the tests.

For any failing integration tests, diagnose and fix, referencing the original Gherkin acceptance criteria in doc/frd.md to understand what the expected behavior should be.
```

---

## Step 5 — Verify End-to-End Flow

Send a final validation prompt:

```
Do an end-to-end manual test of the core task management flow using curl:

1. GET  /api/v1/tasks — confirm the seeded tasks are returned
2. POST /api/v1/tasks — create a new HIGH priority task with a valid
   assignedUserId from src/data/users.json, dueDate set to next Friday
3. GET  /api/v1/tasks/:id — fetch the newly created task and confirm status is TO_DO
4. PATCH /api/v1/tasks/:id/status — update status to IN_PROGRESS
5. GET  /api/v1/tasks/:id — confirm status is IN_PROGRESS and statusHistory
   has an entry recording the TO_DO → IN_PROGRESS transition
6. PATCH /api/v1/tasks/:id/status — update status to COMPLETED
7. GET  /api/v1/tasks?status=COMPLETED — confirm the task appears in the filtered list

Show me each curl command and response. The complete flow must work without errors.
```

---

## Debugging Tips for Attendees

If Copilot gets stuck in a loop on a specific error:

```
Stop trying to fix [specific error]. Let's approach this differently.
What is the root cause of this error based on the stack trace? 
What are the 3 most likely causes? Eliminate them one by one.
```

If a test keeps failing after fixes:

```
Read the original acceptance criteria in doc/frd.md for [FR-ID or US-ID].
Is the test testing the right thing? Is the implementation correct per the FRD?
Tell me which is wrong — the test or the implementation — before making any changes.
```

---

## Key Takeaway

> The local agent's superpower is the **terminal + code edit + reasoning** combination. A human debugger reads an error, opens the file, makes a change, saves, rebuilds, checks the output. The local agent does this loop autonomously, maintaining context across multiple iterations. The key practice is to give it a **goal** ("all tests pass") rather than micro-managing each step.

---

**Next**: [Exercise 21 — IaC & CI/CD (Optional)](exercise-21-iac-cicd.md)
