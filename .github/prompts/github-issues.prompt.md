---
name: GitHub Issues Generator
description: "Use when you need to convert an implementation plan into GitHub Issues. Triggered by: create GitHub issues, generate work items from plan, convert tasks to issues, create GitHub issues from implementation plan."
---

# GitHub Issues Generator

Convert the implementation plan (from `doc/implementation-plan.md` or the plan in this conversation) into a structured list of GitHub Issues ready to be created via the **GitHub MCP server** or manually.

## For Each Issue, Provide

```markdown
### Issue: [Issue Title]

**Labels**: enhancement | bug | documentation | testing | infrastructure | security
**Milestone**: Phase 1 | Phase 2 | Phase N
**Assignee**: (leave blank)
**Estimated Effort**: S (< 4h) | M (4–8h) | L (8–16h)

**Description**:
[2–3 sentence description of what needs to be done and why]

**Acceptance Criteria**:
- [ ] [Verifiable criterion 1]
- [ ] [Verifiable criterion 2]
- [ ] [Verifiable criterion 3]

**Technical Notes**:
[Any technical context, file paths, or dependencies the developer needs]

**References**:
- FRD: [FR-ID]
- Depends on: #[issue-number] (if known)
```

---

## After Generating the Issue List

Provide the following **GitHub MCP commands** to create each issue:

```
Use the GitHub MCP to create an issue in repository [OWNER/REPO]:
- Title: "[Issue Title]"
- Body: [full markdown body above]
- Labels: [labels]
- Milestone: [milestone]
```

> **Note**: Replace `[OWNER/REPO]` with your actual GitHub repository name before running.

---

## Grouping Rules

- Group issues by Phase (Phase 0 → Phase 1 → Phase 2…)
- Create one **Epic issue** per Phase to link sub-issues
- Database tasks → label `database`
- API tasks → label `backend`
- Test tasks → label `testing`
- Security tasks → label `security`
- IaC/CI/CD tasks → label `infrastructure`
