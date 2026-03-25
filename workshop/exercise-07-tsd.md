# Exercise 07 — Generate the Technical Specification Document

**Duration**: 4 minutes  
**Copilot Feature**: TSD Custom Agent  
**Goal**: Use the TSD agent to design the system architecture from the BRD.

---

## Background

The TSD bridges business requirements and engineering. Your **TSD Author** agent acts as a Solutions Architect — it reads the BRD, proposes technology choices, designs the data model and API contracts, and produces a document the development team can build from.

---

## Step 1 — Switch to the TSD Agent

1. In Copilot Chat, click the agent selector
2. Select **TSD Author**

---

## Step 2 — Send the TSD Generation Prompt

Copy and paste this prompt:

```
Read #file:brd.md and #file:requirement.md, then create a complete Technical Specification Document saved as doc/tsd.md.

Requirements:
- Include a Mermaid system architecture diagram showing all major components
- Include a Mermaid ER diagram for the database schema (include all tables needed for task management)
- Design REST API endpoints for: authentication, user management, task management, task assignment, task dependencies, and reporting
- Recommend a technology stack with justifications (consider the Azure deployment constraint from #requirement.md)
- Include security architecture addressing the OWASP Top 10
- Define a CI/CD pipeline architecture
- Trace every technical decision back to a BRD requirement ID
```

---

## Step 3 — Monitor the Plan

The TSD agent will plan its approach. Look for:
- It plans to read BOTH `#brd.md` AND `#requirement.md`
- It mentions Mermaid diagrams
- It is NOT writing application code

Approve the plan.

---

## Step 4 — Ask for a Specific Architecture Decision (Optional)

Try this follow-up to see how the agent reasons:

```
The system needs to handle email notifications and Teams webhook alerts. 
Update the Integration Points section in doc/tsd.md to cover:
- SendGrid for email (async via a message queue)
- Microsoft Teams webhook for manager alerts
- Include a sequence diagram showing the task status update notification flow
```

---

## Key Takeaway

> The architecture designed here becomes the **source of truth** for the next exercises. Every API you build, every table you create, and every test you write will trace back to `doc/tsd.md`. This is how Copilot becomes a true SDLC co-pilot — not just a code generator, but an architectural collaborator.

---

**Next**: [Exercise 08 — Generate FRD](exercise-08-frd.md)
