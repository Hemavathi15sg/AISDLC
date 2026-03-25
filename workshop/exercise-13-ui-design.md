# Exercise 13 — Design & Scaffold the Task Management UI

| | |
|---|---|
| **Duration** | 5 minutes |
| **Feature** | Local Agent — UI Scaffolding from API Spec |
| **Goal** | Use Copilot to scaffold a functional frontend for the ITMS that connects directly to the API you built |

---

## Background

Your ITMS API is now running and tested. The final step in this SDLC journey is a UI that the end user actually touches. Rather than designing screens from scratch, Copilot reads the **FRD user stories** and the **running API routes** to scaffold components that are already wired to real endpoints — no guesswork.

This exercise shows how the same document-driven approach you applied to backend code scales directly to frontend work. Every component maps to a `FR-ID` from `doc/frd.md`, and every API call uses the exact routes from `src/routes/` that you verified in Exercise 12.

> **No separate framework decision needed.** The default UI stack is **React + TypeScript + Vite**. If your team chose a different backend language in Exercise 09, the API contract (JSON responses, `/api/v1/` base path) is identical — only the UI scaffold prompt changes slightly. Participants using Python or Java can simply replace `React/Vite` with their preferred framework (Vue, Angular, or plain HTML+Fetch).

---

## Step 1 — Scaffold the UI Project

In Copilot Chat (Agent mode, Local agent), send:

```
Read doc/frd.md (sections: User Roles & Permissions, UC-001 through UC-006)
and src/routes/ to understand the available API endpoints.

Scaffold a frontend project for the ITMS inside a new ui/ folder at the workspace root.

Requirements:
- Framework: React + TypeScript + Vite (or the frontend framework of my choice)
- Folder structure:
    ui/src/components/    ← reusable UI components
    ui/src/pages/         ← full-page views
    ui/src/services/      ← API client functions (one file per resource)
    ui/src/types/         ← TypeScript types mirroring the API response shapes
- Create ui/src/services/api.ts as a shared Axios (or Fetch) client that:
    - Points to http://localhost:3000/api/v1 by default
    - Reads base URL from VITE_API_BASE_URL environment variable
    - Attaches the { success, data, error, meta } envelope types from copilot-instructions.md
- Create ui/.env.example:
    VITE_API_BASE_URL=http://localhost:3000/api/v1
- Add a README note that the API (Exercise 12) must be running before starting the UI

Do NOT implement any page content yet — scaffolding and API client only.
```

---

## Step 2 — Generate the Dashboard Page

The Dashboard maps directly to **UC-006 (Project Progress Summary)** in `doc/frd.md`. Send:

```
Read the GET /api/v1/tasks endpoint (check src/routes/ for the exact path and response shape).

Generate ui/src/pages/DashboardPage.tsx:
- Call GET /api/v1/tasks on load and derive summary counts from the response:
    Total tasks | Completed | In Progress | Blocked | To Do
- Display each count in a summary card (a simple styled div is fine — no external component library required)
- Show a task table below the summary cards with columns:
    Task ID | Title | Priority | Status | Assigned To | Due Date
- Add a status badge: colour-code Completed (green), In Progress (blue), Blocked (red), To Do (grey)
- Wire the page into the router as the default route /

Keep each component under 50 lines. Extract the summary card into
ui/src/components/SummaryCard.tsx and the status badge into
ui/src/components/StatusBadge.tsx.
```

---

## Step 3 — Generate the Create Task Form

The Create Task form maps to **UC-001 (Task Creation)** and **UC-002 (Task Assignment)**.

```
Read doc/frd.md UC-001 and UC-002 and the POST /api/v1/tasks request body schema.
Also read src/data/users.json to understand the user shape.

Generate ui/src/pages/CreateTaskPage.tsx with a controlled form that:
- Fetches the user list from GET /api/v1/users on mount and populates the Assigned To dropdown
- Collects: Title (required), Description, Priority (Low/Medium/High dropdown), Assigned To (user dropdown), Due Date (date input)
- Validates required fields on the client before submitting — show inline error messages
- On submit, calls POST /api/v1/tasks with the correct request body
- On success (201), redirects to /tasks/:id of the newly created task
- On API error, displays the error.message from the response envelope

Wire the page into the router at /tasks/new.
Add a "Create Task" button on the DashboardPage that navigates to /tasks/new.
```

---

## Step 4 — Generate the Task Detail & Status Update View

Task Detail maps to **UC-003 (Dependencies)**, **UC-004 (Status Tracking)**, and the status history requirement from `doc/frd.md`.

```
Read UC-003, UC-004, and the endpoints:
  GET  /api/v1/tasks/:id
  GET  /api/v1/tasks/:id/dependencies  (or the equivalent route in src/routes/)
  GET  /api/v1/tasks/:id/history
  PATCH /api/v1/tasks/:id/status

Generate ui/src/pages/TaskDetailPage.tsx:
- Fetch and display all task fields (title, description, priority, status, assignee, due date)
- Display a "Dependencies" section: list each dependency task by title and status;
  if a dependency is not Completed, show a "Blocked by" warning
- Display a "Status History" section: a timeline list of status changes with changedBy and timestamp
- Add an "Update Status" dropdown with the allowed next statuses
  (To Do → In Progress, In Progress → Completed or Blocked)
  and a "Save" button that calls PATCH /api/v1/tasks/:id/status
- After a successful status update, refresh the page data

Wire the page into the router at /tasks/:id.
Each section (Dependencies, StatusHistory, UpdateStatus) should be its own component
in ui/src/components/.
```

---

## Step 5 — Run the UI

Once all pages are scaffolded, send:

```
Install UI dependencies and start the development server:
1. cd ui && npm install (or the equivalent for the chosen framework)
2. Start the Vite dev server: npm run dev
3. Open http://localhost:5173 in the browser

Verify:
- Dashboard loads and shows task counts from the live API
- "Create Task" form submits successfully and redirects correctly
- Task Detail page shows dependencies and status history
- Status update persists after page refresh (the API writes to src/data/tasks.json)

If any step fails, diagnose the error and apply the fix before moving on.
```

> **Both servers must be running**: the API on port 3000 (Exercise 12) and the UI dev server on port 5173.

---

## Checkpoint — End-to-End SDLC Complete ✅

At the end of this exercise you should have:

```
ui/
├── src/
│   ├── components/
│   │   ├── SummaryCard.tsx
│   │   ├── StatusBadge.tsx
│   │   ├── Dependencies.tsx
│   │   ├── StatusHistory.tsx
│   │   └── UpdateStatus.tsx
│   ├── pages/
│   │   ├── DashboardPage.tsx
│   │   ├── CreateTaskPage.tsx
│   │   └── TaskDetailPage.tsx
│   ├── services/
│   │   └── api.ts
│   └── types/
│       └── task.types.ts
└── .env.example
```

Every component traces back to a `UC-ID` in `doc/frd.md` — the same document that drove the API, the tests, and the security review.

---

## Key Takeaway

> The same document-driven workflow — **FRD → architecture → prompt → Copilot generates** — works equally well for UI as it did for the API. You did not write a single component from scratch; you directed Copilot by referencing the FRD use cases and the exact API routes already in your workspace. This is how Copilot accelerates the *full* SDLC, not just backend code.

---

**Next**: [Exercise 14 — Unit & Functional Tests](exercise-14-testing.md)
