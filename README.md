# Pen Task Management API

A RESTful backend for project and task management. Users can create projects, break them down into tasks, assign collaborators, and track progress through status stages — all secured with JWT authentication.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.0.6 |
| Database | PostgreSQL 17 |
| Security | Spring Security + JWT (JJWT 0.12.5) |
| Infrastructure | Docker + Docker Compose |
| Validation | Jakarta Bean Validation |
| Docs | SpringDoc OpenAPI (Swagger UI) |

---

## Getting Started

### Prerequisites

- Docker & Docker Compose
- Java 21
- Maven

### 1. Clone the repository

```bash
git clone https://github.com/your-username/taskmanagement.git
cd taskmanagement
```

### 2. Configure environment variables

Create a `.env` file in the project root:

```env
DB_USER=your_db_user
DB_PASSWORD=your_db_password
DB_NAME=penTask
DB_URL=jdbc:postgresql://localhost:5433/penTask
PGADMIN_EMAIL=admin@admin.com
PGADMIN_PASSWORD=your_pgadmin_password
JWT_SECRET=your_base64_secret
JWT_EXPIRATION=3600000
```

### 3. Start the database

```bash
docker-compose up -d
```

PostgreSQL starts on port `5433`. pgAdmin is available at `http://localhost:5050`.

### 4. Run the application

```bash
./mvnw spring-boot:run
```

API available at `http://localhost:8080`.

---

## API Documentation

Interactive Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

---

## API Reference

### Auth

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/api/auth/register` | Public | Register a new user |
| POST | `/api/auth/login` | Public | Login and receive JWT |

**Register**
```json
POST /api/auth/register
{
  "name": "Sergio",
  "surname": "Perez",
  "email": "sergio@example.com",
  "username": "sergio",
  "password": "securepassword"
}
```

**Login**
```json
POST /api/auth/login
{
  "username": "sergio",
  "password": "securepassword"
}
```
```json
{
  "username": "sergio",
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

All subsequent requests require:
```
Authorization: Bearer <token>
```

---

### Projects

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/api/project` | Required | Create a project |
| GET | `/api/project` | Required | List projects (paginated) |
| GET | `/api/project/{id}` | Required | Get project by ID |
| PUT | `/api/project/{id}` | Owner only | Update a project |
| DELETE | `/api/project/{id}` | Owner only | Delete a project |

```json
POST /api/project
{
  "name": "My Project",
  "description": "Project description",
  "endDateTime": "2025-12-31T23:59:59",
  "userIds": [1, 2]
}
```

Supports pagination and sorting:
```
GET /api/project?page=0&size=10&sort=name,asc
```

---

### Tasks

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/api/task` | Required | Create a task |
| GET | `/api/task` | Required | List tasks (paginated) |
| GET | `/api/task/{id}` | Required | Get task by ID |
| PUT | `/api/task/{id}` | Assignee only | Update a task |
| DELETE | `/api/task/{id}` | Assignee only | Delete a task |

```json
POST /api/task
{
  "name": "Design database schema",
  "description": "Define all entities and relationships",
  "endDateTime": "2025-06-30T18:00:00",
  "userId": 1,
  "projectId": 1
}
```

---

### Users

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| GET | `/api/user` | Required | List users (paginated) |
| GET | `/api/user/{id}` | Required | Get user by ID |
| PATCH | `/api/user/{id}` | Required | Update a user |
| DELETE | `/api/user/{id}` | Required | Delete a user |

---

## Data Model

```
User ──< Task
User >──< Project
Project ──< Task
```

- A **Project** has many **Tasks** and many **Users**
- A **Task** belongs to one **Project** and optionally one **User**
- A **User** can belong to many **Projects** and have many **Tasks**

**TaskStatus:** `PENDING` | `ONGOING` | `FINISHED`

**ProjectStatus:** `PENDING` | `ONGOING` | `FINISHED`

---

## Security Model

- JWT required on all endpoints except `/api/auth/register` and `/api/auth/login`
- Project update/delete restricted to the project creator
- Task update/delete restricted to the assigned user
- Passwords hashed with BCrypt

---

## Error Responses

```json
{
  "message": "Project not found",
  "path": "uri=/api/project/99",
  "timestamp": "2025-06-16T10:30:00"
}
```

| Status | Scenario |
|---|---|
| 400 | Validation failure |
| 401 | Missing or invalid token |
| 403 | Insufficient permissions |
| 404 | Resource not found |
| 409 | Username already exists |
| 500 | Unexpected server error |

---

## Running Tests

```bash
./mvnw test
```
