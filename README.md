# 🏢 Property Management REST API

**A secure, production-ready REST API demonstrating Agile and DevOps best practices**

[![CI Pipeline](https://img.shields.io/badge/CI-GitHub%20Actions-blue)](/.github/workflows/ci.yml)
[![Java](https://img.shields.io/badge/Java-21-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.10-green)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## 📖 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Technology Stack](#-technology-stack)
- [Quick Start](#-quick-start)
- [API Documentation](#-api-documentation)
- [Architecture](#-architecture)
- [Testing](#-testing)
- [CI/CD](#-cicd)
- [Project Structure](#-project-structure)
- [Sprint Documentation](#-sprint-documentation)
- [Contributing](#-contributing)

---

## 🎯 Overview

This project is a **secure property listing REST API** built to demonstrate:

✅ **JWT Authentication** with email/password login
✅ **Role-Based Access Control** (USER, AGENT, ADMIN)
✅ **Property Management** with ownership validation
✅ **CI/CD Pipeline** with GitHub Actions
✅ **Clean Architecture** with service layer abstraction
✅ **Test-Driven Development** with 80%+ coverage
✅ **Agile Methodology** with sprint planning and retrospectives

**Built with:**
- Java 21
- Spring Boot 3.5
- Spring Security + JWT
- H2 Database (in-memory)
- Docker
- GitHub Actions

---

## ✨ Features

### Authentication & Authorization
- 🔐 User registration with BCrypt password encryption
- 🎫 JWT-based authentication
- 🛡️ Role-based access control (USER, AGENT, ADMIN)
- 🚫 Protected endpoints with method-level security

### Property Management
- 🏠 Create property listings (AGENT, ADMIN only)
- ✏️ Update own properties (ownership validation)
- 👀 Public read access to all properties
- 📊 View personal property listings

### DevOps & Monitoring
- 🔄 CI/CD pipeline with automated testing
- 🐳 Dockerized application
- 💚 Health check endpoints
- 📝 Structured logging
- ⚡ Global exception handling

---

## 🛠️ Technology Stack

### Backend
- **Language:** Java 21
- **Framework:** Spring Boot 3.5.10
- **Security:** Spring Security + JWT (jjwt 0.12.3)
- **Database:** H2 (in-memory)
- **ORM:** Spring Data JPA / Hibernate
- **Validation:** Jakarta Bean Validation
- **Build Tool:** Maven 3.9

### DevOps
- **CI/CD:** GitHub Actions
- **Container:** Docker (multi-stage build)
- **Monitoring:** Spring Boot Actuator
- **Logging:** SLF4J + Logback

### Testing
- **Framework:** JUnit 5
- **Mocking:** Mockito
- **Integration:** Spring Test + MockMvc
- **Test Containers:** (Optional)

---

## 🚀 Quick Start

### Prerequisites
- Java 21+ installed
- Maven 3.9+ installed
- (Optional) Docker installed

### Running Locally

```bash
# Clone the repository
git clone <repository-url>
cd Agile-and-Devops-in-Practice/Property-Management-API

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run

# Application will start on http://localhost:8080
```

### Running with Docker

```bash
cd Property-Management-API

# Build Docker image
docker build -t property-api:latest .

# Run container
docker run -p 8080:8080 property-api:latest

# Access application at http://localhost:8080
```

### Running Tests

```bash
# Run all tests
mvn test

# Run tests with coverage
mvn test jacoco:report

# Run specific test class
mvn test -Dtest=UserServiceTest
```

---

## 📡 API Documentation

### Authentication Endpoints

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}

Response: 201 Created
{
  "message": "User registered successfully",
  "email": "user@example.com",
  "role": "USER"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}

Response: 200 OK
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "user@example.com",
  "role": "USER"
}
```

### Property Endpoints

#### Create Property (AGENT/ADMIN only)
```http
POST /api/properties
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "title": "Beautiful House",
  "description": "3 bedroom house in great location",
  "price": 250000.00
}

Response: 201 Created
{
  "id": 1,
  "title": "Beautiful House",
  "description": "3 bedroom house in great location",
  "price": 250000.00,
  "ownerEmail": "agent@example.com",
  "createdAt": "2026-02-17T12:00:00",
  "updatedAt": "2026-02-17T12:00:00"
}
```

#### Get All Properties (Public)
```http
GET /api/properties

Response: 200 OK
[
  {
    "id": 1,
    "title": "Beautiful House",
    "description": "3 bedroom house in great location",
    "price": 250000.00,
    "ownerEmail": "agent@example.com",
    "createdAt": "2026-02-17T12:00:00",
    "updatedAt": "2026-02-17T12:00:00"
  }
]
```

#### Update Property (Owner only)
```http
PUT /api/properties/{id}
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "title": "Updated Title",
  "description": "Updated description",
  "price": 260000.00
}

Response: 200 OK
{
  "id": 1,
  "title": "Updated Title",
  "description": "Updated description",
  "price": 260000.00,
  "ownerEmail": "agent@example.com",
  "createdAt": "2026-02-17T12:00:00",
  "updatedAt": "2026-02-17T13:00:00"
}
```

### Admin Endpoints

#### Get System Stats (ADMIN only)
```http
GET /api/admin/stats
Authorization: Bearer <jwt-token>

Response: 200 OK
{
  "message": "Admin statistics",
  "totalUsers": 100,
  "totalProperties": 50,
  "totalAgents": 10,
  "accessedBy": "admin@example.com"
}
```

### Health Check

```http
GET /actuator/health

Response: 200 OK
{
  "status": "UP"
}
```

### Complete API Summary

| Endpoint | Method | Auth | Role | Description |
|----------|--------|------|------|-------------|
| `/api/auth/register` | POST | No | - | Register new user |
| `/api/auth/login` | POST | No | - | Login and get JWT |
| `/api/test/secure` | GET | Yes | Any | Test secured endpoint |
| `/api/admin/stats` | GET | Yes | ADMIN | Admin statistics |
| `/api/agent/dashboard` | GET | Yes | AGENT, ADMIN | Agent dashboard |
| `/api/properties` | POST | Yes | AGENT, ADMIN | Create property |
| `/api/properties` | GET | No | - | List all properties |
| `/api/properties/{id}` | GET | No | - | Get property by ID |
| `/api/properties/{id}` | PUT | Yes | Owner | Update own property |
| `/api/properties/my-properties` | GET | Yes | AGENT, ADMIN | Get user's properties |
| `/actuator/health` | GET | No | - | Health check |

---

## 🏗️ Architecture

### Clean Architecture Layers

```
┌─────────────────────────────────┐
│   Controllers (REST API)        │  ← Presentation Layer
├─────────────────────────────────┤
│   Services (Business Logic)     │  ← Service Layer
├─────────────────────────────────┤
│   Repositories (Data Access)    │  ← Data Layer
├─────────────────────────────────┤
│   Models (Entities)              │  ← Domain Layer
└─────────────────────────────────┘
```

### Security Architecture

```
HTTP Request
    ↓
JwtAuthenticationFilter (validate JWT)
    ↓
Spring Security Filter Chain
    ↓
Controller (@PreAuthorize checks role)
    ↓
Service (business logic + ownership validation)
    ↓
Repository (data access)
```

### Key Design Patterns
- **Repository Pattern** - Data access abstraction
- **Service Layer Pattern** - Business logic separation
- **DTO Pattern** - Request/response data transfer
- **Filter Pattern** - JWT authentication
- **Builder Pattern** - Entity construction

---

## 🧪 Testing

### Test Structure

```
src/test/java/
├── controller/
│   └── AuthControllerIntegrationTest.java      (5 tests)
├── integration/
│   ├── AuthenticationIntegrationTest.java      (5 tests)
│   ├── RBACIntegrationTest.java                (6 tests)
│   └── PropertyManagementIntegrationTest.java  (8 tests)
├── security/
│   └── JwtTokenProviderTest.java               (6 tests)
└── service/
    └── UserServiceTest.java                    (4 tests)
```

### Test Coverage
- **Total Tests:** 41+
- **Unit Tests:** 10
- **Integration Tests:** 30+
- **Coverage:** 80%+

### Running Specific Tests

```bash
# Run unit tests only
mvn test -Dtest=**/*Test.java

# Run integration tests only
mvn test -Dtest=**/integration/**

# Run with coverage report
mvn test jacoco:report
# View report at: target/site/jacoco/index.html
```

---

## 🔄 CI/CD

### GitHub Actions Pipeline

The project includes a complete CI/CD pipeline that runs on:
- Push to `main` or `dev` branches
- Pull requests to `main` or `dev`

### Pipeline Steps:
1. ✅ Checkout code
2. ✅ Setup JDK 21
3. ✅ Build with Maven
4. ✅ Run tests
5. ✅ Package application
6. ✅ Build Docker image
7. ✅ Upload test results

### Pipeline File
See [`.github/workflows/ci.yml`](.github/workflows/ci.yml)

### Docker

**Build:**
```bash
docker build -t property-api:latest .
```

**Run:**
```bash
docker run -p 8080:8080 property-api:latest
```

**Multi-stage Dockerfile** for optimized image size:
- Build stage: Maven build
- Runtime stage: JRE-only Alpine image

---

## 📂 Project Structure

```
Agile-and-Devops-in-Practice/
├── .github/
│   ├── workflows/
│   │   └── ci.yml                              # CI/CD pipeline
│   └── pull_request_template.md                # PR checklist
├── docs/
│   ├── sprint-1-review.md                      # Sprint 1 review
│   ├── sprint-1-retrospective.md               # Sprint 1 retrospective
│   └── project-summary.md                      # Final summary
├── Property-Management-API/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/org/amalitech/propertymanagementapi/
│   │   │   │   ├── config/                     # Security configuration
│   │   │   │   ├── controller/                 # REST controllers
│   │   │   │   ├── dto/                        # Data transfer objects
│   │   │   │   ├── exception/                  # Custom exceptions
│   │   │   │   ├── model/                      # JPA entities
│   │   │   │   ├── repository/                 # Data repositories
│   │   │   │   ├── security/                   # JWT & security
│   │   │   │   └── service/                    # Business logic
│   │   │   └── resources/
│   │   │       └── application.yml             # Configuration
│   │   └── test/
│   │       └── java/...                        # Test classes
│   ├── Dockerfile                              # Container configuration
│   ├── DOCKER.md                               # Docker instructions
│   ├── pom.xml                                 # Maven dependencies
│   └── HELP.md                                 # Spring Boot help
├── 0-Sprint-Plan.md                            # Product backlog
├── 1-Sprint-1-Plan.md                          # Sprint 1 plan
├── 2-Sprint-2-Plan.md                          # Sprint 2 plan
└── README.md                                   # This file
```

---

## 📚 Sprint Documentation

This project was built following Agile methodology with 2 sprints:

### Sprint 0 - Planning
- [Product Backlog](0-Sprint-Plan.md)
- User stories with acceptance criteria
- Story point estimation
- Definition of Done

### Sprint 1 - Authentication & CI/CD (1 week)
- [Sprint 1 Plan](1-Sprint-1-Plan.md)
- [Sprint 1 Review](docs/sprint-1-review.md)
- [Sprint 1 Retrospective](docs/sprint-1-retrospective.md)
- **Delivered:** User Registration, JWT Authentication, CI Pipeline
- **Velocity:** 18 Story Points

### Sprint 2 - RBAC & Properties (1 week)
- [Sprint 2 Plan](2-Sprint-2-Plan.md)
- **Delivered:** Role-Based Access Control, Property Management, Monitoring
- **Velocity:** 13 Story Points

### Final Summary
- [Project Summary](docs/project-summary.md) - Complete project overview

---

## 🌟 Key Achievements

### Technical Excellence
✅ **JWT Authentication** with secure token generation
✅ **RBAC Implementation** with method-level security
✅ **Ownership Validation** for resource access control
✅ **Clean Architecture** with clear separation of concerns
✅ **Comprehensive Testing** with unit and integration tests
✅ **Docker Support** with multi-stage builds
✅ **CI/CD Pipeline** with automated testing

### Agile & DevOps
✅ **Sprint Planning** with story points and DoD
✅ **Feature Branching** with Git workflow
✅ **Incremental Development** with small commits
✅ **Sprint Reviews** with demo and metrics
✅ **Sprint Retrospectives** with actionable improvements
✅ **Continuous Integration** with GitHub Actions
✅ **Infrastructure as Code** with Dockerfile

---

## 🤝 Contributing

### Development Workflow

1. **Create feature branch**
   ```bash
   git checkout dev
   git checkout -b feature/your-feature-name
   ```

2. **Make changes with conventional commits**
   ```bash
   git commit -m "feat: add new feature"
   git commit -m "fix: resolve bug"
   git commit -m "test: add test cases"
   ```

3. **Push and create PR**
   ```bash
   git push origin feature/your-feature-name
   ```

4. **Use PR template** - Fill out the checklist

5. **Merge to dev** after review

### Commit Message Convention

```
feat: new feature
fix: bug fix
test: add/update tests
docs: documentation
refactor: code refactoring
chore: maintenance
ci: CI/CD changes
```

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 📧 Contact

For questions or feedback:
- Create an issue in the repository
- Email: your-email@example.com

---

## 🙏 Acknowledgments

Built following:
- Spring Security best practices
- Clean Code principles (Robert C. Martin)
- Agile Manifesto
- The Twelve-Factor App methodology
- DevOps best practices

---

**⭐ If you find this project helpful, please consider giving it a star!**

**Built with ❤️ demonstrating Agile and DevOps excellence**
**February 2026**
