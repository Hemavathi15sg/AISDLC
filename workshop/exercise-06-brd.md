# Exercise 06 — Generate the Business Requirements Document

**Duration**: 4 minutes  
**Copilot Feature**: BRD Custom Agent  
**Goal**: Use the BRD agent to generate a professional BRD from `requirement.md`.

---

## Background

Now that your `BRD Author` agent is set up, you'll use it to produce a formal Business Requirements Document. The agent will read `requirement.md`, apply BA best practices, structure the document properly, and save it to `doc/brd.md` — without you writing a single line.

---

## Step 1 — Open Copilot Chat and Select the BRD Agent

1. Open Copilot Chat (`Ctrl+Alt+I`)
2. Click the **agent/model selector** at the top of the chat input
3. Select **BRD Author**

You should see the agent name appear in the chat header.

---

## Step 2 — Send the BRD Generation Prompt

Copy and paste the following prompt into the chat:

```
Read the project requirements from #file:requirement.md and create a comprehensive Business Requirements Document. Save it as doc/brd.md.

Make sure to:`
- Number all requirements uniquely (BR-F-001, BR-NF-001, BR-R-001...)
- Include a stakeholder table with interests and influence levels
- Include a risks and mitigations table
- Add a glossary of domain terms
- Keep it suitable for both business and technical readers
```

---

## Step 3 — Review Copilot's Plan

The agent will show you a **plan** before writing. Review it to ensure:
- It understood the domain (task management)
- It plans to create `doc/brd.md`
- It isn't proposing any code

Click **Continue** (or **Keep**) to proceed.

---

## Step 4 — Inspect the Output

Once Copilot finishes, open `doc/brd.md`. Check:

- [ ] Executive Summary captures the business problem
- [ ] Functional requirements present with numbers
- [ ] Non-functional requirements have measurable criteria (e.g., "response time < 2 seconds")
- [ ] All task features from `requirement.md` are captured
- [ ] A stakeholder table exists with Developer, Team Lead, Project Manager, QA Engineer rows

---

## Key Takeaway

> The BRD agent gives Copilot a **persistent, reusable role**. Compare this to asking the default agent "write a BRD" — you'd need to re-explain the structure every time. Agents encode your standards once and apply them consistently.

---

**Next**: [Exercise 07 — Generate TSD](exercise-07-tsd.md)
