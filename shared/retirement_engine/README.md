# shared/retirement_engine

Real Gradle module (Kotlin Multiplatform: androidTarget + iosX64/iosArm64/iosSimulatorArm64), implemented in Phase 5 (Retirement engine), per docs/RELEASE_PLAN.md.

Domain-only — no `data`/`presentation`/`di` layers this phase. A projection is a pure function of `(ProjectionRequest.Ready, calculationDate) -> VersionedProjection`, computed on demand; there is nothing to persist and no UI consumer yet.

See docs/FINANCIAL_RULES.md section 11 for the spec this module implements, docs/RELEASE_PLAN.md's Phase 5 entry for current status and known limitations, and docs/ADR/0006-money-and-decimal-representation-for-retirement-engine.md for the `Money`/`Rate` value type decision.
