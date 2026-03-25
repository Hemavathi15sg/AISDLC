# Exercise 11 — Create an Implementation Prompt File

**Duration**: 3 minutes  
**Copilot Feature**: Prompt Files (`.prompt.md`)  
**Goal**: Package your implementation plan into a reusable prompt file that any team member can invoke.

---

## Background

A **Prompt File** (`.prompt.md`) in GitHub Copilot is a saved, reusable prompt. Unlike typing a prompt in chat, prompt files:
- Are stored in `.github/prompts/` and accessible to the whole team
- Can include instructions, context references, and parameterized inputs
- Can be invoked quickly from the Copilot Chat command palette

The **Implementation Plan prompt file** you create here captures the standard way to generate a development plan from the FRD for this project — and any future project following the same process.

---

## Step 1 — Understand the Prompt File Format

A `.prompt.md` file looks like this:

```markdown
---
name: Prompt Name
description: "When to use this prompt"
---

# Prompt content here

Instructions for Copilot...
```

The `description` field is what you see in the command palette when typing `/`.

---

## Step 2 — Inspect the Pre-Built Prompt File

The workshop already includes a prompt file at `.github/prompts/implementation-plan.prompt.md`.

Open it and read it. Notice:
- The YAML frontmatter with `name` and `description`
- Instructions for structuring phases and task tables
- Rules about referencing FRD IDs and flagging Background Agent candidates

---

## Step 3 — Create a Custom Project-Specific Version

Now create **your own** version tailored to your chosen tech stack. In Copilot Chat (local agent), send:

Use ```/create-prompt``` with name ```itms-implementation-plan``` and paste the following content:
```
This prompt is specifically for the Intelligent Task Management System (ITMS) project.
When invoked, it should:
1. Read #frd.md and #tsd.md
2. Generate a phased implementation plan with our specific tech stack: [YOUR STACK e.g. TypeScript/Express/PostgreSQL]
3. Reference the correct folder structure: src/routes/, src/services/, src/repositories/, src/models/
4. Include database migration tasks using our tooling convention
5. Include tasks for OpenAPI spec generation
6. Flag tasks suitable for background agent execution

Format: phases as H2 headers, tasks as a table with columns: ID | Task | Effort | FRD Ref | Parallel? | Background Agent?
```

> Replace `[YOUR STACK]` with your choice from Exercise 09.

---

## Step 4 — Use the Prompt File

Test your new prompt file:

1. In Copilot Chat, type `/` — the command palette opens
2. Type `itms` — you should see **itms-implementation-plan** appear
3. Select it and press Enter
4. Copilot executes the prompt and generates a fresh implementation plan

---

## Step 5 — Compare the Two Outputs

Notice the difference between:
- The generic `.github/prompts/implementation-plan.prompt.md` (for any project)
- Your project-specific `itms-implementation-plan.prompt.md` (ITMS-specific folder conventions, stack, tools)

> This is why teams maintain both generic prompt libraries and project-specific ones.

---

## Key Takeaway

> Prompt files turn one-time instructions into team assets. Instead of one person knowing "the right way to ask Copilot for an implementation plan," the whole team has a `/itms-implementation-plan` command they can invoke consistently. This is how you scale Copilot adoption across a team.

---

**Next**: [Exercise 12 — Build APIs with Local Agent](exercise-12-api-local-agent.md)

> 🟡 **Optional stop available**: If your GitHub repository and Personal Access Token are configured, try [Exercise 17 — Create GitHub Issues via MCP](exercise-17-github-issues.md) before continuing to Exercise 12.
