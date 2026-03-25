# Exercise 08 — Generate the Functional Requirements Document

**Duration**: 4 minutes  
**Copilot Feature**: FRD Custom Agent  
**Goal**: Use the FRD agent to create testable user stories and use cases from the BRD and TSD.

---

## Background

The FRD is the document development teams and QA engineers work from directly. It breaks down business requirements into **use cases**, **user stories with Gherkin acceptance criteria**, and a **permissions matrix**. The FRD Author agent synthesizes the BRD and TSD into this developer-ready document.

---

## Step 1 — Switch to the FRD Agent

1. In Copilot Chat, click the agent selector
2. Select **FRD Author**

---

## Step 2 — Send the FRD Generation Prompt

Copy and paste this prompt:

```
Read #file:requirement.md Create a comprehensive Functional Requirements Document saved as doc/frd.md.

Include:
- A User Roles & Permissions Matrix (Developer / Team Lead / Project Manager / QA Engineer vs all features)
- Detailed use cases for: Task Creation (UC-001), Task Assignment (UC-002), Task Dependency Management (UC-003), Task Status Tracking (UC-004), Task Listing and Filtering (UC-005), Project Progress Summary (UC-006)
- User stories for each use case with Given/When/Then acceptance criteria in Gherkin format
- A complete Functional Requirements Catalogue with FR-IDs linked to requirement sections
- Data validation rules for all input fields (Task ID, title, priority, status, dates)
- All notification triggers for task reassignment, dependency blocking, and status changes with recipient and content
- Error scenarios and user-facing error messages
```

## Checkpoint — Docs Complete ✅

You should now have:

```
doc/
├── brd.md    ← Business requirements (from Exercise 06)
├── tsd.md    ← Technical architecture (from Exercise 07)
└── frd.md    ← Functional requirements + user stories (this exercise)
```

These three documents form the **specification foundation** for all remaining exercises. Every piece of code, database schema, test, and deployment script will trace back to one of these files.

---

## Key Takeaway

> Notice how each agent built on the previous output. BRD → TSD → FRD is the natural SDLC chain, and each agent was pre-wired to read the prior document. This is the power of **document-driven agentic workflows** — Copilot maintains context across the entire specification phase without you having to copy-paste content.

---

**Next**: [Exercise 09 — Create Custom Instructions](exercise-09-custom-instructions.md)
