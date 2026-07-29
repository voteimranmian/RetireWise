# shared/scenario_comparison

Domain + presentation Gradle module. Implemented in Phase 7b, per docs/RELEASE_PLAN.md.

Mirrors `shared/onboarding`'s scaffold — `domain`/`presentation` layers only, no `data`/`di` layer, since no new repository is introduced (`ProfileRepository` from `shared/profile` is reused via the existing `profileModule`).

Depends on `shared/core`, `shared/profile`, `shared/retirement_engine`, `shared/design_system` (`api`), and `shared/scenario_engine`/`shared/benefits_engine` (`implementation`).

Provides:

- `ScenarioLever`/`LeverInputKind`/`isSupported()` (the 9 docs/PRD.md section 8.3 scenario types, 6 of them supported this phase)
- `buildChangeSet` (maps a supported lever + raw input onto a `ScenarioChangeSet`)
- `ScenarioBuilderState`/`canAddScenario`/`addScenario`/`removeScenario` (up to 3 built scenarios, mirroring `OnboardingState.kt`'s style)
- `ScenarioComparisonScreen` (entry composable — loads the profile/goal, runs the base plan projection, lets a user build/remove/compare scenarios)
- `ScenarioLeverPicker`, `ScenarioLeverInputForm`, `ScenarioSummaryCard` (supporting composables and their pure helper functions: `scenarioLeverIcon`, `scenarioLeverInputValue`, `formatMoney`)

`shared/navigation`'s `ExploreScreen.kt` renders `ScenarioLever.entries` and navigates to `ScenarioComparisonScreen` for the supported levers.

See `docs/ADR/0009-default-assumption-set-for-ui-projections.md` for the new `AssumptionSetV1` default assumption set this module needed to unblock calling `project()` from the UI, and `docs/RELEASE_PLAN.md` Phase 7's "Known limitations" for what is and isn't done (3 deferred scenario types, tax/narrative fields still `NotYetModeled`/`NotYetGenerated`, `AssumptionSetV1` not user-adjustable yet, `PlanId` has no referential integrity).
