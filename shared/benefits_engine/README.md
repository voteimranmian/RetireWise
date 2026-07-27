# shared/benefits_engine

Implemented in Phase 6 (Government benefits), per `docs/RELEASE_PLAN.md`.

Domain-only Kotlin Multiplatform module (no `data`/`presentation`/`di` layers), mirroring `shared/retirement_engine`'s scaffold — pure functions and versioned rule data, nothing to persist and no UI consumer. Depends only on `shared/core` (for `Money`/`Rate`) and `kotlinx-datetime`; has no dependency on `shared/retirement_engine` or `shared/profile`, so it can be reused by a future tax engine or dashboard.

Contents:

- `domain/BenefitRuleSet.kt` / `domain/BenefitRuleRepository.kt` — versioned, sourced CPP/OAS/GIS rule data (`CanadaBenefitRuleRepositoryV1`).
- `domain/formula/CppBenefitFormulas.kt`, `OasBenefitFormulas.kt`, `GisBenefitFormulas.kt` — pure formula functions applying the statutory timing adjustment to a user-supplied benefit estimate.
- `domain/GovernmentBenefitEstimate.kt` — result type consumed by `shared/retirement_engine`.

Scope: accepts a **user-supplied benefit estimate at age 65** and applies only the statutory early/late timing adjustment — it does not compute CPP from earnings history or determine OAS/GIS eligibility from residency years (neither is collected by onboarding). GIS uses a linear approximation, not the real piecewise-banded reduction table. See `docs/ADR/0007-government-benefit-scope-and-sourcing-for-phase-6.md` and `docs/FINANCIAL_RULES.md` section 11.7 for the full scope, sourcing, and known limitations.
