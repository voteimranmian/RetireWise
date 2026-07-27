# ADR 0007: Government benefit scope, sourcing, and module layout for Phase 6

## Context

`docs/RELEASE_PLAN.md` Phase 6 ("Government benefits") requires the engine to produce "sourced and versioned government benefit estimates" for CPP, OAS, and GIS. `CLAUDE.md` rules 5 ("do not hardcode tax rates or government benefit values in application code") and 6 ("use versioned rule data") apply directly, since this is the first phase to introduce real CPP/OAS/GIS dollar figures anywhere in the codebase.

A full, first-principles CPP calculation requires a 40+ year earnings history; full OAS/GIS eligibility requires residency-years tracking. Neither is collected by this app's onboarding (`shared/profile`), and fabricating either would violate rule 5 directly. `docs/PRD.md` itself frames this feature as "CPP and OAS timing scenarios," not a from-scratch benefit calculator.

The new benefit formulas also need `Money`/`Rate`, and `shared/retirement_engine` needs to consume them — but `Money`/`Rate` previously lived inside `shared/retirement_engine`, which would create a circular module dependency.

## Decision

1. **Scope**: Phase 6 accepts a **user-supplied benefit estimate at age 65** for CPP and OAS (`Assumptions.estimatedCppAmountAtAge65`/`estimatedOasAmountAtAge65`) and applies only the **statutory timing adjustment** (the actuarial early/late reduction or increase) on top of it. It does not compute CPP from earnings history or determine OAS/GIS eligibility from residency years — those remain Release-two scope.
2. **New module `shared/benefits_engine`**: domain-only, mirrors `shared/retirement_engine`'s scaffold. Contains `BenefitRuleSet` (the versioned, sourced rule data), `BenefitRuleRepository` (interface + `CanadaBenefitRuleRepositoryV1`), and pure formula functions (`cppAdjustedAnnualAmount`, `oasAdjustedAnnualAmount`, `gisAnnualAmount`).
3. **Relocate `Money`/`Rate` to `shared/core`**: both `benefits_engine` and `retirement_engine` need these value types, and `retirement_engine` depends on `benefits_engine`, so they must live in a module both can reach without a cycle. `shared/core` already sits below both.
4. **`BenefitRuleRepository` is an explicit, defaulted parameter** to `RetirementProjectionEngine.project()` (default `CanadaBenefitRuleRepositoryV1`) rather than an internal-only call, so a specific historical rule-set version can be re-supplied later — keeping old reports reproducible per `docs/FINANCIAL_RULES.md` section 11.5.
5. **GIS uses a linear approximation**: `gisAnnualAmount` interpolates between two published anchor points (max GIS at $0 non-OAS income; $0 GIS at the official income cutoff) rather than the real piecewise-banded reduction table. This is a documented MVP simplification, not the authoritative Service Canada schedule.
6. **Sourcing**: CPP's 0.6%/month early reduction, 0.7%/month late increase, and OAS's 0.6%/month deferral increase are long-standing, structural Service Canada rules (not quarterly-indexed), sourced with high confidence. The GIS maximum annual amount and income cutoff are quarterly-indexed dollar figures; direct `canada.ca` retrieval returned HTTP 403 in this environment, so these two figures are **secondary-sourced** (aggregator sites quoting the same canada.ca statistics) and are explicitly flagged in `BenefitRuleSet.sourceDescription` as pending official re-verification before production use.

## Alternatives considered

- **Full earnings-history CPP calculation**: rejected for this phase — the app does not collect a 40-year earnings history, and no plausible substitute avoids fabricating data (rule 5 violation). Deferred to Release two.
- **Residency-based OAS/GIS eligibility modeling**: same reasoning; deferred to Release two.
- **Keeping `Money`/`Rate` inside `retirement_engine` and having `benefits_engine` depend on `retirement_engine`**: would invert the natural dependency (the retirement engine needs benefit *amounts*, not the other way around) and still leaves a cycle once `retirement_engine` also needs to call into `benefits_engine`'s formulas.
- **Hardcoding CPP/OAS/GIS figures directly inside `RetirementProjectionEngine`**: rejected — violates rule 6 (versioned rule data) and rule 5's spirit; a dedicated, versioned `BenefitRuleSet`/`BenefitRuleRepository` is the same pattern the codebase already uses for other sourced-data boundaries.
- **The real GIS piecewise-banded reduction table**: rejected for this slice on effort-vs-value grounds given the MVP's other simplifications (flat-carried debt, no couples modeling); the linear approximation is anchored to the same two published figures and documented as an approximation rather than silently presented as exact.

## Advantages

1. Never fabricates a CPP/OAS/GIS dollar figure the user didn't supply or that isn't traceable to a documented source.
2. `governmentBenefits` becomes `ProjectionValue.Known` only when the user has actually supplied an estimate — fully backward compatible with every Phase 5 caller (nullable, defaulted fields).
3. `BenefitRuleRepository` being swappable keeps historical projections reproducible even as rule sets are updated in later phases.
4. Clean module boundary: `benefits_engine` has no dependency on `retirement_engine` or `profile`, so it can be reused by a future tax engine or dashboard without pulling in projection-specific types.

## Risks

1. **GIS's linear approximation understates or overstates GIS in some income bands** relative to the real piecewise table — acceptable for this MVP slice but must be revisited before this figure is presented as authoritative.
2. **Secondary-sourced GIS dollar figures**: the max annual amount and income cutoff came from aggregator sites, not a direct canada.ca fetch (which returned HTTP 403 in this environment). These need official re-verification before production use — tracked via `BenefitRuleSet.sourceDescription`.
3. **No partial CPP/OAS**: a user who supplies only one of the two estimates gets the other program's contribution treated as $0 rather than `NotYetModeled` — consistent with how other optional income fields (`otherRetirementIncome`, etc.) already default to zero, but worth noting as a judgment call distinct from the all-or-nothing `NotYetModeled` treatment used for taxes.

## Consequences

`shared/retirement_engine` now depends on `shared/benefits_engine`, and both depend on `shared/core` for `Money`/`Rate`. `AnnualProjectionEntry.governmentBenefits` and `CalculationMetadata.governmentBenefitRuleVersion` are populated per the rules above. Full CPP/OAS/GIS eligibility and calculation, the real GIS reduction table, and OAS recovery tax (golden household #8, which needs a tax engine) remain out of scope until Release two or a future tax-engine phase.

## Review date

Revisit before any production launch: re-verify the GIS maximum amount and income cutoff directly against canada.ca, and reassess whether the linear GIS approximation is acceptable or needs the real piecewise table.
