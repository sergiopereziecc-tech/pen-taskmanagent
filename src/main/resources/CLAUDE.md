# CLAUDE.md — PEN Task Management API

## Project Overview
PEN is a Trello-style task management REST API built as a portfolio project. It is complete and deployed live at `pen-taskmanagent-production.up.railway.app`.

## Tech Stack
- Java 21, Spring Boot, Spring Security (JWT)
- PostgreSQL, JPA/Hibernate
- Docker (multi-stage Dockerfile), Docker Compose for local dev
- Railway (production deployment)
- Swagger/OpenAPI for docs
- VSCode as editor

## Architecture
Layered: Controller → Service → Repository → Mapper → DTO pattern throughout. No entity leaking into responses. Global exception handling via `GlobalExceptionHandler`. Bean Validation on request DTOs. Pagination on list endpoints.

## Auth
Stateless JWT filter (`JwtFilter`). `SecurityUtil` provides `getCurrentUsername()`. Role-based: `USER` (default on registration) and `ADMIN` (granted manually via DB only).

## Current State
Deployed and running. Swagger UI live at `/swagger-ui/index.html`. The following issues need fixing before this is fully portfolio-ready.

## Open Issues (priority order)

### 1. Hybrid access model (CRITICAL)
`SecurityConfig` currently gates all write routes on `ROLE_ADMIN`. No user can ever become ADMIN through the app. A freshly registered user gets 403 on every action.

**Fix:** Drop `hasRole("ADMIN")` from project/task routes in `SecurityConfig`, require only `authenticated()`. Move ownership + admin-bypass logic into service layer. Add `isAdmin()` helper to `SecurityUtil`.

Specific service changes needed:
- `ProjectServiceImpl`: creator or admin can update/delete
- `TaskServiceImpl.createTask`: verify caller is project creator, collaborator, or admin
- `TaskServiceImpl.updateTask/deleteTask`: assignee, project creator, or admin
- `UserServiceImpl.updateUser/deleteUser`: self or admin
- `GET /api/user`: open to any authenticated user (needed for collaborator lookup)

### 2. NPE on unassigned tasks
`deleteTask` and `updateTask` call `task.getUser().getUsername()` with no null check. Creating an unassigned task then deleting/updating it throws NPE → generic 500.

**Fix:** Null-check `task.getUser()` before username comparison.

### 3. Inconsistent null handling on `userId` in `updateTask`
`createTask` handles null `userId` gracefully. `updateTask` calls `userRepository.findById(taskRequest.userId())` unconditionally → `IllegalArgumentException` → generic 500.

**Fix:** Match null handling between create and update.

### 4. Demo admin account
Promote one account to ADMIN on the deployed Railway instance via direct DB query so admin behavior is demonstrable to reviewers.

### 5. Tests
Only `TaskServiceImpl` has tests. Need tests for `ProjectServiceImpl` and `UserServiceImpl`, including the new ownership/admin-bypass logic.

### 6. Polish
- Fill `pom.xml` metadata (`<name>`, `<description>`, `<url>`, `<developer>`)
- Remove stale `.gitignore` rule for `application.properties`
- Remove commented-out `setStartTime` lines in `ProjectServiceImpl` and `TaskServiceImpl`
- Add GitHub Actions workflow running `mvn test` on push

## Git Conventions
- No commits on `main` directly
- Feature branches: `feature/`, `fix/`, `refactor/`
- Conventional commits: `feat:`, `fix:`, `refactor:`, `test:`, `chore:`
- One commit per meaningful unit of work

## Local Dev
- Environment variables loaded via `.vscode/launch.json` with `envFile` pointing to `.env`
- `.env` is gitignored — never commit secrets
- Docker Compose available for local Postgres

## What's Working
- Full CRUD for users, projects, tasks
- JWT registration and login
- Ownership-based authorization (ForbiddenException logic in services)
- Pagination on list endpoints
- Swagger UI
- Multi-stage Docker build
- Railway deployment with managed Postgres