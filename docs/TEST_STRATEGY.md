# RetireWise — Test Strategy

## 24.1 Unit testing

Test: every financial formula, every benefit rule, every tax calculation, every scenario transformation, every recommendation rule, every data mapper, every validation rule, every view model. Target very high coverage for calculation and rule modules.

## 24.2 Golden financial test cases

Fixed test households with known expected outcomes:

1. Single employee with no pension
2. Married couple with two RRSP accounts
3. Defined benefit pension household
4. Self employed person
5. Small business owner
6. Homeowner with mortgage at retirement
7. Lower income GIS eligible retiree
8. High income OAS recovery tax scenario
9. Early retirement at age 55
10. Delayed CPP and OAS scenario

Each rule update must rerun all golden cases.

## 24.3 Property based testing

Invariants such as:

1. Increasing retirement spending should not improve sustainability
2. Increasing savings while holding all else constant should not reduce projected assets
3. Delaying retirement while continuing contributions should generally improve funding
4. Account balances must never become negative unless the model explicitly supports borrowing
5. Taxable income must reconcile with income components

## 24.4 AI testing

Evaluation suite covering: correct tool selection, refusal to invent benefit values, correct use of sources, identification of missing information, safe response to investment requests, clear distinction between estimates and facts, prompt injection resistance, consistent structured output, English quality, French quality, appropriate professional review recommendations, no unsupported certainty.

## 24.5 User interface testing

Navigation, forms, screen rotation where supported, small devices, large devices, Dynamic Type, screen readers, dark mode, offline state, slow network state, error recovery, streaming AI responses, interrupted calculations, session expiry.

## 24.6 Integration testing

Authentication, profile synchronization, calculation API, scenario creation, AI tool orchestration, policy retrieval, report creation, data export, account deletion, notification delivery.

## Phase 0 status

Shared module unit test source sets are configured with `kotlin.test`, with one passing sample test as a smoke check. Golden financial test cases, property based tests, and AI evaluation suites are introduced alongside their respective engines/features per `RELEASE_PLAN.md`.

## Phase 5 status

`shared/retirement_engine` has unit tests for every formula function (`ContributionFormulasTest`, `GrowthFormulasTest`, `InflationFormulasTest`, `ExpenseFormulasTest`, `IncomeFormulasTest`, `WithdrawalFormulasTest`), covering normal case, retirement-age boundary, and depletion/clamp-at-zero. `MoneyTest`/`RateTest` moved to `shared/core` in Phase 6 alongside the `Money`/`Rate` value types themselves.

## Phase 6 status

`shared/benefits_engine` has unit tests for every formula function and rule source (`CppBenefitFormulasTest`, `OasBenefitFormulasTest`, `GisBenefitFormulasTest`, `BenefitRuleRepositoryTest`), covering the standard-age case, early/late adjustment, start-age clamping, and GIS's zero/cutoff/above-cutoff/midpoint boundaries.

Golden households (24.2): **5 of 10 implemented** — #1 (single employee, no pension), #3 (defined benefit pension household), #9 (early retirement at 55), #7 (lower-income GIS-eligible retiree), and #10 (delayed CPP/OAS increases the benefit once started). Their expected CPP/OAS/GIS figures are computed by calling the same `benefits_engine` formula functions directly in the test (formula-level correctness is already covered by the dedicated unit tests above; the golden test's purpose is to verify `RetirementProjectionEngine` wiring, not re-derive floating-point rounding by hand). #2, #5, #6, #8 remain gaps pending couples modeling, real mortgage amortization, or a tax engine; #4 (self-employed) is flagged as a cheap follow-up once employment/business income sources exist.

Property based invariants (24.3): implemented as a hand-rolled seeded `kotlin.random.Random` sampler (300 iterations, fixed seed) in `RetirementProjectionInvariantsTest` rather than adding a property-testing library — none exists in the version catalogue and a handful of fixed invariants don't justify the dependency. Maps directly to the listed invariants: increasing spending never improves final net worth; increasing contributions never reduces final net worth; delaying retirement never worsens final net worth (pension/other income held at zero for this comparison, since an indexed pension's amount depends on years-since-retirement and would confound the comparison); account balances never go negative; known income components reconcile with `savingsSurplusOrDeficit`; and, added in Phase 6, deferring CPP or OAS start age never decreases the adjusted annual benefit amount (monotonicity of the actuarial adjustment).

## Phase 7 status

`shared/scenario_engine` has unit tests covering every new type and formula: `ScenarioChangeSetTest` (`isIdentity()` true/false per field), `ScenarioApplicationTest` (identity case, each of the five levers changed alone, a combined two-lever case, and a regression guard that `cppStartAge`/`oasStartAge` land on the nested `assumptions` rather than the top-level request), `ScenarioProjectionEngineTest` (`runScenario` matches a direct `project()` call on the applied request; `createAndRunScenario` populates every `Scenario` field, including stamping `calculationVersion` from `projection.metadata.engineVersion`), `ScenarioSummaryFormulasTest` (net worth at 80/90 both `Known` in range and `NotYetModeled` when the projection ends early; government benefits forwards `Known`/`NotYetModeled` correctly; `isSustainableThroughProjectionEnd` true/false; tax and narrative fields pinned to always `NotYetModeled`/`NotYetGenerated` this phase), and `ScenarioComparisonTest` (exactly three scenarios succeeds; zero/one/two succeed; four or more throws `IllegalArgumentException`; an un-run scenario with a null `projectionSummary` throws `IllegalArgumentException`).

No new property-based invariant was added — "delaying retirement never worsens net worth" is already covered by `RetirementProjectionInvariantsTest` operating directly on `ProjectionRequest.Ready`; scenario_engine's tests stay focused on scenario-application/comparison logic rather than re-verifying retirement math.

## Phase 7b status

`shared/retirement_engine`'s new `AssumptionSetV1Test` pins the exact stamped `assumptionSetVersion` string and the three `Rate.ofPercent()` values, so a future accidental edit to the default rates is caught immediately.

`shared/scenario_comparison` has unit tests for every pure function, all independent of the Compose runtime (mirroring `ExploreScreenTest`'s existing style): `ScenarioLeverTest` (`isSupported()` per lever; `buildChangeSet` maps each supported lever's input onto the correct single `ScenarioChangeSet` field with every other field null; throws for an unsupported lever or a mismatched input kind), `ScenarioBuilderStateTest` (`canAddScenario` true/false at the 3-scenario cap; `addScenario` throws once at the cap and clears `configuringLever`; `removeScenario` drops a matching id and no-ops on an unknown one), `ScenarioLeverIconTest` (every lever maps to its expected icon), `ScenarioLeverInputValueTest` (age/money parsing, blank/unparsable text yields `null`, an unsupported lever always yields `null`), and `FormatMoneyTest` (positive/negative amounts, zero, and thousands-grouping).

`shared/navigation`'s `ExploreScreenTest` was rewritten against the new `ScenarioLever`-driven card list (it previously tested the now-removed `exploreScenarioTypes()`/`exploreScenarioIcon()` pure functions): confirms all 9 PRD levers are present, exactly 6 are supported/3 are not, and every lever has a mapped icon.

No new golden households or property-based invariants — this phase is presentation wiring over `scenario_engine`/`retirement_engine`'s already-tested pure functions; regression coverage comes from re-running those modules' existing suites.
