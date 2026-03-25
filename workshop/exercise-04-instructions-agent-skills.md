# Exercise 04 — Agents, Skills, Instructions, and Prompts

Building great experiences with GitHub Copilot starts with understanding the core primitives that shape how Copilot behaves in different contexts. This article clarifies what each artifact does, how it is packaged inside this repository, and when to use it.

## Agents

Agents are configuration files (`*.agent.md`) that describe:

- The tasks they specialize in (for example, "Terraform Expert" or "LaunchDarkly Flag Manager").
- Which tools or MCP servers they can invoke.
- Optional instructions that guide the conversation style or guardrails.

When you assign an issue to Copilot or open the **Agents** panel in VS Code, these configurations let you swap in a specialized assistant. Each agent in this repo lives under `.github/agents/` and includes metadata about the tools it depends on.

### When to reach for an agent

- You have a recurring workflow that benefits from deep tooling integrations.
- You want Copilot to proactively execute commands or fetch context via MCP.
- You need persona-level guardrails that persist throughout a coding session.

> **Try It: Create an Agent**
> 1. Open GitHub Copilot Chat.
> 2. Click the **Agents** dropdown at the bottom.
> 3. Select **Configure Custom Agents...** -> **Create new custom agent**.
> 4. Save it in `.github/agents/my-first.agent.md`.

## Skills

Skills are self-contained folders that package reusable capabilities for GitHub Copilot. Each skill lives in its own directory and contains a `SKILL.md` file along with optional bundled assets such as reference documents, templates, and scripts.

A `SKILL.md` defines:

- A **name** (used as a `/command` in VS Code Chat and for agent discovery).
- A **description** that tells agents and users when the skill is relevant.
- Detailed instructions for how the skill should be executed.

### When to reach for a skill

- You want to standardize how Copilot responds to a recurring task.
- You need bundled resources (templates, schemas, scripts) to complete the task.
- You want agents to discover and invoke the capability automatically.

> **Try It: Define a Skill**
> 1. Create a folder `.github/skills/code-reviewer/`.
> 2. Create a file `.github/skills/code-reviewer/SKILL.md`.
> 3. Add frontmatter with `name: review` and `description: Professional code review`.

## Instructions

Instructions (`*.instructions.md` or `copilot-instructions.md`) provide background context that Copilot reads whenever it works on matching files. They often contain:

- Coding standards or style guides (naming conventions, testing strategy).
- Framework-specific hints (Angular best practices, .NET analyzers to suppress).
- Repository-specific rules ("never commit secrets").

### When to reach for instructions

- You need persistent guidance that applies across many sessions.
- You are codifying architecture decisions or compliance requirements.
- You want Copilot to understand patterns without manually pasting context.

> **Try It: Set Global Standards**
> 1. Create a file `.github/copilot-instructions.md`.
> 2. Add: "Always use arrow functions for React components and include JSDoc comments."
> 3. Copilot will now follow this for every code generation in this workspace.

## Prompts

Prompts (`*.prompt.md`) are reusable snippets of text that you can quickly inject into a chat session using the `/` slash command. They are simpler than skills and focus on one-off requests.

### When to reach for a prompt

- You have a long, specific prompt you find yourself typing often.
- You want to share a specific "recipe" with your team without a full skill structure.
- You need a quick way to trigger a specific behavior via a slash command.

> **Try It: Create a Prompt Shortcut**
> 1. Create a file `.github/prompts/refactor.prompt.md`.
> 2. Add: "Refactor this code for better readability and performance, ensuring no logic changes."
> 3. Usage: Type `/refactor` in chat to immediately use this prompt.

## How the artifacts work together

Think of these artifacts as complementary layers:

1. **Instructions** lay the groundwork with long-lived guardrails.
2. **Prompts** provide quick, reusable shortcuts for common tasks.
3. **Skills** let you trigger rich, reusable workflows on demand—and let agents discover those workflows automatically.
4. **Agents** bring the most opinionated behavior, bundling tools and instructions into a single persona.

By combining all four, teams can achieve:

- Consistent onboarding for new developers.
- Repeatable operations tasks with reduced context switching.
- Tailored experiences for specialized domains (security, infrastructure, data science, etc.).

## Next steps

- Explore the rest of the **Fundamentals** track for deeper dives on chat modes, collections, and MCP servers.
- Browse the [Awesome Agents](https://awesome-copilot.github.com/agents/), [Skills](https://awesome-copilot.github.com/skills/), and [Instructions](https://awesome-copilot.github.com/instructions/) directories for inspiration.
- Try generating your own artifacts, then add them to the repo to keep the Learning Hub evolving.

---

**Next**: [Exercise 05 — Setup & Create Custom Agents](exercise-05-setup-agents.md)

---