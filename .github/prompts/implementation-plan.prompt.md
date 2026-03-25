---
name: Implementation Plan
description: "Use when you need to convert a Functional Requirements Document into a detailed implementation plan for developers. Triggered by: create implementation plan, break down FRD into tasks, generate development plan from FRD."
---

# Implementation Plan Generator

Read `doc/frd.md` carefully, then generate a comprehensive, phased implementation plan suitable for a development team.

## Output Format

Structure the plan as follows:

### Phase 0: Project Setup & Foundation
List tasks for:
- Repository structure, branch strategy
- Development environment setup
- Database setup and migration tooling
- CI pipeline skeleton

### Phase 1: Core Domain — [Feature Group 1]
For each phase, list implementation tasks as:

```
Task ID  | Title                          | Effort | Dependencies | Acceptance Criteria
---------|-------------------------------|--------|--------------|--------------------
T-001    | [Task title]                   | S/M/L  | -            | [Verifiable criteria]
```

Group tasks by:
1. Database schema and seed data
2. Domain models / entities
3. Repository / data access layer
4. Service / business logic layer
5. API controllers / endpoints
6. Input validation and error handling
7. Authentication & authorization guards
8. Unit tests for service layer
9. Integration tests for API endpoints
10. API documentation (OpenAPI/Swagger)

### Phase 2: [Next Feature Group]
Repeat the same task table structure.

### Phase N: Cross-Cutting Concerns
- Logging & monitoring setup
- Security hardening
- Performance optimization
- Documentation

## Rules

- Every task must reference the FRD's FR-ID or US-ID it implements
- Effort sizing: S = < 4h, M = 4–8h, L = 8–16h
- Mark tasks that can be done in parallel vs. sequential
- Flag tasks suitable for a Background Agent (long-running, self-contained)
- Flag tasks that require human decision (architecture forks, integrations with external systems)
- Total the estimated effort per phase
