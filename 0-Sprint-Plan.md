# Sprint 0 - Planning & Setup

## Product Vision
Build a secure property listing REST API that demonstrates modern authentication (JWT & OAuth2), role-based access control, and automated CI/CD deployment using Agile and DevOps best practices.

## Product Backlog

### Story 1 - User Registration

**User Story (US1):**

**As a** new user,
**I want to** create an account
**So that** I can store my notes securely.

**Acceptance Criteria:**

- User can submit email + password.
- Password is encrypted using BCrypt.
- Duplicate email registration is prevented
- API returns appropriate HTTP status codes.
- User stored in database with default role `USER`.

**Priority:** High

**Estimate:** 5 Story Points

---
### Story 2 - JWT Authentication

**User Story (US2):**

**As a** registered user,
**I want to** log in and receive a JWT token
**So that** I can access protected endpoints.

**Acceptance Criteria:**

- User can log in with valid credentials.
- Invalid credentials return 401 Unauthorized.
- JWT is generated upon successful login.
- Token cantains user ID and role claims.
- Protected endpoints require valid JWT.

**Priority:** High

**Estimate:** 8 Story Points

---
### Story 3 - Role-Based Access Control (RBAC)

**User Story (US3):**

**As an** admin,
**I want** access to admin-only endpoints
**So that** system operations are restricted based on roles.

**Acceptance Criteria:**

- Roles supported: `USER`, `AGENT`, `ADMIN`
- Admin endpoints accessible only by `ADMIN`
- Agent endpoints accessible by `AGENT` and `ADMIN`
- Unauthorized access returns 403 Forbidden.
- Role validation enforced via Spring Security Configuration.

**Priority:** High

**Estimate:** 5 Story Points

---
### Story 4 - Property Management

**User Story (US4):**

**As an** agent,
**I want to** create and update property listings
**So that** properties can be manages within the system

**Acceptance Criteria:**

- Agent can create a property listin.
- Agenet can edit their own listing.
- Users can view listings (read-only)
- Input validation is enforced.
- Proper HTTP status codes are returned.

**Priority:** Medium

**Estimate:** 8 Story Points

---
### Story 5 - CI/CD Pipeline

**User Story (US4):**

**As a** developer,
**I want** automated builds and tests
**So that** code quality is maintained

**Acceptance Criteria:**

- Github Actions pipeline runs on push.
- Application builds successfully.
- Unit tests execute automatically.
- Pipeline fails on test failure.
- Docker image is built successfully.

**Priority:** High

**Estimate:** 5 Story Points

## Prioritized Backlog (Ordered)


| Priority Order | Story                     | Story Points |
| -------------- | ------------------------- | ------------ |
| 1              | US2 – JWT Authentication  | 8            |
| 2              | US1 – Registration        | 5            |
| 3              | US3 – RBAC                | 5            |
| 4              | US6 – CI/CD               | 5            |
| 5              | US4 – Property Management | 8            |

Total Estimated Effort: **31 Story Points**

## Definition of Done (DoD)

A backlog item is considered complete when:

- Code is implemented following clean architecture principles
- Feature is pushed via feature branch
- Pull request created and merged into main
- Unit tests written and passing
- CI pipeline passes successfully
- API endpoints documented
- README updated if applicable

## Team Capacity & Velocity

1 Developer (Individual Project)

The developer is responsible for:

- Planning & backlog refinement
- Development & testing
- CI/CD setup
- Documentation
- Deployment

## Estimated Velocity

Based on available time and task complexity:
- Estimated Velocity: 10–12 Story Points per sprint
- This reflects sustainable individual productivity over a 1-week sprint.
