# Exercise 14 — Write Unit & Functional Tests

**Duration**: 5 minutes  
**Copilot Feature**: Local Agent + Prompt Files  
**Goal**: Generate comprehensive unit and integration/functional tests for the ITMS API.

---

## Background

Tests are where the FRD's acceptance criteria become code. Each **Given/When/Then** scenario in `doc/frd.md` maps to an automated test. Copilot can read those acceptance criteria and write tests that verify them — turning the FRD into a living test suite.

---

## Step 1 — Generate the Test Configuration

Send this prompt in Copilot Chat (Agent mode):

> **Note**: Adjust the prompt based on your tech stack (Node.js, Python, Java). And if you have skipped Exercise 19, make sure to update the prompt by removing the context map references `.github/skills/context-map/context-map.md`.

```
Reference the context map at .github/skills/context-map/context-map.md and .github/copilot-instructions.md.

Set up the testing infrastructure for the ITMS project:
1. Install and configure the appropriate test framework for the tech stack (Jest for Node.js, pytest for Python, JUnit for Java)
2. Create tests/unit/ and tests/integration/ directory structure
3. Set up test data configuration using the JSON seed files from src/data/ — no database required.
   Copy the JSON files to a tests/fixtures/ folder so tests work with isolated, known data.
4. Create a tests/helpers/ folder with:
   - A factory function to create test task objects and test user objects with different roles
     (Developer, Team Lead, Project Manager, QA Engineer)
   - A helper that resets the in-memory JSON store to the fixture state before each test
     (so tests are isolated and repeatable without a database)
5. Add test scripts to package.json / pyproject.toml:
   - "test:unit" — runs unit tests only
   - "test:integration" — runs integration tests
   - "test:all" — runs everything with coverage report
```

---

## Step 2 — Generate Unit Tests for the Service Layer

Send this prompt:

```
Read #frd.md (focus on FR-IDs for task management) and the service files in src/services/.

Generate unit tests in tests/unit/ for the Task Service:

Test Suite: TaskService.createTask()
- Test: Should create a task with status "To Do" when all required fields are provided
  (maps to: Given a Developer provides title, priority, and due date When they submit the task Then a "To Do" task is created)
- Test: Should throw ValidationError when title is missing
- Test: Should throw ValidationError when priority is not Low/Medium/High
- Test: Should throw NotFoundError when assignedUserId does not exist
- Test: Should correctly record the creator in createdBy field

Test Suite: TaskService.updateTaskStatus()
- Test: Should update status from "To Do" to "In Progress" successfully
- Test: Should set status to "Blocked" when a dependency task is not Completed
- Test: Should allow status change to "Completed" when all dependencies are Completed
- Test: Should throw BlockedTaskError when trying to start a blocked task
- Test: Should record status change in task history

Use mocks for the repository layer in all unit tests.
Each test must include Arrange / Act / Assert structure as comments.
```

---

## Step 3 — Generate Integration Tests for the API

Send this prompt:

```
Read #frd.md (focus on the Gherkin acceptance criteria in user stories US-001 to US-006).

Generate integration tests in tests/integration/ for the Task Management API:

Test Suite: POST /api/v1/tasks
- Scenario: "Developer creates a valid task"
  Given: A valid assignedUserId exists in the users fixture data
  When: POST /api/v1/tasks with { title, description, priority: "HIGH", assignedUserId, dueDate }
  Then: 201 Created, response has { success: true, data: { id, status: "TO_DO", priority: "HIGH" } }

- Scenario: "Task creation rejected when title is missing"
  When: POST /api/v1/tasks with missing title
  Then: 400 Bad Request, { success: false, error: { code: "VALIDATION_ERROR" } }

- Scenario: "Task creation rejected when assignedUserId does not exist"
  When: POST /api/v1/tasks with a random unknown assignedUserId
  Then: 400 Bad Request, { success: false, error: { code: "VALIDATION_ERROR" } }

Test Suite: PATCH /api/v1/tasks/:id/status
- Scenario: "User updates task from TO_DO to IN_PROGRESS"
  Given: A task with no blocking dependencies (use fixture task with no dependencies)
  When: PATCH /api/v1/tasks/:id/status with { status: "IN_PROGRESS" }
  Then: 200 OK, task status updated, history entry appended

- Scenario: "Task transitions to BLOCKED when dependency is incomplete"
  Given: A task whose dependency task is NOT COMPLETED (use fixture dependency data)
  When: PATCH /api/v1/tasks/:id/status with { status: "IN_PROGRESS" }
  Then: 422 Unprocessable Entity, { success: false, error: { code: "TASK_BLOCKED" } }

Test Suite: GET /api/v1/tasks
- Scenario: "List all tasks returns paginated results"
  When: GET /api/v1/tasks
  Then: 200 OK, { success: true, data: [...], meta: { total, page, limit } }

- Scenario: "Filter by status returns only matching tasks"
  When: GET /api/v1/tasks?status=TO_DO
  Then: All returned tasks have status TO_DO

Each integration test should reset the in-memory JSON store to fixture state before running using the test helper from Step 1.
```

---

## Step 4 — Run Tests and Check Coverage

> **(Optional) Database stored procedure tests**: If you have completed [Exercise 20](exercise-20-database-sql.md) and are running a real database, ask Copilot to also generate tests in `tests/unit/database/` for the `update_task_status()`, `add_task_dependency()`, and `resolve_task_dependency()` PL/pgSQL functions.

Send this prompt:

```
Run the unit tests using the terminal and show me the coverage report.
Identify any files in src/ that have less than 80% test coverage and list them.
```

---

## Step 5 — Generate Functional Tests with Selenium WebDriver

Send this prompt in Copilot Chat (Agent mode):
```
Read #frd.md and generate functional test cases for the ITMS application using Selenium WebDriver.

Requirements:
Use Page Object Model: create a TaskPage class in tests/functional/pages/<file name>
Use WebDriverWait + expected_conditions — no time.sleep()
Each test must have # Arrange / # Act / # Assert comments
```

**Note**: This step uses Selenium WebDriver to generate functional tests via Copilot Agent mode.
> If you have the **Playwright MCP** or any other browser automation MCP configured in your VS Code MCP settings, you can send the same prompt and Copilot will use that MCP to interact with the live application, discover real selectors, and generate tests automatically.


## Verify

- [ ] Unit tests exist for `TaskService` with at least 8 test cases
- [ ] Integration tests cover the 4 main API endpoints
- [ ] Tests use the factory helper and JSON store reset helper
- [ ] Each test follows Arrange/Act/Assert structure
- [ ] Coverage report is generated
- [ ] No test requires a running database or live server to pass
- [ ] Functional test cases generated for the ITMS application.

---

## Key Takeaway

> When Copilot writes tests from the FRD's Gherkin criteria, the tests become a **living specification**. Every test failure tells you exactly which user story is broken. This is the promise of Behavior-Driven Development — and Copilot makes it practical by eliminating the tedious work of writing the test boilerplate.

---

**Next**: [Exercise 15 — Security Review](exercise-15-security.md)
