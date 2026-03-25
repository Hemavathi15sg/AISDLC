# Exercise 10 — Plan Mode: Generate Implementation Plan

**Duration**: 4 minutes  
**Copilot Feature**: Plan Mode (Copilot Edits)  
**Goal**: Use Plan Mode to let Copilot reason through the full implementation strategy before writing any code.

---

## Background

**Plan Mode** in GitHub Copilot is a deliberate step where Copilot analyzes the task, identifies all files and components affected, and presents an execution plan **before** taking any action. You review and approve — or modify — the plan before Copilot touches a single file.

This is especially valuable for large tasks (like "implement the entire task module") where you want to verify the approach first.

---

## Step 1 — Switch to Plan Mode

In Copilot Chat:
1. Click the **mode selector** at the bottom-left of the chat panel
2. Select **Plan** (this may appear as `Plan Mode` or the pencil/plan icon)

> You should see a visual indicator that Plan mode is active.

---

## Step 2 — Send the Implementation Planning Prompt

Copy and paste this prompt:

```
Read #frd.md and #tsd.md carefully.

Generate a complete, phased implementation plan for the Intelligent Task Management System (ITMS).

Structure the plan as:
- Phase 0: Project setup, folder structure, database migration tooling, CI skeleton
- Phase 1: Authentication & User Management (registration, login, JWT, RBAC)
- Phase 2: Task Management (task creation, assignment, dependencies, status tracking)
- Phase 3: Task Reporting & Progress Summary (project progress, task filters, export)
- Phase 4: Notifications (email via SendGrid, Teams webhooks)
- Phase 5: Reporting (monthly reports, CSV/PDF export)
- Phase 6: Testing, security hardening, and documentation

For each phase, list tasks with:
- Task ID (T-001, T-002...)
- Title
- Effort (S/M/L where S=<4h, M=4-8h, L=8-16h)
- FRD reference (FR-ID or US-ID)
- Whether it can run in parallel or must be sequential
- Whether it's a good candidate for Background Agent (long, self-contained tasks)

Do NOT create any files yet. Show me the plan first.
```

---

## Step 3 — Review the Plan

Copilot will display the plan before doing anything. Check:

- [ ] All 6 phases are covered
- [ ] Phase 0 includes project scaffolding tasks
- [ ] Tasks reference FRD IDs
- [ ] Some tasks are flagged as Background Agent candidates
- [ ] Effort estimates seem reasonable (Phase 1 ≈ 3–5 days total)

---

## Step 4 — Refine the Plan

Ask Copilot to adjust if needed. Example refinements:

```
Move the JWT middleware task to Phase 0 since all phases depend on it.
Also add a task for setting up the OpenAPI/Swagger documentation scaffold in Phase 0.
```

After refinement, **approve the plan** and switch back to **Agent mode** to save the plan as a file.

---

## Verify

Open `doc/implementation-plan.md` and confirm:

- [ ] All phases are present
- [ ] Tasks have IDs, effort estimates, and FRD references
- [ ] Background Agent candidates are flagged
- [ ] Total effort per phase is summarized

---

## Key Takeaway

> Plan Mode prevents "tunnel vision" — where Copilot dives into implementation and creates a solution that doesn't fit the full picture. By reviewing the plan first, you catch architectural gaps and poor task ordering **before** they become code debt. On real projects, this 5-minute review often saves hours of rework.

---

**Next**: [Exercise 11 — Create Implementation Prompt File](exercise-11-implementation-prompt.md)
