Excellent.
Sprint 2 is where your project starts looking **production-grade**, not just “it works”.

Based on your backlog and Sprint 1 delivery (US1 + US2), Sprint 2 should deliver:

* **US3 – RBAC (5 SP)**
* **US4 – Property Management (8 SP)**

That’s 13 SP — aligned with your established velocity.

---

# Sprint 2 – Execution & Improvement Plan

## Sprint Goal

Deliver role-based authorization and property management features while improving engineering discipline and observability.

---

# Apply Sprint 1 Retrospective Improvements

From Sprint 1 you identified:

> 1. Write security configuration tests earlier
> 2. Break stories into smaller technical tasks before coding
> 3. Add PR checklist template
> 4. Improve Docker pipeline (optional)

## What You Do in Sprint 2

### Improvement #1 – Security Tests First (TDD-ish approach)

Before implementing RBAC:

* Write integration tests for:

  * `/admin/**` → 403 if USER
  * `/agent/**` → 403 if USER
  * `/agent/**` → 200 if AGENT
  * `/admin/**` → 200 if ADMIN

This prevents filter-order mistakes early.

---

### Improvement #2 – Smaller Technical Tasks

Break US3 into subtasks:

* Create Role enum expansion
* Seed ADMIN and AGENT roles
* Implement method-level security
* Write RBAC integration tests
* Refactor security config

Break US4 into:

* Property entity
* Property repository
* Create property endpoint
* Update property endpoint
* Ownership validation
* Read-only public endpoint
* Input validation
* Tests

---

### Improvement #3 – PR Checklist

Create:

```
.github/pull_request_template.md
```

```md
## Checklist

- [ ] Feature works locally
- [ ] Tests added/updated
- [ ] CI pipeline passes
- [ ] No hardcoded secrets
- [ ] Security reviewed
- [ ] README updated (if needed)
```

---

# Deliver Work (Sprint 2 Features)

---

# US3 – Role-Based Access Control (RBAC)

## Implementation Strategy

### Enable Method Security

```java
@EnableMethodSecurity
```

---

### Role Structure

```
USER
AGENT
ADMIN
```

---

### Secure Endpoints

```java
@PreAuthorize("hasRole('ADMIN')")
@GetMapping("/admin/stats")
```

```java
@PreAuthorize("hasAnyRole('AGENT','ADMIN')")
@PostMapping("/agent/property")
```

---

### Integration Tests

Test cases:

| Scenario                | Expected |
| ----------------------- | -------- |
| USER → /admin           | 403      |
| AGENT → /admin          | 403      |
| ADMIN → /admin          | 200      |
| USER → create property  | 403      |
| AGENT → create property | 201      |

---

# US4 – Property Management

## Domain Model

```java
Property
- id
- title
- description
- price
- owner (User)
- createdAt
```

---

## Endpoints

| Method | Endpoint             | Role               |
| ------ | -------------------- | ------------------ |
| POST   | /api/properties      | AGENT              |
| PUT    | /api/properties/{id} | AGENT (owner only) |
| GET    | /api/properties      | Public             |
| GET    | /api/properties/{id} | Public             |

---

## Ownership Validation

In service layer:

```java
if (!property.getOwner().getId().equals(currentUserId)) {
    throw new AccessDeniedException("Not your listing");
}
```

---

## Validation

Use:

```
@NotBlank
@Positive
```

And:

```
@Valid
```

---

# Add Monitoring & Observability

You don’t need enterprise observability yet — but you must show DevOps awareness.

---

## A. Structured Logging

Add:

```properties
logging.level.org.springframework.security=DEBUG
logging.level.com.yourpackage=INFO
```

Use SLF4J properly:

```java
log.info("User {} created property {}", userId, propertyId);
log.warn("Unauthorized property edit attempt by {}", userId);
```

---

## B. Global Exception Handler

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
```

Standardized error response:

```json
{
  "timestamp": "",
  "status": 403,
  "error": "Forbidden",
  "message": "Access denied"
}
```

---

## C. Health Endpoint

Add Spring Boot Actuator:

Dependency:

```
spring-boot-starter-actuator
```

Expose:

```properties
management.endpoints.web.exposure.include=health,info
```

Test:

```
GET /actuator/health
```

Response:

```json
{
  "status": "UP"
}
```

This makes your API look production-ready.

---

# Git Commit Strategy (Sprint 2)

## Branches

```
feature/rbac
feature/property-management
feature/monitoring
```

---

## Example Commit History

### RBAC

```
feat: enable method-level security
feat: implement role-based endpoint restrictions
test: add rbac integration tests
fix: correct role prefix mismatch (ROLE_)
```

---

### Property

```
feat: create Property entity
feat: implement property repository
feat: add property service layer
feat: implement create property endpoint
feat: enforce ownership validation
test: add property integration tests
```

---

### Monitoring

```
feat: add actuator dependency
feat: implement global exception handler
chore: configure logging levels
```

---

# Sprint 2 Review Document

Create:

```
/docs/sprint-2-review.md
```

## Include:

### Delivered Features

* Role-Based Access Control
* Property Management
* Ownership enforcement
* Structured logging
* Health endpoint
* Improved PR workflow

---

### Demo Evidence

Screenshots of:

* 403 when USER hits /admin
* 201 when AGENT creates property
* 403 when AGENT edits someone else’s property
* /actuator/health response
* Successful CI run

---

### Updated API Summary

Add property endpoints table.

---