# Exercise 02 — Chat Participants Workshop Demo

**Duration**: 5 minutes  
**Copilot Feature**: Copilot Chat participants ( `@vscode`, `@terminal`, `@github`)  
**Goal**: Build on Exercise 01 by practicing requirement-focused prompts with chat participants, chat variables, and slash commands.

---

## Background

**Chat participants** help you route a question to the right context so answers are faster and more accurate.

- `@vscode` explains editor actions and commands.
- `@terminal` explains command output and shell usage.
- `@github` helps with issues, pull requests, and collaboration workflows.

---

## What You Will Learn

1. How to understand `requirement.md` using `#file:` and `#selection`.
2. How to move from simple explanation to structured requirement extraction.
3. How chat variables and slash commands speed up and focus responses.

---
### Step 0 — Open Copilot Chat in Ask Mode
1. Open Copilot Chat: Ctrl+Alt+I (Windows/Linux) or Cmd+Option+I (macOS).
2. In the mode selector, choose Ask.
3. Keep Ask mode for all prompts in this exercise.
4. Run Step 1, Step 2, Step 3, then the Add-On prompts in order.

### Step 1 — `#file` for requirement understanding (file-grounded)

Prompt:

```
Read #file:requirement.md and explain the problem statement in simple language for a beginner.
```

### Step 2 — `@github` for GitHub workflow guidance

Prompt:

```
@github Explain issues, pull requests, and when to use each in a workshop project.
```
---

## Add-On: Chat Variables

Chat variables pin specific context into your prompt — no copy-pasting required. Here is the full reference:

| Variable | Scope | Best used for |
|---|---|---|
| `#file:` | One file | Grounding answers in a specific document |
| `#selection` | Highlighted text | Focused questions on a code or text snippet |
| `#codebase` | All indexed files | Cross-file search and summaries |
| `#sym` | A named symbol | Explaining or refactoring a function/class/variable |

---

### Step A — `#file:` variable

Pin Copilot to a single file.

Prompt:

```
From #file:requirement.md, create a table with columns: Requirement, Priority, Reason.
```

### Step B — `#selection` variable

Ask about only the text you have highlighted.

1. Open `requirement.md` and select only the "Functional Requirements" section.
2. In chat, enter this prompt:

```
Summarize #selection in 6 short bullets for product stakeholders.
```
---

## Add-On: Slash Commands

Slash commands trigger a specific Copilot action. Type `/` in chat to open the command picker.

| Command | What it does |
|---|---|
| `/explain` | Explains selected code or a file in plain language |
| `/fix` | Suggests a fix for a bug or error in the selection |
| `/tests` | Generates unit tests for the selected code |
| `/doc` | Generates a documentation comment for a function or class |

---

### Step 1 — discover available commands

1. Open Copilot Chat.
2. Type `/` and wait for the command picker to appear.
3. Browse the list — available commands depend on your VS Code and Copilot version.

### Step 2 — `/explain` with a file

Explain a whole file without opening it:

```
/explain #file:requirement.md
```
---

## Key Takeaways

- Use `#file:`, `#selection`, `#codebase`, `#sym`, and `#editor` to control exactly what Copilot reads.
- `#codebase` is broad (all files); `#file:` is precise (one file); `#selection` is surgical (highlighted text only).
- Slash commands (`/explain`, `/fix`, `/tests`, `/doc`) trigger specific Copilot actions — no long prompts needed.
- Build prompts progressively: explain first, then extract, then structure.

---

**Next**: [Exercise 03 — Prompt Engineering Workshop](exercise-03-prompt-engineering-and-agents.md)

