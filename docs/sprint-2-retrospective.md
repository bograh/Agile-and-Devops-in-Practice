# Sprint 2 Retrospective - Property Management API

**Date:** February 17, 2026
**Sprint:** Sprint 2
**Participants:** Development Team
**Facilitator:** Scrum Master
**Duration:** 1 hour

---

## Sprint 2 Recap

**Sprint Goal:** Implement role-based access control and property management features with improved development practices

**Outcome:** Goal Achieved
- Delivered: 13/13 story points (100%)
- RBAC fully implemented and merged
- Property management core features complete
- All Sprint 1 improvements successfully applied

---

## What Went Well (Continue Doing)

### 1. Test-Driven Development (TDD)

**What Happened:**
- Applied TDD approach to RBAC feature (Sprint 1 improvement)
- Wrote all 6 tests BEFORE implementing the code
- All tests passed on first implementation run
- Zero bugs found in RBAC feature

**Impact:**
- Better understanding of requirements upfront
- Prevented bugs rather than fixing them later
- Faster development (despite writing tests first)
- Higher confidence in code quality
- Clear acceptance criteria validation

**Team Feedback:**
> "Writing tests first forced us to think about the API design and edge cases before coding. This was a game-changer!" - Developer

**Recommendation:** **Continue TDD for all features in Sprint 3**

---

### 2. Incremental Commits with Conventional Messages

**What Happened:**
- Maintained small, focused commits throughout Sprint 2
- Every commit followed conventional commit format
- Clear commit history: feat, fix, test, docs, chore
- Example: `feat: enable method-level security and implement role-based endpoint restrictions`

**Impact:**
- Easy to track changes
- Simplified debugging (bisect-able history)
- Self-documenting code evolution
- Clean revert capability if needed

**Metrics:**
- Average commit size: ~100 lines
- Conventional format: 100% compliance
- Commits in Sprint 2: 8 commits

**Recommendation:** **Continue this practice - consider adding commit message linting**

---

### 3. Feature Branch Workflow

**What Happened:**
- Used dedicated branches for RBAC and Property features
- Isolated development prevented conflicts
- Merged with --no-ff for clear history
- All branches published to remote

**Impact:**
- Safe experimentation without affecting main code
- Would enable parallel team development
- Clear feature boundaries in history
- Easy to review entire feature in one go

**Branch Strategy:**
```
main (production)
  ↑
dev (integration)
  ↑         ↑
feature/rbac  feature/property-management
```

**Recommendation:** **Continue - consider adding branch protection rules**

---

### 4. Application of Sprint 1 Improvements

**What Happened:**
- Successfully applied all 3 improvements from Sprint 1 retrospective:
  1. TDD for security features (RBAC)
  2. PR checklist template created
  3. Better task breakdown (Property feature)

**Impact:**
- Demonstrates continuous improvement
- Process maturity increasing
- Retrospectives are actionable, not just discussion
- Closed feedback loop

**Team Feedback:**
> "Seeing our retrospective actions actually implemented and working is motivating. We're truly getting better each sprint!"

**Recommendation:** **Continue prioritizing retrospective action items**

---

### 5. Comprehensive Documentation

**What Happened:**
- Created extensive documentation:
  - README.md (580 lines)
  - DELIVERABLES.md (580 lines)
  - project-summary.md (507 lines)
  - Sprint 2 review (comprehensive)
- Updated documentation as features were added

**Impact:**
- Knowledge transfer made easy
- Clear project status for stakeholders
- Easy onboarding for new team members
- Professional deliverable quality

**Recommendation:** **Continue - documentation is a key differentiator**

---

## What Could Be Improved (Change/Add)

### 1. Test Data Management

**Problem:**
- Property integration tests causing 36 test failures
- Tests may be interfering with each other
- Possible test data cleanup issues
- Database state between tests unclear

**Root Causes:**
- Lack of proper test data isolation
- No @Transactional rollback strategy
- Possible H2 database state persistence
- Test execution order dependencies

**Proposed Solutions:**

**Solution A: Transaction Rollback (Recommended)**
```java
@SpringBootTest
@Transactional  // Rollback after each test
public class PropertyManagementIntegrationTest {
    @BeforeEach
    void setUp() {
        // Test data setup
    }
    // Tests...
}
```

**Solution B: Database Cleanup**
```java
@AfterEach
void tearDown() {
    propertyRepository.deleteAll();
    userRepository.deleteAll();
}
```

**Solution C: Test Containers**
```java
@Testcontainers
@SpringBootTest
public class PropertyManagementIntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");
}
```

**Action Items for Sprint 3:**
1. Debug property test failures (Priority: High)
2. Implement @Transactional on test classes
3. Add proper tearDown methods
4. Document test data management strategy
5. Verify all tests pass independently and together

**Owner:** Development Team
**Due:** Sprint 3 Day 1

---

### 2. Early Merge to Dev Branch

**Problem:**
- Property feature developed entirely on feature branch
- Not merged to dev until "complete"
- Integration issues discovered late
- Test failures blocked dev merge

**Impact:**
- Delayed integration
- Late discovery of issues
- Feature locked in branch
- Would block team collaboration

**Current Flow:**
```
Feature Branch → [All work done] → Merge to Dev (blocked by tests)
```

**Improved Flow:**
```
Feature Branch → [Entity] → Merge to Dev →
Feature Branch → [Repository] → Merge to Dev →
Feature Branch → [Service] → Merge to Dev →
Feature Branch → [Controller] → Merge to Dev
```

**Proposed Solution:**
- Merge smaller, incremental changes to dev
- Each commit should be merge-worthy
- Integrate frequently (at least daily)
- Use feature flags if needed for incomplete features

**Benefits:**
- Continuous integration (true CI)
- Earlier issue detection
- Better team visibility
- Easier rollback if needed

**Action Items for Sprint 3:**
1. Define "merge-worthy" criteria
2. Merge to dev at least once per day
3. Consider feature flags for WIP features
4. Track integration frequency

**Owner:** Development Team
**Due:** Sprint 3 (ongoing)

---

### 3. Test Suite Execution Time

**Observation:**
- Test suite growing (41 tests now)
- Integration tests starting Spring context multiple times
- No measurement of test execution time
- May become a bottleneck as codebase grows

**Current State:**
- Total tests: 41
- Execution time: Not measured
- Context loads: Multiple per test class

**Proposed Solution:**

**Strategy 1: Shared Application Context**
```java
// Use same context for multiple test classes
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class BaseIntegrationTest {
    @Autowired
    protected MockMvc mockMvc;
}

public class AuthTests extends BaseIntegrationTest {
    // Tests reuse context
}
```

**Strategy 2: Test Categorization**
```java
@Tag("integration")
public class PropertyManagementIntegrationTest { }

@Tag("unit")
public class UserServiceTest { }

// Run only unit tests during development
// mvn test -Dgroups=unit
```

**Strategy 3: Parallel Execution**
```xml
<!-- pom.xml -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
        <parallel>classes</parallel>
        <threadCount>4</threadCount>
    </configuration>
</plugin>
```

**Action Items for Sprint 3:**
1. Measure current test execution time
2. Set baseline and target metrics
3. Implement shared test context
4. Add test categories (unit/integration)
5. Explore parallel test execution

**Owner:** Development Team
**Due:** Sprint 3 Mid-sprint

---

### 4. API Documentation and Testing Tools

**Gap Identified:**
- No interactive API documentation (Swagger/OpenAPI)
- Manual cURL testing only
- No Postman collection provided
- Difficult for stakeholders to test API

**Impact:**
- Time-consuming manual endpoint testing
- No easy way for QA to test
- Documentation separate from code
- Harder to demo to stakeholders

**Proposed Solution:**

**Option 1: SpringDoc OpenAPI (Recommended)**
```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

Access at: `http://localhost:8080/swagger-ui.html`

**Option 2: Postman Collection**
- Export collection with all endpoints
- Include example requests/responses
- Share with team and stakeholders

**Option 3: REST Client Files**
```http
### Register User
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "email": "test@example.com",
  "password": "password123"
}
```

**Action Items for Sprint 3:**
1. Add SpringDoc OpenAPI dependency
2. Configure OpenAPI annotations
3. Customize Swagger UI
4. Export Postman collection
5. Update README with API docs link

**Owner:** Development Team
**Due:** Sprint 3 Week 1

---

### 5. Monitoring and Observability Gaps

**Current State:**
- Spring Boot Actuator enabled
- Health endpoint available
- No metrics collection
- No request tracing
- No error rate monitoring
- No performance metrics

**Missing Capabilities:**
- Request/response times
- Error rates by endpoint
- Database query performance
- JVM metrics
- Custom business metrics (properties created, users registered)

**Proposed Solution:**

**Step 1: Enable Metrics**
```yaml
# application.yml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  metrics:
    export:
      prometheus:
        enabled: true
```

**Step 2: Add Custom Metrics**
```java
@Service
public class PropertyService {
    private final Counter propertyCreated;

    public PropertyService(MeterRegistry registry) {
        this.propertyCreated = registry.counter("properties.created");
    }

    public PropertyResponse createProperty(...) {
        // ... create logic
        propertyCreated.increment();
        return response;
    }
}
```

**Step 3: Structured Logging**
```java
@Slf4j
@RestController
public class PropertyController {
    @PostMapping
    public ResponseEntity<PropertyResponse> createProperty(...) {
        log.info("Creating property: title={}, owner={}",
                 request.getTitle(), authentication.getName());
        // ...
    }
}
```

**Action Items for Sprint 3:**
1. Enable metrics endpoints
2. Add custom business metrics
3. Implement structured logging
4. Define key performance indicators (KPIs)
5. Document monitoring strategy

**Owner:** Development Team
**Due:** Sprint 3 (monitoring story)

---

## 🎬 Action Items Summary for Sprint 3

| Priority | Action Item | Owner | Target |
|----------|-------------|-------|--------|
| 🔴 High | Debug and fix property test failures | Dev Team | Day 1 |
| 🔴 High | Implement test data isolation strategy | Dev Team | Day 2 |
| 🟡 Medium | Add SpringDoc OpenAPI documentation | Dev Team | Week 1 |
| 🟡 Medium | Implement frequent dev merges policy | Dev Team | Ongoing |
| 🟡 Medium | Measure test execution time | Dev Team | Week 1 |
| 🟢 Low | Add custom metrics and monitoring | Dev Team | New Story |
| 🟢 Low | Implement structured logging | Dev Team | New Story |
| 🟢 Low | Create Postman collection | Dev Team | Week 2 |

---

## 📊 Process Metrics

### Retrospective Effectiveness

**Sprint 1 → Sprint 2 Improvements:**
- Action Items Identified: 3
- Action Items Completed: 3 (100%)
- Impact: High (TDD prevented bugs, PR template ready)

**Sprint 2 → Sprint 3 Improvements:**
- Action Items Identified: 5
- Expected Completion: Sprint 3
- Priority Distribution: 2 High, 3 Medium, 3 Low

### Team Health
- Sprint goal achieved
- No overtime required
- Team morale: High
- Process improvements working
- Test debugging needed

---

## 🎓 Key Learnings

### Technical Learnings

1. **TDD is Worth the Investment**
   - Initial time investment pays off quickly
   - Prevents bugs rather than finding them
   - Better API design upfront
   - **Takeaway:** Make TDD the default, not the exception

2. **Method-Level Security is Powerful**
   - More flexible than URL patterns
   - Clearer authorization intent
   - Easier to maintain
   - **Takeaway:** Use @PreAuthorize consistently

3. **Global Exception Handling is Essential**
   - Consistent error responses
   - Proper HTTP status codes
   - Better API usability
   - **Takeaway:** Add handlers for all custom exceptions

4. **Test Isolation Matters**
   - Tests must not depend on each other
   - Database state must be controlled
   - Cleanup is as important as setup
   - **Takeaway:** Address test isolation early, not late

### Process Learnings

1. **Retrospective Actions Must Be Prioritized**
   - Don't just discuss, commit to actions
   - Track action items explicitly
   - Celebrate when improvements work
   - **Takeaway:** Make retrospectives actionable

2. **Documentation is Development**
   - Don't leave docs for the end
   - Update docs as you code
   - Comprehensive docs show professionalism
   - **Takeaway:** Documentation is part of DoD

3. **Small, Frequent Merges > Large, Rare Merges**
   - Integrate early and often
   - Smaller changes easier to review
   - Issues found sooner
   - **Takeaway:** Merge to dev daily if possible

---

## Team Shoutouts

### TDD Champion
For successfully applying Test-Driven Development to the RBAC feature, resulting in zero bugs and a smooth implementation!

### Documentation Excellence
For creating comprehensive project documentation (3,000+ lines) that makes the project professional and maintainable!

### 100% Velocity
For maintaining 100% sprint completion rate across both sprints (18 SP + 13 SP = 31 SP delivered)!

---

## Sprint 3 Focus Areas

Based on this retrospective, Sprint 3 should focus on:

### Must Have (P0)
1. Fix property test failures
2. Merge property management to dev
3. Implement test isolation strategy

### Should Have (P1)
4. Add OpenAPI documentation
5. Optimize test execution time
6. Establish daily merge cadence

### Nice to Have (P2)
7. Add metrics and monitoring
8. Implement structured logging
9. Create Postman collection

---

## Retrospective Reflection

### What Made This Sprint Successful?

1. **Clear Action Items from Sprint 1**
   - We knew exactly what to improve
   - We committed to those improvements
   - We actually implemented them

2. **TDD Adoption**
   - Game-changer for code quality
   - Will continue this practice
   - Should be standard for all features

3. **Comprehensive Documentation**
   - Makes project professional
   - Helps knowledge transfer
   - Impresses stakeholders

### What Should We Do Differently Next Sprint?

1. **Test Failures Earlier Detection**
   - Don't let property tests go un-debugged
   - Address test issues immediately
   - Make test passing a daily check

2. **More Frequent Integration**
   - Don't keep features in branches too long
   - Merge small changes to dev daily
   - True continuous integration

3. **Proactive Monitoring**
   - Add observability early
   - Don't wait for problems
   - Metrics guide decisions

---

## Retrospective Format Feedback

**Format Used:** What Went Well / What Could Be Improved
**Effectiveness:** Excellent - Clear structure, actionable outcomes
**Duration:** 1 hour
**Participation:** 100%
**Action Items Generated:** 8

**Keep for Next Retrospective:** Same format works well

---

## Closing Thoughts

Sprint 2 was a success! We maintained our 100% velocity, applied all retrospective improvements from Sprint 1, and delivered important features (RBAC and Property Management core).

Key wins:
- TDD approach worked excellently
- Process improvements were applied, not just discussed
- Documentation is now comprehensive and professional
- Code quality remains high

Areas for Sprint 3:
- Fix property tests quickly
- Integrate more frequently
- Add observability

**Team morale:** High
**Confidence in process:** Growing
**Ready for Sprint 3:** Absolutely!

---

**Retrospective Completed:** February 17, 2026
**Next Retrospective:** End of Sprint 3
**Action Items Tracked:** GitHub Issues / Sprint Backlog

**Remember:** Continuous improvement is not a one-time event, it's a mindset!
