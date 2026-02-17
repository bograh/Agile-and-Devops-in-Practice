# Sprint 1 – Execution Plan (1 Week)

## Sprint Goal

Deliver secure authentication (Registration + JWT Login) and establish automated CI pipeline with tests.

---

# Deliver Work (Increment 1)

## Stories Delivered

### US1 – Registration

* Email + password registration
* Password encrypted using `BCrypt`
* Duplicate email validation
* Default role: `USER`
* Proper HTTP status codes
* Unit tests for service layer

### US2 – JWT Authentication

* Login endpoint
* Credential validation
* JWT generation
* Token contains:

  * `sub` → userId
  * `role`
  * `iat`
  * `exp`
* JWT filter added to security chain
* Protected test endpoint to validate token
* Integration test for secured endpoint

---

# Version Control Strategy (Iterative History)

Since you’re building this as a professional-grade portfolio project, your Git history must show incremental thinking.

## Branching Strategy

```
main
 ├── feature/user-registration
 ├── feature/jwt-authentication
 └── feature/ci-pipeline
```

---

## Suggested Commit Timeline (Day-by-Day)

### Day 1 – Project Setup

```
chore: initialize spring boot project structure
chore: configure dependencies (security, jwt, test)
chore: configure application.yml
```

---

### Day 2 – Registration Domain Layer

```
feat: create User entity and Role enum
feat: create UserRepository
feat: implement UserService registration logic
test: add unit tests for UserService
```

---

### Day 3 – Security & Password Encoding

```
feat: configure BCrypt password encoder
feat: implement registration controller
test: add registration integration test
fix: prevent duplicate email registration
```

Merge `feature/user-registration` → `main`

---

### Day 4 – JWT Infrastructure

```
feat: create JwtTokenProvider
feat: implement token generation with claims
feat: implement token validation logic
test: add jwt provider unit tests
```

---

### Day 5 – Authentication Flow

```
feat: implement login endpoint
feat: configure authentication manager
feat: add jwt authentication filter
feat: secure test endpoint
test: add authentication integration test
```

Merge `feature/jwt-authentication` → `main`

---

### Day 6 – CI/CD Pipeline

```
ci: add github actions workflow
ci: configure maven build + test job
ci: fail pipeline on test failure
```

Merge `feature/ci-pipeline` → `main`

---

# CI/CD – GitHub Actions Example

`.github/workflows/ci.yml`

```yaml
name: CI Pipeline

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Set up JDK 21
        uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '21'

      - name: Build and Test
        run: mvn clean verify

      - name: Build Docker Image
        run: docker build -t property-api .
```

---

# Testing Strategy

Minimum required:

### Unit Tests

* `UserServiceTest`
* `JwtTokenProviderTest`

### Integration Tests

* Registration endpoint
* Login endpoint
* Access protected endpoint with valid token
* Access protected endpoint without token → 401

Use:

* `@SpringBootTest`
* `@AutoConfigureMockMvc`
* `Testcontainers` (optional bonus)

---

# Sprint Review Document (What You Deliver)

Create a file:

```
/docs/sprint-1-review.md
```

## Include:

### ✔ Delivered Features

* User Registration
* JWT Authentication
* Protected Endpoint
* CI Pipeline

### ✔ Screenshots

* Successful GitHub Actions run
* Postman:

  * Register → 201
  * Login → returns JWT
  * Access protected endpoint with JWT → 200
  * Without JWT → 401

### ✔ API Summary

| Endpoint           | Method | Description           | Auth Required |
| ------------------ | ------ | --------------------- | ------------- |
| /api/auth/register | POST   | Register user         | No            |
| /api/auth/login    | POST   | Authenticate user     | No            |
| /api/test/secure   | GET    | Test secured endpoint | Yes           |

---

# Sprint Retrospective (Document)

Create:

```
/docs/sprint-1-retrospective.md
```

### What Went Well

* Clear story breakdown
* Incremental commits
* CI pipeline caught test failure early

### What Didn’t Go Well

* Spent too much time debugging JWT filter order
* Security config complexity slowed velocity

### Improvements for Sprint 2

- Write security configuration tests earlier
- Break stories into smaller technical tasks before coding
- Add PR checklist template
- Automate Docker push (if stable)

---