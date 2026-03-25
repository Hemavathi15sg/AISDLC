# Exercise 01 — Installing Your IDE & GitHub Copilot

**Duration**: 5 minutes  
**Copilot Feature**: GitHub Copilot Setup & Chat Participants Overview  
**Goal**: Install a supported IDE, set up GitHub Copilot, understand the data flow, and learn how chat participants work.

---

## Background

**GitHub Copilot** is an AI-powered coding assistant that offers inline code completions, a chat interface, and autonomous agents. It is available to individual developers, teams, enterprises, and students via GitHub accounts with an active subscription or free trial.

**What you need:**
- GitHub account ([github.com/signup](https://github.com/signup))
- Active Copilot subscription or free trial
- Supported IDE (VS Code recommended)
---

## Step 1 — Install Visual Studio Code

1. Go to [https://code.visualstudio.com](https://code.visualstudio.com) and download the installer for your operating system (Windows / macOS / Linux).
2. Run the installer and follow the on-screen instructions.
3. Launch VS Code once the installation is complete.

> **Already installed?** Make sure you are on **VS Code 1.90 or later** (`Help → About` to check). Earlier versions may not support the latest Copilot Chat features.

---

## Step 2 — Install the GitHub Copilot Extensions

GitHub Copilot in VS Code requires **extensions**:

| Extension | Purpose |
|-----------|---------|
| **GitHub Copilot Chat** | Chat panel, agents, inline chat |

**Installation steps:**

1. Open **Extensions** (`Ctrl+Shift+X` / `Cmd+Shift+X`).
2. Install  **GitHub Copilot Chat** (publisher: GitHub).
3. Reload VS Code if prompted.

> Both extensions are bundled in the latest VS Code releases. If you see them already listed under "Installed", skip this step.

---

## Step 3 — Sign In to GitHub Copilot

1. After installation, a **GitHub Copilot icon** (the Copilot logo) will appear in the bottom-right status bar.
2. Click the icon (or open the Command Palette with `Ctrl+Shift+P` / `Cmd+Shift+P` and run **"GitHub Copilot: Sign In"**).
3. VS Code will open a browser window asking you to authorise GitHub Copilot.
4. Sign in with your GitHub account and click **Authorize**.
5. Return to VS Code — the status bar icon should now show a green check or the Copilot logo without an error indicator.

> **No licence yet?** Visit [github.com/features/copilot](https://github.com/features/copilot) and activate a free trial before signing in.

---

## Step 4 — Verify the Setup

1. In any file, type `def greet(name):` and confirm an inline suggestion appears; press **Tab** to accept.
2. Open Copilot Chat (`Ctrl+Alt+I` / `Cmd+Option+I`), type `Hello!`, and confirm you get a response.

If both work, you are ready for the workshop.

---

## Step 5 — Understanding Data Flow & Chat Participants

This mini activity is designed for a fresh setup, even if you have no project files yet.

1. Open Copilot Chat (`Ctrl+Alt+I` / `Cmd+Option+I`).
2. Try `@vscode` with this prompt:
   `@vscode How do I split the editor into two side-by-side panes?`
3. Open the terminal(`Ctrl+~`) and run a simple command:
   - Windows: `get-Date`
   - macOS/Linux: `date`
4. Ask `@terminal`:
   `@terminal Explain the last command output in simple words.`


### What to Notice

- `@vscode` explains IDE usage and shortcuts.
- `@terminal` explains commands and terminal output.


### Data Flow 

1. You type a prompt.
2. The selected participant receives it.
3. Copilot processes based on that participant's specialty.
4. The response appears in chat.

> In Exercise 05, you will create custom agents that behave like specialized participants.

---

## Key Takeaway

> GitHub Copilot is not just an autocomplete tool — it is an **AI pair programmer** with a chat interface and agent capabilities. The remaining exercises in this workshop build on top of each other to show you how to use it across the full software development lifecycle.

---

**Next**: [Exercise 02 — Working with GitHub Copilot: Chat Participants](exercise-02-chat-participants.md)
