# Exercise 21 — Infrastructure as Code & CI/CD Pipelines

**Duration**: 5 minutes  
**Copilot Feature**: DevOps Custom Agent + Prompt Files  
**Goal**: Use the DevOps agent to generate Docker, Terraform/Bicep, and GitHub Actions pipelines for the ITMS application.

---

> ---
> 🟡 **OPTIONAL EXERCISE**
>
> This exercise is **not required** for the mandatory track. It is the infrastructure and deployment capstone — ideal if you have extra time after Exercise 14 or want to explore IaC and CI/CD automation with Copilot after the workshop.
>
**Best after**: Exercise 16 &nbsp;|&nbsp; This is the final exercise in the workshop.
> ---


## Background

The final phase of SDLC is deployment infrastructure. The **DevOps Agent** (`devops.agent.md`) you created in Exercise 05's setup is a specialized agent that reads the TSD's deployment architecture and generates production-ready IaC and CI/CD configurations.

---

## Step 1 — Switch to the DevOps Agent

1. In Copilot Chat, click the agent selector
2. Select **DevOps & IaC Agent**

---

## Step 2 — Create Docker Configuration

Send this prompt:

```
Read doc/tsd.md (Infrastructure & Deployment section) and the application code in src/.

Create the Docker configuration:

1. infra/docker/Dockerfile
   - Multi-stage build: builder stage (compile/install deps) and final stage (minimal runtime image)
   - Run as non-root user (security best practice)
   - Only copy the production artifacts to the final stage
   - Expose the correct port (from .env.example)
   - Include a HEALTHCHECK instruction using the /api/v1/health endpoint

2. infra/docker/docker-compose.yml
   - Service: api (our application)
   - Service: db (PostgreSQL with the correct version from tsd.md)
   - Service: redis (for session/rate limiting)
   - Mount db/migrations/ into the db service to auto-run migrations on startup
   - Use .env file for environment variables
   - Include a depends_on with health checks

3. .dockerignore
   - Exclude: node_modules, .env, .git, tests/, coverage/, *.log, doc/

Build and verify the Docker image can be built successfully.
```

---

## Step 3 — Create Infrastructure as Code (Azure)

Send this prompt:

```
Create Azure Bicep templates in infra/bicep/ for the ITMS production environment.

The infrastructure should match the TSD deployment architecture.

1. infra/bicep/main.bicep — entry point that deploys all modules
2. infra/bicep/modules/container-app.bicep — Azure Container App for the API
3. infra/bicep/modules/database.bicep — Azure Database for PostgreSQL (Flexible Server)
4. infra/bicep/modules/keyvault.bicep — Azure Key Vault for secrets (DB password, JWT secret, SendGrid key)
5. infra/bicep/modules/container-registry.bicep — Azure Container Registry
6. infra/bicep/modules/monitoring.bicep — Application Insights + Log Analytics Workspace
7. infra/bicep/parameters/dev.json — parameter file for dev environment
8. infra/bicep/parameters/prod.json — parameter file for production

Security requirements:
- Container App uses managed identity to access Key Vault (no secrets in env vars)
- PostgreSQL only accessible from within the VNet (no public endpoint)
- Container Registry uses admin-disabled, access via managed identity only
- All resources tagged: environment, project=ITMS, owner

Also create infra/bicep/README.md with:
- Prerequisites (Azure CLI, Bicep CLI versions)
- Deployment commands for dev and prod
- How to update secrets in Key Vault
```

> **If preferring Terraform**: Replace "Bicep" with "Terraform" in the prompt above.

---

## Step 4 — Create CI/CD Pipelines

Send this prompt:

```
Create GitHub Actions workflow files in .github/workflows/:

1. .github/workflows/ci.yml — Continuous Integration (triggers on: pull_request to main)
   Steps:
   a. Checkout code
   b. Setup language runtime (cache dependencies)
   c. Install dependencies
   d. Run linter (fail on warnings)
   e. Run unit tests (fail if coverage < 80%)
   f. Build Docker image (fail if build fails)
   g. Run Trivy container security scan (fail on CRITICAL vulnerabilities)
   h. Run Semgrep static analysis (fail on HIGH findings)
   i. Comment results on the PR

2. .github/workflows/cd-staging.yml — Deploy to Staging (triggers on: push to main)
   Steps:
   a. Build and push Docker image to ACR with tag: main-{commit-sha}
   b. Run database migrations against staging DB
   c. Deploy to Azure Container App (staging slot) using GitHub OIDC (no secrets stored)
   d. Run smoke tests: GET /api/v1/health must return 200
   e. Run integration tests against staging environment
   f. Notify Teams channel on success/failure

3. .github/workflows/cd-production.yml — Deploy to Production (triggers on: push tag v*.*.*)
   Steps:
   a. Require manual approval gate (environment: production)
   b. Build and push Docker image with tag: {version}
   c. Run database migrations against production DB
   d. Blue-green deploy: deploy to Container App revision, shift traffic gradually
   e. Health check: poll /api/v1/health for 2 minutes
   f. On failure: automatic rollback to previous revision
   g. Create GitHub Release with changelog

Use GitHub OIDC (not service principal passwords) for Azure authentication.
Store all secrets in GitHub Environments (not repository secrets).
```

---

## Step 5 — Create the infra/README.md

Send:

```
Create infra/README.md with complete deployment documentation:

1. Architecture overview (text description of what was created)
2. Prerequisites list (tools, CLI versions, Azure permissions needed)
3. First-time setup steps (create service principal, configure GitHub secrets, run Bicep deploy)
4. Day-to-day deployment guide (how CI/CD works, how to trigger a release)
5. How to roll back to a previous version
6. Environment variables reference (what each secret in Key Vault is for)
7. Monitoring: where to find logs, how to set up alerts for error rate spikes
8. Cost estimation for the Azure resources at 500 concurrent users
```

---

## Verify

Open the created files and check:

- [ ] Dockerfile uses multi-stage build and non-root user
- [ ] `docker-compose.yml` starts both app and database
- [ ] Bicep/Terraform templates are parameterized (no hardcoded values)
- [ ] CI workflow has test + security scan gates
- [ ] CD staging runs integration tests before marking deploy as success
- [ ] CD production requires manual approval
- [ ] No secrets hardcoded in any YAML, Dockerfile, or IaC file

---

## Workshop Complete! 🎉

You have completed the full E2E SDLC using GitHub Copilot:

| Phase | Output |
|-------|--------|
| Planning | `doc/brd.md`, `doc/tsd.md`, `doc/frd.md` |
| Project Management | GitHub Issues via MCP |
| Development | `src/` — REST APIs with auth, task management, dependencies, and reporting |
| Database | `db/migrations/`, `db/procedures/`, stored procedures with PL/SQL |
| Testing | `tests/` — unit + integration tests from FRD acceptance criteria |
| Security | OWASP review report, vulnerability fixes |
| Infrastructure | `infra/` — Docker, Bicep/Terraform, GitHub Actions CI/CD |

---

## What to Explore Next

| Topic | How |
|-------|-----|
| Claude or GPT-4o as base model | Change model in Copilot Chat settings |
| Custom skill for API docs | Create `.github/skills/api-docs/SKILL.md` |
| Automated PR review agent | Create `.github/agents/code-review.agent.md` |
| Prompt for performance profiling | Add `.github/prompts/performance-review.prompt.md` |
| Pre-commit security hook | Create `.github/hooks/pre-commit.json` |

---

**Return to**: [Workshop Overview (README.md)](../README.md)
