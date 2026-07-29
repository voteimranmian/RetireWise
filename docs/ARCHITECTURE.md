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
9. Navigation Compose Multiplatform (`org.jetbrains.androidx.navigation:navigation-compose`) — see ADR 0003
10. Kotlin test for shared unit tests

Use native implementations through `expect`/`actual` declarations where required.

> Phase 0 note: Ktor, SQLDelight, and Multiplatform Settings are declared in the version catalogue but only added to a module's dependencies when that module first needs them (e.g. SQLDelight lands with the profile/persistence work, Ktor lands with the backend API client). Koin is wired in Phase 0 because dependency injection is an explicit Phase 0 exit criteria. Navigation Compose Multiplatform is wired in Phase 2, once the five primary destinations that need real navigation exist (see ADR 0003) — Phase 0/1 used a minimal hand-written `Screen` sealed interface instead.

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
>
> Phase 3 infrastructure note: `ca-central-1` (Montreal) was chosen as the Canadian AWS region. See `docs/ADR/0004-aws-cdk-ecs-fargate-dev-infrastructure.md` for the AWS CDK / ECS Fargate / dev-only-environment decision and its cost/risk tradeoffs.

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

infra

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

> Phase 0 status: `apps/androidApp`, `apps/iosApp`, `shared/core`, `shared/design_system`, `shared/navigation`, `shared/authentication`, `shared/profile`, `shared/onboarding`, `shared/retirement_engine`, `shared/benefits_engine`, `shared/scenario_engine`, `infra`, and `backend/authentication` are real Gradle modules with working code. Every other `shared/*` module and the rest of `backend/*` exist only as placeholder directories with a README describing their purpose and the phase in which they will be implemented (see `RELEASE_PLAN.md`). They are intentionally not wired into `settings.gradle.kts` until a phase actually needs them, to avoid empty modules inflating build time and Gradle configuration for no benefit.
>
> Phase 4 status: `shared/profile` (domain-only `Profile`/`RetirementGoal`/`ProfileRepository`, no UI) and `shared/onboarding` (the 12-question initial assessment from `PRD.md` section 9.1, `domain`/`data`/`presentation`/`di` layers) ship a client-side scaffold only — both repositories are in-memory, not persisted across app restarts, since no backend exists yet. See `RELEASE_PLAN.md` Phase 4 for what is and isn't done.
>
> Phase 5 status: `shared/retirement_engine` is **domain-only** — no `data`/`presentation`/`di` layers, deliberately deviating from the layering below, because a projection is a pure function of `(ProjectionRequest.Ready, Assumptions, calculationDate)` with nothing to persist yet and no UI consumer. It depends on `shared/core`, `shared/benefits_engine`, and `shared/profile` (for `Profile`/`RetirementGoal` input types) and `kotlinx-datetime`. `Money`/`Rate` originally lived in this module but moved to `shared/core` in Phase 6 so `shared/benefits_engine` could use them without a circular dependency. See `RELEASE_PLAN.md` Phase 5 and `docs/ADR/0006-money-and-decimal-representation-for-retirement-engine.md` for what is and isn't done.
>
> Phase 6 status: `shared/benefits_engine` is **domain-only**, mirroring `shared/retirement_engine`'s scaffold. It depends only on `shared/core` (no dependency on `retirement_engine` or `profile`), and provides `BenefitRuleSet`/`BenefitRuleRepository` (versioned, sourced CPP/OAS/GIS rule data) plus pure formula functions consumed by `shared/retirement_engine`. See `RELEASE_PLAN.md` Phase 6 and `docs/ADR/0007-government-benefit-scope-and-sourcing-for-phase-6.md` for what is and isn't done.
>
> Phase 7 status: `shared/scenario_engine` is **domain-only**, mirroring `shared/benefits_engine`'s scaffold (no `data`/`presentation`/`di` layers — no persistence backend exists for scenarios yet, so `Scenario` objects are in-memory only). It depends on `shared/core` and `shared/retirement_engine` (both `api`, since its public functions expose `ProjectionRequest.Ready`/`VersionedProjection`/`ProjectionValue`/`PlanStatus` types) and `shared/benefits_engine` (`implementation`, required explicitly since `retirement_engine`'s own dependency on it is `implementation`). It applies a `ScenarioChangeSet` patch to a base `ProjectionRequest.Ready` and reuses `RetirementProjectionEngine.project()` unmodified to run each scenario, then compares up to three at a time. See `RELEASE_PLAN.md` Phase 7 and `docs/ADR/0008-scenario-planning-scope-and-comparison-metrics-for-phase-7.md` for what is and isn't done.

> Phase 3 status: `shared/authentication` currently ships a client-side scaffold only — real `domain`/`data`/`presentation`/`di` layers exist, but sign-in is backed by `NotConfiguredAuthRepository` (every provider reports `AuthResult.NotConfigured`; there is no backend account service or real Apple/Google/Firebase OAuth integration yet) and consent is backed by `InMemoryConsentRepository` (state is not persisted across app restarts). `infra` (AWS CDK, Kotlin) defines a dev-only AWS environment — VPC, ECR, ECS Fargate, RDS PostgreSQL — and `backend/authentication` is a Ktor skeleton exposing only `GET /health`, meant to prove that pipeline deploys correctly before real sign-in/session endpoints are built. See `RELEASE_PLAN.md` Phase 3 and `docs/ADR/0004-aws-cdk-ecs-fargate-dev-infrastructure.md` for what is and isn't done.

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
