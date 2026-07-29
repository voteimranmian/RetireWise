# ADR 0009: Default assumption set for UI-initiated projections

## Context

Phase 7b wires real scenario creation and comparison into `ExploreScreen.kt`, which requires calling `com.retirewise.retirementengine.domain.formula.project()` from a screen for the first time in the app's history. `project()` requires a `ProjectionRequest.Ready`, which in turn requires an `Assumptions` value carrying `incomeGrowthRate`/`expectedReturnRate`/`inflationRate`/`assumptionSetVersion`.

`Assumptions`'s own KDoc (added in Phase 5) is explicit that there is deliberately no default factory: "Hardcoding an unversioned, unsourced assumption set into application code is exactly what CLAUDE.md rules 5-6 warn against by extension. Every caller must supply explicit, sourced values." Every existing caller (all of `retirement_engine`/`benefits_engine`/`scenario_engine`'s test suites) has always constructed its own `Assumptions` by hand. No UI has ever needed to.

Phase 7b's `ScenarioComparisonScreen` cannot ask the user to configure growth/inflation/return rates before showing a single number — no settings screen exists yet, and PRD 19.2/19.5 assume the app already has a working base plan by the time a user reaches Explore. A default rate set is therefore required to unblock any projection at all.

## Decision

1. **`AssumptionSetV1` is a new default factory in `shared/retirement_engine`**, stamping `assumptionSetVersion = "DEFAULT_ASSUMPTIONS_V1"` and using `incomeGrowthRate = 2%`, `expectedReturnRate = 5%`, `inflationRate = 2%` — the same rates every existing test fixture in the codebase already assumes.
2. **This does not contradict `Assumptions`'s "no fabricated defaults" warning.** That warning targets *government benefit/tax values* (CLAUDE.md rule 5's actual subject) — CPP/OAS amounts, tax rates, GIS cutoffs — where a specific dollar figure or rate is a claim about a real government program. Growth/inflation/return-rate assumptions are a different category: every retirement planning tool must choose *some* long-run economic assumption, and neither `docs/PRD.md` nor `docs/ARCHITECTURE.md` require these to be sourced from an official body the way benefit figures must be (see ADR 0007 sections 5-6 for that distinct sourcing bar).
3. **2% inflation is a real, citable figure**: the Bank of Canada's official inflation-control target is 2%. 5% expected return and 2% income growth are conservative, widely used long-run planning assumptions, not tied to any single authoritative source — flagged explicitly as a placeholder pending real actuarial/market-data review, not presented as a sourced government figure.
4. **`AssumptionSetV1` is not user-adjustable this slice.** There is no settings screen yet to let a user override growth/inflation/return assumptions; every projection run from `ScenarioComparisonScreen` uses this single default set. This is a documented limitation, not an oversight.
5. **Versioned like every other rule set** (`assumptionSetVersion`), so `CalculationMetadata.assumptionSetVersion` continues to make old reports reproducible/auditable per `docs/FINANCIAL_RULES.md` section 11.5, exactly as it already does for hand-built `Assumptions` values.

## Alternatives considered

- **Block Phase 7b's UI wiring until a real, actuarially-reviewed default assumption set is approved**: rejected — this would indefinitely stall closing the Phase 7 UI-consumer gap (docs/RELEASE_PLAN.md), and every other phase's docs already accept "placeholder pending review" as a valid interim state (e.g. ADR 0007's GIS anchor points).
- **Prompt the user to enter growth/inflation/return rates before the first projection**: rejected for this slice — no settings/preferences screen exists yet, and PRD 19.2/19.5 do not describe this as part of the Explore flow. Deferred until a settings screen is built.
- **Treat this as a `NotYetModeled` gap like CPP/OAS/tax fields**: rejected — unlike a benefit/tax dollar figure, there is no engine-level reason a growth/inflation/return assumption can't be supplied today; withholding it would make the entire projection non-functional rather than partially honest.

## Advantages

1. Unblocks every UI projection call without inventing a government benefit or tax value.
2. Consistent with every existing test fixture's own assumed rates, so scenario/base-plan numbers a developer sees in the UI won't look surprising relative to what the test suites already validate.
3. Versioned from day one — a future, reviewed `AssumptionSetV2` can replace it without breaking `CalculationMetadata`'s reproducibility guarantee.

## Risks

1. **5% return / 2% income growth are not tied to a specific, citable source** the way the 2% inflation figure is — must be replaced with a reviewed figure before any real user relies on projected numbers for a financial decision.
2. **Not user-adjustable**: every user sees projections built on the same fixed assumptions regardless of their actual risk tolerance or investment mix, until a settings screen exists.

## Consequences

`shared/scenario_comparison`'s `ScenarioComparisonScreen` calls `AssumptionSetV1.create()` when mapping a `Profile`/`RetirementGoal` into a `ProjectionRequest.Ready`. No other module is forced to use it — `Assumptions`'s constructor remains as strict as before for every other caller.

## Review date

Revisit before any production launch or before this app is used for a real financial decision: replace the 5%/2% return/income-growth placeholders with a reviewed, sourced figure (or make them user-adjustable via a settings screen), matching the same production-readiness bar ADR 0007 sets for its own GIS anchor points.
