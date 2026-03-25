# Exercise 18 — Assign an Independent Task to the Background Agent

**Duration**: 3 minutes  
**Copilot Feature**: Background Agent  
**Goal**: Delegate a long, self-contained coding task to the Background Agent and continue working while it runs.

---

> ---
> 🟡 **OPTIONAL EXERCISE**
>
> This exercise is **not required** to complete the mandatory track. The Background Agent runs tasks asynchronously in a sandboxed environment — it is self-contained and does not block any later exercise. Try it after Exercise 12 if time permits.
>
> **Best after**: Exercise 12 &nbsp;|&nbsp; **Return to mandatory track**: [Exercise 16 — Unit & Functional Tests](exercise-14-testing.md)
> ---


## Background

The **Background Agent** runs Copilot tasks in the background — inside a sandboxed environment, asynchronously — while you keep working in your main VS Code session. It's perfect for:
- Long-running code generation tasks (generate all CRUD endpoints, write all tests)
- Tasks that don't need your immediate attention
- Parallel workstreams (you write one feature while Copilot writes another)

When the background agent finishes, it opens a pull request with the generated changes for you to review.
---

## Step 1 — Launch the Background Agent

In Copilot Chat:
1. Click the **agent menu** or look for the **Copilot CLI**, make sure you have installed the [Copilot CLI](https://docs.github.com/en/copilot/how-tos/copilot-cli/set-up-copilot-cli/install-copilot-cli) before proceeding.
2. Select **Start Background Task** (or `New Background Agent Session`)

> **Alternative**: Use `Ctrl+Shift+P` → **GitHub Copilot: New Background Agent**

---

## Step 2 — Assign the Task Assignment API Task

This task is a good background candidate because it's:
- Self-contained (doesn't depend on your current work)
- Well-defined (we have the FRD and TSD to guide it)
- Time-consuming (multiple controllers, services, repositories)

Copy and paste this prompt into the Background Agent:

```
You are working in the ITMS (Intelligent Task Management System) project.

Read:
- doc/tsd.md (API design section — Task Assignment and Dependency endpoints)
- doc/frd.md (Use Cases UC-002 Task Assignment, UC-003 Task Dependency Management)
- .github/copilot-instructions.md (coding standards)
- The existing code in src/ to understand the folder structure and patterns

Implement the Task Assignment workflow:

1. PATCH /api/v1/tasks/:id/assign
   - Auth required: any role
   - Accepts: { assignedUserId }
   - Reassigns the task to the specified user
   - Records previous assignee in task history
   - Triggers notification stub (log the reassignment event)

2. POST /api/v1/tasks/:id/dependencies
   - Auth required: any role
   - Accepts: { dependsOnTaskId }
   - Creates a dependency link between tasks
   - If dependsOnTaskId is not Completed, marks the task as Blocked

3. DELETE /api/v1/tasks/:id/dependencies/:dependencyId
   - Auth required: any role
   - Removes the dependency link
   - Re-evaluates blocked status of the task

4. PATCH /api/v1/tasks/:id/status
   - Auth required: any role
   - Accepts: { status } (To Do / In Progress / Blocked / Completed)
   - Validates allowed status transitions
   - Records status change in task history

Follow the same patterns as the existing auth and task endpoints.
Create service, repository, and any new model files needed.
Open a pull request with all changes when done.
```

---

## Step 3 — Continue Working

While the background agent runs:
- **Continue in your local session** — move to Exercise 19 (Context Map)
- The background agent works independently and won't interrupt you

---

## Step 4 — Review the Pull Request

When the background agent finishes (typically 5–15 minutes for this task), it will:
1. Show a notification in VS Code
2. Open Chat session
3. Review and Click on "apply" to apply the generated code changes to the branch

Review the changes:
- [ ] All 4 endpoints are implemented
- [ ] Dependency blocking logic works correctly (task becomes Blocked when dependency is incomplete)
- [ ] Task history records all assignment and status changes
- [ ] Code follows the same patterns as the existing `src/` files

Merge the PR if it looks correct.

---

## Key Takeaway

> Background Agent is a **parallel workstream**. On a real project, you might assign 4–5 long tasks to background agents (e.g., write all CRUD for users, write all CRUD for task priorities, write all notifications, generate all tests) while you focus on the complex business logic. Each background agent opens a PR, you review and merge. This compresses days of work into hours.

---

**Next optional**: [Exercise 19 — Context Map Skill](exercise-19-context-map.md)

**Return to Mandatory Track →**: [Exercise 16 — Unit & Functional Tests](exercise-14-testing.md)
