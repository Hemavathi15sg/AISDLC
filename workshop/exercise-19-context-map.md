# Exercise 19 — Create a Context Map Skill

**Duration**: 4 minutes  
**Copilot Feature**: Skills (`SKILL.md`)  
**Goal**: Install the Context Map skill from the awesome-copilot repository and use it to generate a codebase map that improves all subsequent Copilot interactions.

---

> ---
> 🟡 **OPTIONAL EXERCISE**
>
> This exercise is **not required** to complete the mandatory track. The context map it produces gives Copilot richer codebase awareness, but Exercises 13–15 work without it. If you run this exercise, reference `context-map.md` in your prompts for Exercises 12 and 13.
>
> **Best after**: Exercise 12 &nbsp;|&nbsp; **Return to mandatory track**: [Exercise 16 — Unit & Functional Tests](exercise-14-testing.md)
> ---


## Background

A **Skill** is an on-demand workflow bundled in a `SKILL.md` file. Unlike instructions (always active) or agents (persona-driven), a skill is a **specific multi-step process** you invoke when you need it.

The **Context Map** skill generates a structured map of your entire codebase — all files, their purposes, relationships, and key patterns. When you attach this map to a Copilot prompt, Copilot gets instant, accurate context about your project without having to re-read every file.

---

## Step 1 — Install the Context Map Skill

Create the skill directory and file:

1. Create the folder `.github/skills/context-map/`
2. Create the file `.github/skills/context-map/SKILL.md`

**In Copilot Chat**, send this prompt to fetch and save the skill:

```
Fetch the Context Map skill from this URL and save it to .github/skills/context-map/SKILL.md:
https://raw.githubusercontent.com/github/awesome-copilot/main/skills/context-map/SKILL.md

Then confirm the file was saved correctly by showing me its first 20 lines.
```

> **Alternative if network access is unavailable**: Ask the instructor for the `SKILL.md` file, or copy it from: https://github.com/github/awesome-copilot/blob/main/skills/context-map/SKILL.md

---

## Step 2 — Read the Skill

Open `.github/skills/context-map/SKILL.md` and read how the skill works. You'll see it instructs Copilot to systematically explore the codebase and generate a structured context document.

---

## Step 3 — Run the Skill

In Copilot Chat (Agent mode, default agent), type `/` and look for **context-map** in the slash command list, or send:

```
Follow the instructions in .github/skills/context-map/SKILL.md to generate a context map for this project.

Save the output as .github/skills/context-map/context-map.md

The context map should cover:
- All directories and their purpose
- Key source files and what they do
- Main data models and their relationships
- API endpoints and their handlers
- Configuration patterns
- Test structure
```

---

## Step 4 — Verify the Context Map

Open `.github/skills/context-map/context-map.md`. Check that it contains:

- [ ] Directory tree with descriptions for each folder
- [ ] Key files listed with their purpose
- [ ] Main entities/models described
- [ ] API routes listed with handler locations
- [ ] Dependencies and their usage noted

---

## Step 5 — Use the Context Map in a Prompt

Now test how the context map improves Copilot's responses. Send this prompt:

```
Using the context map in .github/skills/context-map/context-map.md, answer:
1. Which file handles task dependency blocking?
2. Where is the JWT authentication middleware?
3. What database tables are currently defined?
4. What's the response format for all API endpoints?
```

Compare the quality and accuracy of the answer vs. asking without the context map.

---

## Why This Matters

In future prompts, prefix complex requests with:

```
Reference the context map at .github/skills/context-map/context-map.md for codebase context, then...
```

This single addition significantly reduces hallucinations (Copilot inventing file paths or function names) because it has accurate, up-to-date codebase knowledge.

---

## Key Takeaway

> Skills are **reusable processes**, not instructions or conversation. The Context Map skill is a good first skill for every project — it creates a shared mental model between you and Copilot about your codebase. You run it once (and re-run it as the codebase grows), and it pays dividends across every subsequent interaction.

---

**Next optional**: [Exercise 20 — Database & SQL / PL/SQL](exercise-20-database-sql.md)

**Return to Mandatory Track →**: [Exercise 16 — Unit & Functional Tests](exercise-14-testing.md)
