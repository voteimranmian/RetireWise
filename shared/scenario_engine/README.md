# shared/scenario_engine

Domain-only Gradle module. Implemented in Phase 7 (Scenario planning), per docs/RELEASE_PLAN.md.

Mirrors `shared/benefits_engine`'s scaffold — no `data`/`presentation`/`di` layers, since no persistence backend exists for scenarios yet (`Scenario` objects are in-memory only).

Depends on `shared/core` and `shared/retirement_engine` (`api`, since public functions expose `ProjectionRequest.Ready`/`VersionedProjection`/`ProjectionValue`/`PlanStatus`) and `shared/benefits_engine` (`implementation`).

Provides:

- `Scenario`, `ScenarioChangeSet`, `ScenarioProjectionSummary`, `ScenarioNarrative` (domain model)
- `cloneBasePlan`/`applyScenarioChange` (apply a scenario's changes to a base plan)
- `runScenario`/`createAndRunScenario` (run a scenario's projection, reusing `RetirementProjectionEngine.project()` unmodified)
- `summarizeProjection` (derive a `ScenarioProjectionSummary` from a projection)
- `compareScenarios` (compare up to three scenarios against a base summary)

See `docs/ADR/0008-scenario-planning-scope-and-comparison-metrics-for-phase-7.md` for scope decisions and known limitations (3 of 9 PRD scenario types deferred, no tax-engine fields, no AI-generated narrative, no UI consumer yet).
