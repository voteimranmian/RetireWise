# ADR 0008: Scenario planning scope, module layout, and comparison metrics for Phase 7

## Context

`docs/RELEASE_PLAN.md` Phase 7 ("Scenario planning") requires that "a user can compare three retirement scenarios," with plan items: create scenario model, clone base plan, apply scenario changes, run calculations, compare scenarios, display comparison cards, add natural-language scenario preview.

`docs/PRD.md` section 8.3 lists nine scenario types. Only six are expressible with today's `ProjectionRequest.Ready`/`Assumptions` fields: retire earlier, delay retirement (`retirementAge`), delay CPP (`cppStartAge`), delay OAS (`oasStartAge`), increase savings (`employeeAnnualContribution`), and change retirement spending (`targetAnnualSpending`). "Pay off mortgage," "downsize home," and "work part time" require debt-payoff, housing-equity, and partial-income modeling that does not exist in any shared module today.

PRD section 19.4's natural-language scenario builder requires the AI advisor, which is Phase 8 and not yet built. PRD section 19.5's comparison screen also lists "monthly after-tax income" and "lifetime taxes paid" — both require a tax engine; `shared/tax_engine` is an unwired placeholder module with no formulas. The same section implies narrative fields ("main advantage," "main tradeoff," "key risk") that require AI-generated prose, which does not exist yet either.

`shared/navigation`'s `ExploreScreen.kt` already has placeholder cards for all nine scenario types, each marked "Available once your plan is built," with a KDoc anticipating Phase 7.

## Decision

1. **Scope**: Phase 7 implements the six scenario types expressible via existing fields. "Pay off mortgage," "downsize home," and "work part time" are deferred until debt-payoff/housing-equity/partial-income modeling exists. This mirrors ADR-0007's precedent of scoping down to what can be computed without fabricating inputs.
2. **New module `shared/scenario_engine`**: domain-only, mirrors `shared/benefits_engine`'s scaffold (no `data`/`presentation`/`di` layers — no persistence backend exists for scenarios yet, so `Scenario` objects are in-memory only this phase). Depends on `shared:core` (`api`), `shared:retirement_engine` (`api`, since this module's public functions expose `ProjectionRequest.Ready`/`VersionedProjection`/`ProjectionValue`/`PlanStatus` types), and `shared:benefits_engine` (`implementation`, required explicitly because `retirement_engine`'s own dependency on `benefits_engine` is `implementation` and so is not transitively visible).
3. **`ScenarioChangeSet` as a nullable-field patch**, not a sealed-per-lever type, so multiple levers combine without a `List` wrapper — matches `Assumptions`'s own nullable-optional-field style.
4. **Reuse `RetirementProjectionEngine.project()` unmodified**: `runScenario`/`createAndRunScenario` apply a `ScenarioChangeSet` to produce a new `ProjectionRequest.Ready` and call `project()` directly — no reimplementation of retirement math.
5. **`ScenarioProjectionSummary` follows the honesty pattern**: fields derivable today (net worth at 80/90, estimated estate, government benefits at retirement, sustainability) are computed; tax-dependent fields (`monthlyAfterTaxIncomeAtRetirement`, `lifetimeTaxesPaid`) are always `ProjectionValue.NotYetModeled`. A new `ScenarioNarrative` sealed interface (`Generated(text)` / `NotYetGenerated(reason)`) mirrors `ProjectionValue` for the three AI-narrative fields, always `NotYetGenerated` this phase.
6. **`compareScenarios` enforces a hard maximum of three scenarios** (`require(scenarios.size <= 3)`), per PRD 19.5's "no more than three." It also requires every scenario to already have a non-null `projectionSummary` (`requireNotNull`), keeping the comparison step pure and cheap rather than an implicit runner.
7. **`PlanId` has no referential integrity**: it is an opaque wrapper with no persisted `Plan` entity to validate against, since no plan-persistence module exists yet.
8. **`ExploreScreen.kt` is left unwired this phase.** Wiring it up would require a ViewModel, a real base-plan source, and state to hold 1-3 in-flight scenarios — a presentation-layer concern distinct from delivering a working comparison engine (plan items 1-6). This is documented as an explicit limitation, not a silent omission.
9. **Natural-language scenario preview (plan item 7) is explicitly deferred to Phase 8**, since it depends on the AI advisor.

## Alternatives considered

- **Sealed-class-per-scenario-type instead of a nullable-field patch**: rejected — would require a `List<ScenarioLever>` to combine multiple levers (e.g., delay retirement + increase savings in one scenario), adding indirection `Assumptions`'s own style doesn't need.
- **Wiring `ExploreScreen.kt` in this phase**: rejected — no ViewModel or base-plan source exists yet, and doing so would blur a domain-engine phase with a presentation-layer phase.
- **Building the three deferred scenario types (mortgage payoff, downsizing, part-time work) using rough approximations**: rejected — would require fabricating debt-amortization, housing-equity, or partial-income assumptions not backed by any existing modeled field, violating the same spirit as rule 5 (no hardcoded/fabricated financial values).
- **Skipping the `compareScenarios` max-3 check and enforcing it only in the UI**: rejected — the comparison engine is the single authoritative place this constraint is checked; relying on UI-layer discipline alone risks silent violations from any future caller (e.g., a future AI advisor calling this directly).
- **Making `compareScenarios` run un-run scenarios itself**: rejected — keeps the function pure and cheap, and keeps "running a projection" (an explicit, traceable step via `createAndRunScenario`/`runScenario`) separate from "comparing already-computed results."

## Advantages

1. Never fabricates a scenario result the underlying request fields can't support — tax and narrative fields stay honestly `NotYetModeled`/`NotYetGenerated` rather than guessed.
2. Reuses `project()` unmodified, so all of Phase 5/6's tested retirement and benefit math applies identically to scenario projections with zero duplication or drift risk.
3. `ScenarioChangeSet`'s patch style lets a single scenario combine multiple levers (e.g., delay retirement and increase savings) without new plumbing.
4. `compareScenarios`'s pure, side-effect-free design keeps it cheap to call repeatedly (e.g., from a future UI recomputing on every lever change) and easy to unit test in isolation from projection running.

## Risks

1. **Three of nine PRD scenario types are unavailable this phase** (pay off mortgage, downsize home, work part time) — users attempting these get no functional path until debt/housing/income modeling is built.
2. **No tax-aware comparison fields**: "monthly after-tax income" and "lifetime taxes paid" are always `NotYetModeled`, so comparison cards will look incomplete relative to the full PRD 19.5 mock until a tax engine exists.
3. **No AI-generated narrative**: "main advantage/tradeoff/key risk" are always `NotYetGenerated`, so scenario cards will lack prose explanation until Phase 8's AI advisor exists.
4. **`PlanId` has no referential integrity check** — a caller could pass an arbitrary string with no corresponding plan, and nothing here would catch it. Acceptable only because no persisted `Plan` entity exists yet to validate against.
5. **`ExploreScreen.kt` remains unwired** — Phase 7's engine has no UI consumer yet, mirroring the same accepted limitation `retirement_engine` and `benefits_engine` already carry.

## Consequences

`shared/scenario_engine` depends on `shared:core`, `shared:retirement_engine`, and `shared:benefits_engine`. No existing module depends on `shared:scenario_engine`, so there is no new consumer to update. Full support for the three deferred scenario types, tax-aware comparison fields, AI-generated narrative, natural-language scenario creation, and `ExploreScreen.kt` wiring remain out of scope until a debt/housing/income-modeling addition, a tax-engine phase, and Phase 8's AI advisor exist, respectively.

## Review date

Revisit once `shared/tax_engine` is wired up (to populate `monthlyAfterTaxIncomeAtRetirement`/`lifetimeTaxesPaid`) and once Phase 8's AI advisor exists (to populate narrative fields and the natural-language scenario builder) — at that point, reassess whether `ScenarioProjectionSummary`'s `NotYetModeled`/`NotYetGenerated` defaults for those fields should be replaced with real computation.
