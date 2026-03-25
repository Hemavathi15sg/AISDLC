# E2E SDLC with GitHub Copilot — Workshop

> **Audience**: Developers, Tech Leads, Architects  
> **Total Duration**: ~55 min mandatory + ~30 min optional (see two-track map below)  
> **Pre-requisites**: VS Code with GitHub Copilot Chat extension, Node/Python/Java runtime, Git CLI, GitHub Copilot access, GitHub MCP, Mermaid extension for VS code.

---

## What You Will Build

An **Intelligent Task Management System (ITMS)** — a realistic business application that covers every phase of the Software Development Life Cycle, all guided by GitHub Copilot.

The system enables teams to create, assign, and track tasks, manage task dependencies, monitor project progress, and identify bottlenecks — all key pain points for software teams working across multiple members and workstreams.

**Core capabilities from [`requirement.md`](requirement.md):**

| Capability | Description |
|---|---|
| **Task Creation** | Create tasks with ID, title, priority, status, assignee, and due date |
| **Task Assignment** | Assign or reassign tasks to team members |
| **Dependency Management** | Link tasks with dependencies; auto-mark blocked tasks |
| **Status Tracking** | Track To Do / In Progress / Blocked / Completed with history |
| **Filtering & Listing** | Query tasks by status, priority, assignee, or due date |
| **Progress Summary** | Dashboard showing total, completed, in-progress, and blocked counts |

> The requirement is already in [`requirement.md`](requirement.md). You do **not** need to write the application yourself — Copilot does the heavy lifting. Your job is to learn how to **direct Copilot effectively** through each SDLC phase.

---

## Prerequisites

Participants should have the following set up before starting the workshop:

- [VS Code](https://code.visualstudio.com/download) with GitHub Copilot Chat extension
- Node/Python/Java runtime/.net SDK (depending on your language choice)
- GitHub Copilot access
- [Git CLI](https://git-scm.com/install/)
- [GitHub Copilot CLI](https://docs.github.com/en/copilot/how-tos/copilot-cli/set-up-copilot-cli/install-copilot-cli)
- [GitHub MCP](https://github.com/mcp/github/github-mcp-server)
- [Mermaid extension](https://marketplace.visualstudio.com/items?itemName=MermaidChart.vscode-mermaid-chart) for VS code

## Workshop Map

> **Two-Track Design**: Complete the **Mandatory Track** first (~50 min) — it covers every SDLC phase end-to-end with no gaps. Then pick any **Optional** exercises based on time and interest. Optional exercises are self-contained; no mandatory exercise depends on them.

---

### 🔵 Mandatory Track (~55 minutes)

These exercises form the core SDLC journey. Each builds directly on the previous one and introduces a distinct GitHub Copilot feature.

| # | Exercise | Copilot Feature | Duration |
|---|----------|----------------|----------|
| 01 | [Installing GitHub Copilot](workshop/exercise-01-installing-github-copilot.md) | GitHub Copilot Setup & Chat Participants Overview | 5 min |
| 02 | [Chat Participants](workshop/exercise-02-chat-participants.md) | Copilot Chat Participants (`@vscode`, `@terminal`, `@github`) | 5 min |
| 03 | [Prompt Engineering](workshop/exercise-03-prompt-engineering-and-agents.md) | Prompt Engineering — Four Pillars | 5 min |
| 04 | [Agents, Skills, Instructions & Prompts](workshop/exercise-04-instructions-agent-skills.md) | Agents, Skills, Instructions, Prompt Files | 5 min |
| 05 | [Setup & Custom Agents](workshop/exercise-05-setup-agents.md) | Custom Agents (`.agent.md`) | 5 min |
| 06 | [Generate BRD](workshop/exercise-06-brd.md) | BRD Custom Agent | 4 min |
| 07 | [Generate TSD](workshop/exercise-07-tsd.md) | TSD Custom Agent | 4 min |
| 08 | [Generate FRD](workshop/exercise-08-frd.md) | FRD Custom Agent | 4 min |
| 09 | [Custom Instructions](workshop/exercise-09-custom-instructions.md) | `.instructions.md` | 3 min |
| 10 | [Plan Mode – Implementation Plan](workshop/exercise-10-plan-mode.md) | Plan Mode | 4 min |
| 11 | [Create Implementation Prompt File](workshop/exercise-11-implementation-prompt.md) | Prompt Files | 3 min |
| 12 | [Build APIs with Local Agent](workshop/exercise-12-api-local-agent.md) | Local (Default) Agent | 5 min |
| 13 | [Design & Scaffold the UI](workshop/exercise-13-ui-design.md) | Local Agent — UI Scaffolding | 5 min |
| 14 | [Unit & Functional Tests](workshop/exercise-14-testing.md) | Local Agent + Prompt File | 5 min |
| 15 | [Security Review](workshop/exercise-15-security.md) | Security Prompt File | 4 min |
| 16 | [Build & Debug](workshop/exercise-16-build-debug.md) | Local Agent + Terminal | 5 min |

> **Why these 12?** They cover every key Copilot feature — Custom Agents → Prompt Files → Plan Mode → Local Agent → Testing → Security → UI Scaffolding — mirroring a real end-to-end SDLC from requirements to a running, full-stack application.

---

### 🟡 Optional Track (~30 minutes — pick any, in any order)

These exercises are self-contained. No mandatory exercise depends on them. Complete them if time allows, or revisit them after the workshop.

| # | Exercise | Copilot Feature | Duration | Best After |
|---|----------|----------------|----------|------------|
| 17 | [Create GitHub Issues via MCP](workshop/exercise-17-github-issues.md) | GitHub MCP + Prompt File | 5 min | Ex 11 |
| 18 | [Background Agent Task](workshop/exercise-18-background-agent.md) | Background Agent | 3 min | Ex 12 |
| 19 | [Context Map Skill](workshop/exercise-19-context-map.md) | Skills (`SKILL.md`) | 4 min | Ex 12 |
| 20 | [Database & SQL / PL/SQL](workshop/exercise-20-database-sql.md) | Local Agent + Instructions | 5 min | Ex 19 |
| 21 | [IaC & CI/CD](workshop/exercise-21-iac-cicd.md) | Custom Agent + Prompt File | 5 min | Ex 16 |

> **Note on Ex 17**: Requires a GitHub repository and a Personal Access Token with `repo` and `issues` scopes. Skip if GitHub MCP is not pre-configured in your environment.
> **Note on Ex 20**: References the context map from Ex 19. If you skip Ex 19, remove the `#context-map.md` reference from the prompts before sending.

---
> The workshop has been tested with the following AI models on GitHub Copilot: `Claude Sonnet 4.6`,`GPT-5.3-codex`. Results may vary with different models. If you encounter issues, try switching to one of these models in your Copilot settings.

> **Note:** Complete **Mandatory** exercises in order (each artifact feeds the next). **Optional** exercises can be done in any order after their recommended prerequisite, or revisited after the workshop.

## Workspace Structure After Workshop

```
SDLC2/
├── req.md                          ← Starting requirement (provided)
├── doc/
│   ├── brd.md                      ← Generated in Exercise 06
│   ├── tsd.md                      ← Generated in Exercise 07
│   └── frd.md                      ← Generated in Exercise 08
├── .github/
│   ├── copilot-instructions.md     ← Created in Exercise 09
│   ├── agents/
│   │   ├── brd.agent.md            ← Created in Exercise 05
│   │   ├── tsd.agent.md            ← Created in Exercise 05
│   │   ├── frd.agent.md            ← Created in Exercise 05
│   │   └── devops.agent.md         ← Created in Exercise 21
│   └── prompts/
    ├── implementation-plan.prompt.md  ← Created in Exercise 11
        ├── github-issues.prompt.md        ← Created in Exercise 17
        └── security-review.prompt.md      ← Created in Exercise 15
├── .github/skills/context-map/     ← Created in Exercise 19
├── src/                            ← API code created in Exercise 12+
├── ui/                             ← Frontend scaffolded in Exercise 13
├── tests/                          ← Tests created in Exercise 14
├── db/                             ← SQL scripts created in Exercise 20
└── infra/                          ← IaC scripts created in Exercise 21
```

---

## Key GitHub Copilot Features Covered

| Feature | Description |
|---------|-------------|
| **Custom Agents** | Scoped AI personas with tool restrictions and domain prompts |
| **Plan Mode** | Pre-execution planning before Copilot takes action |
| **Local Agent** | Default interactive coding agent in VS Code |
| **Background Agent** | Runs long tasks asynchronously while you work |
| **Prompt Files** | Reusable, parameterized prompt templates (`.prompt.md`) |
| **Instructions** | Always-on context injected into every Copilot conversation |
| **Skills** | On-demand workflow bundles loaded from `SKILL.md` |
| **GitHub MCP** | Model Context Protocol server for GitHub integration |
| **UI Scaffolding** | Generating full-stack frontend components from FRD use cases and live API routes |

---

## Getting Started

1. Click **"Use this template"** → **"Create a new repository"**
2. Set the owner to your GitHub account, enter a repository name (e.g. `itms-workshop`), and click **"Create repository"**
3. Create a folder and clone your new repository
4. Open the cloned repository in VS Code
5. Start with [Exercise 01](workshop/exercise-01-installing-github-copilot.md)

---

---

## Further Learning — Try next

Explore these exercises to go deeper on the Copilot features:

- [Integrate MCP with Copilot](https://github.com/skills/integrate-mcp-with-copilot) — Connect Model Context Protocol servers to extend Copilot with external tools and data sources
- [Create Applications with the Copilot CLI](https://github.com/skills/create-applications-with-the-copilot-cli) — Use GitHub Copilot CLI to scaffold and build applications from the terminal
- [Scale Institutional Knowledge using Copilot Spaces](https://github.com/skills/scale-institutional-knowledge-using-copilot-spaces) — Organise team knowledge and context into Copilot Spaces for consistent, organisation-wide AI assistance
- [Idea to App with Copilot Spark](https://github.com/skills/idea-to-app-with-spark) — Go from a natural language idea to a deployed application using Copilot Spark
- [Expand Your Team with the Copilot Coding Agent](https://github.com/skills/expand-your-team-with-copilot) — Delegate tasks to the Copilot coding agent and collaborate with it like a team member

---


> **Instructor Note**: Each exercise has a `> Instructor Guide` section visible only in the markdown source. Exercises are designed so attendees never need to copy code — they copy **prompts** and let Copilot generate the output.
