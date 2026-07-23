# RetireWise — Architecture

## 14. Technical Architecture

### 14.1 Mobile technology

1. Kotlin Multiplatform
2. Compose Multiplatform
3. Kotlin Coroutines
4. Kotlin Serialization
5. Ktor client
6. SQLDelight for structured local data
7. Multiplatform Settings for simple preferences
8. Koin for dependency injection
9. Voyager (or an approved Compose Multiplatform navigation solution)
10. Kotlin test for shared unit tests

Use native implementations through `expect`/`actual` declarations where required.

> Phase 0 note: Ktor, SQLDelight, and Multiplatform Settings are declared in the version catalogue but only added to a module's dependencies when that module first needs them (e.g. SQLDelight lands with the profile/persistence work, Ktor lands with the backend API client). Koin and Voyager are wired in Phase 0 because dependency injection and navigation are explicit Phase 0 exit criteria.

### 14.2 Backend technology

Recommended initial backend:

1. Kotlin with Ktor server
2. PostgreSQL
3. Redis for short lived caching and rate limiting
4. Object storage for reports and approved documents
5. Background job service
6. Managed secrets service
7. Container based deployment in a Canadian region where available

Firebase may be used for authentication, notifications, crash reporting, and selected application services.

Do not store the authoritative financial rules only in a client database. Do not call AI providers directly from the mobile client.

> Phase 0 note: no backend service exists yet. It is introduced starting Phase 3 (authentication) and Phase 8 (AI advisor) per `RELEASE_PLAN.md`.

### 14.3 High level architecture

```
iOS Application
Android Application
        |
Shared Compose Multiplatform Interface
        |
Shared Presentation and Domain Modules
        |
Secure Backend API
        |
AI Orchestration Service
Calculation Service
Government Policy Service
User Profile Service
Scenario Service
Report Service
Consent Service
Audit Service
        |
PostgreSQL
Object Storage
Cache
Model Providers
Verified Government Sources
```

### 14.4 Repository structure

```
RetireWise

apps
  androidApp
  iosApp

shared
  core
  design_system
  navigation
  authentication
  onboarding
  dashboard
  profile
  retirement_engine
  tax_engine
  benefits_engine
  scenario_engine
  ai_advisor
  government_programs
  learning
  action_plan
  reports
  analytics
  security
  localization
  testing

backend
  api
  authentication
  user_service
  planning_service
  calculation_service
  ai_orchestrator
  policy_service
  content_service
  report_service
  notification_service
  audit_service
  database
  migrations
  jobs

docs
  PRD.md
  ARCHITECTURE.md
  AI_ARCHITECTURE.md
  DESIGN_SYSTEM.md
  SECURITY.md
  PRIVACY.md
  DATA_MODEL.md
  API_SPEC.md
  FINANCIAL_RULES.md
  TEST_STRATEGY.md
  RELEASE_PLAN.md
  CLAUDE.md
  ADR

scripts
  setup
  lint
  test
  build
  seed
  verify
```

> Phase 0 status: `apps/androidApp`, `apps/iosApp`, `shared/core`, `shared/design_system`, and `shared/navigation` are real Gradle modules with working code. Every other `shared/*` module and all of `backend/*` exist only as placeholder directories with a README describing their purpose and the phase in which they will be implemented (see `RELEASE_PLAN.md`). They are intentionally not wired into `settings.gradle.kts` until a phase actually needs them, to avoid empty modules inflating build time and Gradle configuration for no benefit.

## 15. Module Architecture

Use clean architecture with unidirectional data flow. Each feature module should contain:

```
domain
data
presentation
di
test
```

**Domain layer**: entities, value objects, repository interfaces, use cases, validation rules.

**Data layer**: API implementations, database implementations, data transfer objects, mapping logic, cache logic.

**Presentation layer**: screen state, user actions, view model, composable screen, navigation events, accessibility labels.

## 17. API Design

Use versioned REST APIs. See `API_SPEC.md` for the endpoint list and OpenAPI generation approach. No backend API exists yet in Phase 0.
