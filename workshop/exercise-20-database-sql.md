# Exercise 20 — Database Design, SQL Scripts & PL/SQL

**Duration**: 5 minutes  
**Copilot Feature**: Local Agent + Instructions  
**Goal**: Generate the full database schema, migration scripts, seed data, and stored procedures for the ITMS application.

---

> ---
> 🟡 **OPTIONAL EXERCISE**
>
> This exercise is **not required** to complete the mandatory track. It generates the full SQL schema, migrations, and stored procedures. Complete it if database-layer code generation is relevant to your role, or revisit it after the workshop.
>
> **Best after**: Exercise 19 (or Exercise 12 if skipping Ex 19) &nbsp;|&nbsp; **Return to mandatory track**: [Exercise 16 — Unit & Functional Tests](exercise-14-testing.md)
> ---


## Background

Most business applications have critical business logic in the database layer — task dependency validation, transactional status updates, audit triggers. This exercise generates the complete SQL layer for ITMS, including PL/SQL (Oracle) or PL/pgSQL (PostgreSQL) stored procedures.

> **If you don't have a database available**, follow the Mock Data path at the end of this exercise — Copilot will create JSON mock data with the same schema structure so your API can run without a real DB.

---

## Step 1 — Generate the Database Schema

In Copilot Chat (Agent mode), send:

```
Reference the ER diagram in doc/tsd.md and the context map at .github/skills/context-map/context-map.md.

Create the database schema migration files in db/migrations/:

1. db/migrations/001_create_users.sql — Users / Team Members table
   Columns: id (UUID), email, password_hash, first_name, last_name, department,
   role (ENUM: DEVELOPER/TEAM_LEAD/PROJECT_MANAGER/QA_ENGINEER),
   is_active, created_at, updated_at

2. db/migrations/002_create_tasks.sql — Tasks table
   Columns: id (UUID), title, description, priority (ENUM: LOW/MEDIUM/HIGH),
   status (ENUM: TO_DO/IN_PROGRESS/BLOCKED/COMPLETED),
   assigned_user_id (FK to users), estimated_completion_date,
   created_by (FK to users), created_at, updated_at, completed_at

3. db/migrations/003_create_task_dependencies.sql — Task Dependencies table
   Columns: id (UUID), task_id (FK), depends_on_task_id (FK),
   created_by (FK to users), created_at
   Constraint: UNIQUE(task_id, depends_on_task_id), no self-referencing

4. db/migrations/004_create_task_status_history.sql — Status History table
   Columns: id (UUID), task_id (FK), previous_status, new_status,
   changed_by (FK to users), changed_at, note

5. db/migrations/005_create_audit_log.sql — Audit trail
   Columns: id, table_name, record_id, operation (INSERT/UPDATE/DELETE),
   old_values (JSONB), new_values (JSONB), performed_by, performed_at

Add appropriate indexes on: assigned_user_id + status, task_id (dependencies), status + priority.
Add foreign key constraints with ON DELETE RESTRICT.
Use PostgreSQL syntax.
```

---

## Step 2 — Create Stored Procedures / Functions

Send this prompt:

```
Create db/procedures/ with the following PL/pgSQL functions:

1. db/procedures/create_task.sql
   Function: create_task(p_title TEXT, p_description TEXT, p_priority TEXT, p_assigned_user_id UUID, p_due_date DATE, p_created_by UUID) RETURNS UUID
   - Validates priority is one of LOW/MEDIUM/HIGH
   - Validates assigned_user_id exists in users table
   - Inserts task with initial status TO_DO
   - Inserts a status history record for the initial creation
   - Returns the new task ID
   - All in a single transaction

2. db/procedures/update_task_status.sql
   Function: update_task_status(p_task_id UUID, p_new_status TEXT, p_changed_by UUID, p_note TEXT) RETURNS VOID
   - Validates the new status is a valid enum value
   - If transitioning to IN_PROGRESS or COMPLETED: checks no unresolved dependency (i.e., no depends_on_task that is not COMPLETED)
   - Raises exception if task is BLOCKED due to incomplete dependencies
   - Updates task status
   - Inserts a status history record
   - All in a single transaction

3. db/procedures/add_task_dependency.sql
   Function: add_task_dependency(p_task_id UUID, p_depends_on_task_id UUID, p_created_by UUID) RETURNS VOID
   - Checks for circular dependency before inserting
   - Inserts the dependency record
   - If depends_on_task is not COMPLETED, sets p_task_id status to BLOCKED and records in history

4. db/procedures/resolve_task_dependency.sql
   Function: resolve_task_dependency(p_task_id UUID) RETURNS VOID
   - Called when a dependency task is marked COMPLETED
   - Checks all dependencies of tasks that depend on p_task_id
   - If all dependencies are now COMPLETED, unblocks those tasks (sets status back to TO_DO)

5. db/triggers/audit_trigger.sql
   Trigger: Create a generic audit trigger function that logs all INSERT/UPDATE/DELETE
   operations on tasks and task_dependencies to the audit_log table.
   Apply the trigger to both tables.
```

---

## Step 3 — Create Seed Data

Send this prompt:

```
Create db/seeds/001_seed_data.sql with realistic test data:
- 10 users: 1 Project Manager, 2 Team Leads, 7 Developers/QA Engineers
- 20 sample tasks covering all priority levels (Low/Medium/High) and statuses (To Do/In Progress/Blocked/Completed)
- 5 task dependency relationships (some blocking, some resolved)
- Status history records showing a realistic progression of tasks

Use realistic names and UUIDs. Make sure the data is self-consistent (users exist before tasks reference them, dependencies reference valid task IDs).
```

---

## Step 4 — Mock JSON Path (No Database Available)

> Skip this step if you have a working database.

If you don't have PostgreSQL available, send this prompt instead:

```
I don't have a database available. Create mock JSON data files in db/mock/ that replicate the database schema so the API can run with an in-memory data store.

Create:
- db/mock/users.json — 10 team members matching the users table schema
- db/mock/tasks.json — 20 sample tasks in different statuses and priorities
- db/mock/task_dependencies.json — 5 task dependency relationships
- db/mock/task_status_history.json — history records for task status changes
- db/mock/README.md — explains how to switch between real DB and mock data mode

Also create src/repositories/mock/ implementations of the repositories that read from these JSON files instead of querying the database.
```

---

## Verify

Check `db/` folder:

- [ ] Migration files are numbered and sequential (001_, 002_…)
- [ ] Each migration creates a table with all required columns
- [ ] Indexes are defined
- [ ] Stored procedures include transaction handling and exception raising
- [ ] Dependency blocking logic correctly sets task status to BLOCKED
- [ ] Audit trigger is created for the correct tables
- [ ] Seed data is self-consistent

---

## Key Takeaway

> Copilot understands both SQL DDL and procedural SQL (PL/pgSQL/PL/SQL). By anchoring the prompt to the TSD's ER diagram and the business rules from the FRD, you get database code that actually matches your requirements — not generic boilerplate. The stored procedures encode business rules (dependency validation, status transitions) in the database layer where they belong.

---

**Return to Mandatory Track →**: [Exercise 16 — Unit & Functional Tests](exercise-14-testing.md)
