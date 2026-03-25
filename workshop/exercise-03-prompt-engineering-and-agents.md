# Exercise 03 — Prompt Engineering Workshop

**Duration**: 5 minutes | **Goal**: Learn the four pillars of strong prompts.

---

## Background

**Prompt engineering** is writing clear, structured instructions to get better AI responses.

- Weak prompts = vague answers → rework needed
- Strong prompts = actionable answers → production-ready code

This workshop helps you to build strong prompts using four pillars and two practical techniques.

---

## The Four Pillars

| Pillar | Purpose | Example |
|--------|---------|---------|
| **Context** | Background information, environment, and constraints. | "I am working on a React project using TypeScript and Tailwind CSS..." |
| **Intent** | The goal or the specific action you want the AI to perform. | "Generate a responsive navigation bar component..." |
| **Clarity** | Use unambiguous language and define the persona. | "Act as a senior frontend developer and use functional components..." |
| **Specificity** | Detailed requirements, formatting, and edge cases. | "Include a mobile hamburger menu, a logo placeholder, and links for Home, About, and Contact. Return only the code." |

The synergy of these pillars ensures that the AI understands not just *what* to do, but *how* and *why* to do it within your specific project boundaries.

---

### Open Copilot Chat in Ask Mode
1. Open Copilot Chat: Ctrl+Alt+I (Windows/Linux) or Cmd+Option+I (macOS).
2. In the mode selector, choose Ask.
3. Keep Ask mode for all prompts in this exercise.

## Build Strong Prompts: Vague → Improved

### Scenario 1: Creating a Data Utility

**Vague Prompt (High ambiguity):**
```
Write a function to format dates.
```

**Improved Prompt (Combining Context, Intent, Clarity, and Specificity):**
Open [workshop/sample-data/date_utils.js](../workshop/sample-data/date_utils.js) and use this prompt in Chat:
```
You are a specialist in internationalization building a financial dashboard in a Node.js environment. Refactor the `formatDate` function in #file:date_utils.js to format ISO date strings. The function should take a date string and a locale as arguments and return the date in 'DD-MMM-YYYY' format (e.g., 19-Mar-2026). Ensure it handles null or invalid date inputs by returning an empty string.
```

**Why it works:** By providing the environment (Node.js) and a persona (i18n specialist), you ensure the AI uses relevant libraries. Explicitly defining edge case behavior (null inputs) prevents bugs in the generated code.

### Scenario 2: Refactoring Code

**Vague Prompt (Subjective request):**
```
Make this code better.
```

**Improved Prompt (Refining via Progressive Disclosure):**
Open [workshop/sample-data/process_legacy_data.py](../workshop/sample-data/process_legacy_data.py) and use this prompt in Chat:
```
Act as a performance tuning expert. Refactor the `process_data` function in #file:process_legacy_data.py to improve performance and readability. Use the `pandas` library instead of the standard `csv` module to minimize memory usage for files up to 1GB. Provide a brief explanation of the structural changes you made.
```

**Why it works:** "Better" is vague; specifying "performance" and "memory usage" gives the AI a clear optimization target. Restricting the toolset to `pandas` ensures the output fits your technical stack.

### Scenario 3: Documentation and Testing

**Vague Prompt (Missing requirements):**
```
Write tests for my component.
```

**Improved Prompt (Using Output Specification):**
Open [workshop/sample-data/Button.jsx](../workshop/sample-data/Button.jsx) and use this prompt in Chat:
```
You are a Quality Engineer. Generate unit tests for the `Button` component in #file:Button.jsx using Vitest and React Testing Library. Follow the Arrange-Act-Assert (AAA) pattern. Ensure you test that the click handler is called when NOT loading, and that the button is disabled when the `isLoading` prop is true. Provide the code in a single file block with all necessary imports.
```

**Why it works:** Specifying the testing framework (Vitest) and the pattern (AAA) results in code that matches your team's style guide immediately, saving time on manual cleanup.

**Why it works:** Specifying the testing framework (Vitest) and the pattern (AAA) results in code that matches your team's style guide immediately, saving time on manual cleanup.

**Practice**: Try comparing the output of a vague prompt versus an improved prompt in Copilot Chat. Notice how the improved prompt reduces follow-up questions and provides more relevant code.

---

## Two Prompting Techniques

**Technique 1 — Progressive Disclosure:**
```
You are a Python developer. Write password validation function.
```
Follow-up: `Now add: min 8 chars, 1 uppercase, 1 number, 1 special char.`
Follow-up: `Add 5 pytest test cases.`

**Technique 2 — Output Specification:**
```
You are a DevOps Engineer. Compare REST vs GraphQL.
Output: markdown table, columns: Feature | REST | GraphQL | Best for. Keep 2-3 words per cell.
```


---

## Key Takeaways

- All four pillars together make production-ready prompts.
- Progressive disclosure: start simple, refine with follow-ups.
- Specify output format to prevent vague responses.
- Weak prompts skip one or more pillars.

---

---

**Next**: [Exercise 04 — Agents, Skills, Instructions, and Prompts](exercise-04-instructions-agent-skills.md)


