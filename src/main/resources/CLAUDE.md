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
Deployed and running. Swagger UI live at `/swagger-ui/index.html`. Portfolio-ready pending one manual step (see Open Issues).

## Open Issues (priority order)

### 1. Demo admin account (manual, not code)
Promote one account to ADMIN on the deployed Railway instance via direct DB query so admin behavior is demonstrable to reviewers. Requires production DB access — do this manually, not via the app.

## Resolved
- **Hybrid access model**: `SecurityConfig` now only requires `authenticated()` on project/task routes; ownership + admin-bypass logic lives in the service layer via `SecurityUtil.isAdmin()`.
- **NPE on unassigned tasks**: `deleteTask`/`updateTask` null-check `task.getUser()` before comparing username.
- **Null handling on `userId` in `updateTask`**: matches `createTask`'s null-safe lookup.
- **Tests**: `ProjectServiceImpl` and `UserServiceImpl` now have unit tests (ownership/admin-bypass coverage), alongside existing `TaskServiceImpl` tests.
- **Polish**: `pom.xml` metadata filled, stale `.gitignore` rule removed, no leftover commented-out `setStartTime` lines, GitHub Actions workflow runs `mvn test` on push.

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