# Exercise 12 — Build APIs with the Local Agent

| | |
|---|---|
| **Duration** | 5 minutes |
| **Feature** | Local (Default) Agent — Agentic Coding |
| **Goal** | Scaffold the ITMS REST API and implement task management endpoints backed by a JSON file store — no database required |

---

## Background

The **Local Agent** (Copilot in Agent mode) reads your workspace files, creates and edits files, runs terminal commands, and iterates — all inside VS Code. Because your **copilot-instructions.md** is already active, every file the agent produces will follow your team's coding standards automatically.

> **No database needed.** The API uses a JSON file store by default so it runs immediately on any machine. To swap in a real database later, see [Exercise 20 — Database & SQL](exercise-20-database-sql.md) — the repository layer is designed so that a single env var (`USE_DATABASE=true`) is the only change required.

---

## Step 1 — Switch to Agent Mode

In Copilot Chat, confirm:
- **Agent**: Local (default) — not a custom agent
- **Mode**: Agent — not Ask or Plan

---

## Step 2 — Scaffold the Project Structure

```
Read #tsd.md and the API Design section. Then scaffold the initial project
structure for the ITMS REST API:

- Root config file (package.json / pyproject.toml / pom.xml — match the stack
  in copilot-instructions.md)
- Folder structure:
    src/routes/         src/controllers/    src/services/
    src/repositories/   src/models/         src/middleware/
    src/config/         src/data/           ← JSON flat-file store
- App entry point starting on port 3000 (Express / FastAPI / Spring Boot)
- .env.example:
    PORT=3000
    NODE_ENV=development
    USE_DATABASE=false        # true → switch to real DB
    DB_CONNECTION_STRING=     # only used when USE_DATABASE=true
- Health check: GET /api/v1/health → { status: "ok", timestamp: <ISO>, version: "1.0.0" }
- README.md with setup instructions

Do NOT implement any business logic yet — scaffolding only.
```

> When the agent pauses to ask about technology choices, answer based on your stack from Exercise 09.

---

## Step 3 — Create the JSON Data Store

### 3a — Seed the data files

```
Set up the JSON data store so the API runs without a database.

Copy these pre-built seed files from workshop/sample-data/ into src/data/:

  sample-data/users.json               → src/data/users.json
  sample-data/tasks.json               → src/data/tasks.json
  sample-data/task_dependencies.json   → src/data/task_dependencies.json
  sample-data/task_status_history.json → src/data/task_status_history.json

The seed data contains:
  - 5 users  (1 PM · 1 TL · 2 Devs · 1 QA)
  - 10 tasks (all priority/status combinations)
  - 3 dependency relationships (2 tasks intentionally blocked)
  - 6 status history entries showing realistic progression
  All IDs cross-reference correctly between files.

Then create the repository layer (items 2–3 below).
```

### 3b — Generic JSON repository (`src/repositories/json-store.ts`)

The agent should produce a `JsonRepository<T>` class that:

| Concern | Implementation |
|---|---|
| Startup | Reads and parses the JSON file into a private in-memory array |
| `findAll(filters?)` | Returns all items; filters are simple equality checks |
| `findById(id)` | Returns one item or `undefined` |
| `create(item)` | Appends to the array, writes file |
| `update(id, patch)` | Shallow-merges patch, writes file, returns updated item |
| `delete(id)` | Removes by id, writes file, returns `true` if found |
| Write format | `fs.writeFileSync` + `JSON.stringify(data, null, 2)` (human-readable) |
| Missing file | Initialises with an empty array |

### 3c — Task repository (`src/repositories/task.repository.ts`)

Three `JsonRepository` instances backed by:

| Instance | File |
|---|---|
| `taskRepo` | `src/data/tasks.json` |
| `historyRepo` | `src/data/task_status_history.json` |
| `dependencyRepo` | `src/data/task_dependencies.json` |

Exported functions (not a class):

| Function | Behaviour |
|---|---|
| `findAll(filters, page, limit)` | Filter with `taskRepo.findAll()`, then paginate |
| `findById(id)` | Delegate to `taskRepo` |
| `create(taskData)` | Generate UUID, set status `TO_DO`, set timestamps, call `taskRepo.create()` |
| `updateStatus(id, newStatus, changedBy, note)` | Update task, then call `addStatusHistoryEntry()` |
| `addStatusHistoryEntry(entry)` | Generate UUID + timestamp, call `historyRepo.create()` |
| `findDependencies(taskId)` | All dependency records matching `taskId` |
| `findStatusHistory(taskId)` | All history records matching `taskId` |

Also create `src/repositories/user.repository.ts` with `findById`, `findByEmail`, and `findAll`.

<details>
<summary>If the agent needs more detail — click to expand the precise follow-up prompt</summary>

```
Create the repository layer for the ITMS JSON data store.

1. src/repositories/json-store.ts
   - Export a generic class JsonRepository<T extends { id: string }>
   - Constructor accepts a file path (e.g. src/data/tasks.json)
   - On construction: read and parse the JSON file into a private in-memory array
   - Methods:
       findAll(filters?: Partial<T>): T[]
         → if filters provided, return only items where every key matches (equality)
       findById(id: string): T | undefined
       create(item: T): T
         → push to array, write full array back to JSON file
       update(id: string, patch: Partial<T>): T | undefined
         → find by id, shallow-merge patch, write back, return updated item
       delete(id: string): boolean
         → remove by id, write back, return true if found
   - All writes: fs.writeFileSync + JSON.stringify(data, null, 2)
   - Missing file on startup → initialise with []

2. src/repositories/task.repository.ts
   - Import JsonRepository plus Task, TaskStatusHistory, TaskDependency from src/models/
   - Three instances: taskRepo, historyRepo, dependencyRepo (paths above)
   - Export functions listed in Step 3c

3. src/repositories/user.repository.ts
   - JsonRepository for src/data/users.json
   - Export: findById(id), findByEmail(email), findAll()

Apply coding standards from .github/copilot-instructions.md throughout.
```

</details>

---

## Step 4 — Implement the Task Management API

### Endpoint reference

| Endpoint | Input | Success | Error |
|---|---|---|---|
| `POST /api/v1/tasks` | `{ title, description, priority, assignedUserId, dueDate }` | `201` task with status `TO_DO` | `400 VALIDATION_ERROR` if title missing, priority invalid, dueDate invalid, or assignedUserId not found |
| `GET /api/v1/tasks` | Query: `status` · `priority` · `assignedUserId` · `page` · `limit` | `200` `{ data, meta: { total, page, limit } }` | — |
| `PATCH /api/v1/tasks/:id/status` | `{ status }` — `TO_DO \| IN_PROGRESS \| BLOCKED \| COMPLETED` | `200` updated task + history entry written | `422 TASK_BLOCKED` if any dependency not `COMPLETED` · `404` if not found |
| `GET /api/v1/tasks/:id` | — | `200` task + `statusHistory[]` + `dependencies[]` | `404` if not found |

Files to create: `src/services/task.service.ts` · `src/controllers/task.controller.ts` · `src/routes/tasks.ts` — mount at `/api/v1`.

### Prompt

```
Implement the four Task Management API endpoints from #tsd.md.
Use src/repositories/task.repository.ts — the service layer calls the repository,
never raw JSON directly.

Implement all four endpoints per the spec above. For each:
  - Validate inputs with a schema library
  - Apply business rules (TASK_BLOCKED dependency check on status change)
  - Return the standard response envelope: { success, data, error, meta }
  - Use structured logging with a request ID on every request

Create src/services/task.service.ts, src/controllers/task.controller.ts,
and src/routes/tasks.ts. Mount all routes at /api/v1.

Apply all standards from .github/copilot-instructions.md.
```

<details>
<summary>If the agent needs more detail — click to expand the precise follow-up prompt</summary>

```
Implement the four ITMS Task Management API endpoints in full.

--- src/models/task.model.ts ---
  enum Priority   { LOW = "LOW", MEDIUM = "MEDIUM", HIGH = "HIGH" }
  enum TaskStatus { TO_DO = "TO_DO", IN_PROGRESS = "IN_PROGRESS",
                    BLOCKED = "BLOCKED", COMPLETED = "COMPLETED" }
  interface Task {
    id: string; title: string; description: string;
    priority: Priority; status: TaskStatus;
    assignedUserId: string; estimatedCompletionDate: string;
    createdBy: string; createdAt: string; updatedAt: string;
    completedAt: string | null;
  }
  interface TaskStatusHistory {
    id: string; taskId: string; previousStatus: string; newStatus: string;
    changedBy: string; changedAt: string; note: string;
  }
  interface TaskDependency {
    id: string; taskId: string; dependsOnTaskId: string;
    createdBy: string; createdAt: string;
  }

--- src/services/task.service.ts ---
All functions throw typed errors — never return raw catches.

  createTask(payload): Task
    1. Validate title, priority, dueDate (not in the past), assignedUserId via userRepo
       → throw ValidationError with field-level message on failure
    2. Call taskRepository.create() with status TO_DO
    3. Return created task

  listTasks(filters, page, limit): { data: Task[]; total: number }
    → delegate to taskRepository.findAll()

  getTaskById(id): Task & { statusHistory; dependencies }
    1. findById → throw NotFoundError if missing
    2. Attach findStatusHistory(id) and findDependencies(id)
    3. Return enriched object

  updateTaskStatus(id, newStatus, changedBy, note): Task
    1. findById → throw NotFoundError if missing
    2. If newStatus is IN_PROGRESS or COMPLETED:
       load dependencies; if ANY dependsOnTask.status !== "COMPLETED" → throw TaskBlockedError
    3. Call taskRepository.updateStatus()
    4. If COMPLETED, set completedAt to current ISO timestamp
    5. Return updated task

--- src/controllers/task.controller.ts ---
  POST   /api/v1/tasks              → createTask()  → 201
  GET    /api/v1/tasks              → listTasks()   → 200
  GET    /api/v1/tasks/:id          → getTaskById() → 200
  PATCH  /api/v1/tasks/:id/status   → updateTaskStatus() → 200

  Every handler:
    - try/catch with error mapping:
        ValidationError  → 400 { code: "VALIDATION_ERROR", message, fields? }
        NotFoundError    → 404 { code: "NOT_FOUND", message }
        TaskBlockedError → 422 { code: "TASK_BLOCKED", message }
        unknown          → 500 { code: "INTERNAL_ERROR", message: "An unexpected error occurred" }
    - Success: { success: true, data, meta? }
    - Log: method · path · requestId (uuid) · statusCode · durationMs

--- src/middleware/error.middleware.ts ---
  Centralised Express error handler with the same error→response mapping above.

--- src/errors/ ---
  ValidationError, NotFoundError, TaskBlockedError
  Each extends AppError which carries statusCode and code properties.

--- src/routes/tasks.ts ---
  Register all four routes on an Express Router; mount at /api/v1 in app entry point.
```

</details>

---

## Step 5 — Verify the APIs

```
Start the application, then test all five endpoints with curl and show the commands
and expected JSON responses:

1. GET  /api/v1/health
2. POST /api/v1/tasks              — HIGH priority, valid assignedUserId from src/data/users.json
3. GET  /api/v1/tasks              — list all, then filter ?status=TO_DO&priority=HIGH
4. GET  /api/v1/tasks/:id          — fetch the task created in step 2
5. PATCH /api/v1/tasks/:id/status  — update status to IN_PROGRESS
```

Confirm:
- Every response uses the `{ success, data, error, meta }` envelope
- After the PATCH, the status history entry appears in the GET `:id` response
- Filtering and pagination work correctly on the list endpoint

---

## (Optional) Step 6 — Switch to a Real Database

> Requires a running database. Complete [Exercise 20 — Database & SQL](exercise-20-database-sql.md) first, then return here.

```
Exercise 20 is done; migrations are in db/migrations/.
Update the repository layer so USE_DATABASE=true in .env routes reads/writes to
the real database instead of JSON files.

- Create src/repositories/db-task.repository.ts implementing the same interface
  as the JSON repository, using parameterized DB queries / ORM
- Update src/config/data-source.ts to export the active repository based on
  USE_DATABASE
- Keep the JSON repositories unchanged as the default fallback

Services, controllers, and routes need no changes.
```

---

## Key Takeaway

Two things to notice:

1. **Standards enforcement** — the agent followed your `copilot-instructions.md` automatically: response envelope, input validation, structured logging.
2. **Repository abstraction** — the same service and controller code works with both the JSON store and a real database. Swapping the data layer is a one-line config change, not a rewrite.

---

**Next**: [Exercise 13 — Design & Scaffold the UI](exercise-13-ui-design.md)


