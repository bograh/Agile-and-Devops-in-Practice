# Sprint 1 Review

**Sprint Duration:** 1 Week
**Sprint Goal:** Deliver secure authentication (Registration + JWT Login) and establish automated CI pipeline with tests
**Date:** February 17, 2026

---

## Delivered Features

### US1 – User Registration (5 Story Points)
**Status:** Completed

**Delivered:**
- Email + password registration endpoint (`POST /api/auth/register`)
- Password encryption using BCrypt
- Duplicate email validation with proper error handling
- Default role assignment (USER)
- Proper HTTP status codes (201 Created, 409 Conflict, 400 Bad Request)
- Unit tests for UserService (4 test cases)
- Integration tests for registration endpoint (5 test cases)

**Acceptance Criteria Met:**
- User can submit email + password
- Password is encrypted using BCrypt
- Duplicate email registration is prevented
- API returns appropriate HTTP status codes
- User stored in database with default role `USER`

---

### US2 – JWT Authentication (8 Story Points)
**Status:** Completed

**Delivered:**
- Login endpoint (`POST /api/auth/login`)
- Credential validation with proper authentication
- JWT token generation with claims (sub, role, iat, exp)
- JWT authentication filter added to security chain
- Protected test endpoint (`GET /api/test/secure`)
- Unit tests for JwtTokenProvider (6 test cases)
- Integration tests for authentication flow (5 test cases)

**Acceptance Criteria Met:**
- User can log in with valid credentials
- Invalid credentials return 401 Unauthorized
- JWT is generated upon successful login
- Token contains user ID and role claims
- Protected endpoints require valid JWT

---

### CI/CD Pipeline (5 Story Points)
**Status:** Completed

**Delivered:**
- GitHub Actions workflow (`.github/workflows/ci.yml`)
- Automated build on push to main/dev branches
- Automated test execution
- Docker image build configuration
- Dockerfile with multi-stage build
- Test result artifact upload

**Acceptance Criteria Met:**
- GitHub Actions pipeline runs on push
- Application builds successfully
- Unit tests execute automatically
- Pipeline fails on test failure
- Docker image is built successfully

---

## 📊 Sprint Metrics

**Planned Story Points:** 13 (US1: 5 + US2: 8)
**Delivered Story Points:** 18 (US1: 5 + US2: 8 + CI: 5)
**Velocity:** 18 SP (exceeded planned capacity)

**Test Coverage:**
- Total Tests: 26
- Passing: 26 (100%)
- Failing: 0
- Test Types: Unit (10), Integration (15), Application (1)

**Code Quality:**
- Clean architecture implemented
- Service layer abstraction
- DTOs for request/response
- Global exception handling
- Proper logging with SLF4J

---

## Technical Deliverables

### API Endpoints

| Endpoint              | Method | Description                   | Auth Required | Status Code |
|-----------------------|--------|-------------------------------|---------------|-------------|
| /api/auth/register    | POST   | Register new user             | No            | 201         |
| /api/auth/login       | POST   | Authenticate and get JWT      | No            | 200         |
| /api/test/secure      | GET    | Test secured endpoint         | Yes (JWT)     | 200         |
| /actuator/health      | GET    | Health check                  | No            | 200         |
| /h2-console           | GET    | H2 database console           | No            | 200         |

### Architecture Components

**Models:**
- User entity with UserDetails implementation
- Role enum (USER, AGENT, ADMIN)

**Security:**
- JwtTokenProvider for token operations
- JwtAuthenticationFilter for request authentication
- CustomUserDetailsService for user loading
- SecurityConfig with Spring Security configuration

**Services:**
- UserService for registration
- AuthService for authentication

**Exception Handling:**
- DuplicateEmailException
- BadCredentialsException handling
- Validation error handling
- Global exception handler

---

## Git Workflow

**Branching Strategy:**
```
main
 └── dev
      ├── feature/user-registration (merged)
      ├── feature/jwt-authentication (merged)
      └── feature/ci-pipeline (merged)
```

**Commit History Examples:**
```
chore: configure dependencies (security, jwt, actuator, validation)
feat: create User entity and Role enum
feat: configure BCrypt password encoder
feat: implement registration controller
test: add unit and integration tests for user registration
feat: create JwtTokenProvider for token generation and validation
feat: implement login endpoint and JWT authentication filter
test: add JWT provider and authentication integration tests
fix: update test expectations for authentication status codes
ci: add GitHub Actions workflow and Dockerfile
```

---

## Definition of Done Verification

- Code is implemented following clean architecture principles
- Features pushed via feature branches
- Pull requests merged into dev
- Unit tests written and passing
- Integration tests written and passing
- CI pipeline passes successfully
- Code committed with proper conventional commit messages

---

## Test Evidence

### Test Execution Summary
```
Tests run: 26, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

### Sample Test Cases

**UserServiceTest:**
- Should successfully register a new user
- Should throw DuplicateEmailException when email already exists
- Should encode password before saving
- Should assign USER role by default

**JwtTokenProviderTest:**
- Should generate valid JWT token
- Should extract username from token
- Should extract role from token
- Should validate token successfully
- Should invalidate token with wrong user

**AuthenticationIntegrationTest:**
- Should login successfully with valid credentials and return JWT
- Should return 401 for invalid credentials
- Should access secured endpoint with valid JWT token
- Should return 403 when accessing secured endpoint without token

---

## Docker Build

Successfully created Dockerfile with:
- Multi-stage build for optimization
- Maven dependency caching
- Alpine-based runtime image
- Health check configuration
- Port 8080 exposed

---

## Lessons Learned

### What Went Well
1. **Clear story breakdown** - Breaking work into user stories helped maintain focus
2. **Test-first approach** - Writing tests early caught issues before integration
3. **Incremental commits** - Small, frequent commits made it easy to track progress
4. **Clean architecture** - Separation of concerns improved code maintainability

### Challenges Encountered
1. **JWT filter order** - Took time to properly configure filter chain order
2. **Spring Security defaults** - Understanding difference between 401 and 403 responses
3. **Test configuration** - Setting up proper test properties for JWT tests

---

## Sprint Outcome

**Sprint Objective:** ACHIEVED

All planned features were delivered with high quality:
- Secure user registration
- JWT-based authentication
- Comprehensive test coverage
- Automated CI/CD pipeline
- Production-ready Docker configuration

The sprint exceeded the planned velocity by completing additional CI/CD infrastructure work ahead of schedule.

---

## Next Sprint Preview

**Sprint 2 Goals:**
- Implement Role-Based Access Control (US3)
- Implement Property Management (US4)
- Add monitoring and observability
- Apply Sprint 1 retrospective improvements

**Estimated Capacity:** 13 Story Points
