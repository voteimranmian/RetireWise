# shared/profile

Real Gradle module (Kotlin Multiplatform: androidTarget + iosX64/iosArm64/iosSimulatorArm64), implemented in Phase 4 (Conversational onboarding), per docs/RELEASE_PLAN.md.

Domain-only `Profile`/`RetirementGoal`/`ProfileRepository` — no UI. Backed by `InMemoryProfileRepository` (state not persisted across app restarts; no backend exists yet). Consumed by `shared/onboarding` and `shared/retirement_engine`.

See docs/RELEASE_PLAN.md's Phase 4 entry for current status and known limitations.
