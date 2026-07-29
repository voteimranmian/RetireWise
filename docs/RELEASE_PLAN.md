# RetireWise — Release Plan / Build Sequence

Claude Code builds the application in this order, in small testable vertical slices. Do not skip ahead. Each phase must meet its exit criteria before the next phase begins.

## Phase 0: Foundation — COMPLETE

1. Create repository
2. Create Kotlin Multiplatform project
3. Configure Android application
4. Configure iOS application
5. Configure Compose Multiplatform
6. Configure formatting and linting
7. Configure continuous integration
8. Create documentation structure
9. Create environment configuration
10. Create dependency catalogue

**Exit criteria:** Both platform applications display the same basic screen and pass the build pipeline.

## Phase 1: Design system — COMPLETE

1. Create semantic colour tokens
2. Create typography
3. Create spacing tokens
4. Create application theme
5. Create buttons
6. Create cards
7. Create input fields
8. Create selection controls
9. Create progress components
10. Create accessible chart container
11. Create loading and error components

**Exit criteria:** A design system showcase screen runs on iOS and Android.

Design system v2 visual refresh (finalized colour tokens, Inter typeface, Material icons, soft-depth cards) applied after Phase 2 — see `docs/DESIGN_SYSTEM.md` "Design system v2 status" and ADR 0005.

## Phase 2: Application shell — COMPLETE

1. Create navigation
2. Create Today screen
3. Create Plan screen
4. Create Explore screen
5. Create Learn screen
6. Create Ask AI screen
7. Add platform adaptive behaviour

**Exit criteria:** All main destinations work on both platforms. See `docs/DESIGN_SYSTEM.md` "Phase 2 status" and ADR 0003 for details.

## Phase 3: Authentication and consent — IN PROGRESS

1. Create account flow — DONE (client-side scaffold; see below)
2. Add Sign in with Apple — BLOCKED (needs real Apple OAuth credentials/backend)
3. Add Google sign in — BLOCKED (needs real Google OAuth credentials/backend)
4. Add email sign in — BLOCKED (needs a backend account service)
5. Add session management — BLOCKED (needs a backend account service)
6. Add biometric lock — NOT STARTED
7. Add privacy consent — DONE (client-side scaffold; see below)
8. Add AI data consent — DONE (client-side scaffold; see below)
9. Add account deletion — BLOCKED (needs a backend account service)

**Client-side scaffold (2026-07-23):** The `shared/authentication` module now has real `domain`/`data`/`presentation`/`di` layers (docs/ARCHITECTURE.md section 15) wired into the app's top-level navigation: Welcome → Create Account → Privacy Consent → AI Consent → Main App. Sign-in buttons (Apple/Google/Email) call `NotConfiguredAuthRepository`, which honestly reports `AuthResult.NotConfigured` for every provider rather than faking success, since no backend account service or real OAuth credentials exist yet. A "Skip for now (preview build)" link lets the flow be exercised end to end during development. Consent is recorded via `InMemoryConsentRepository`, which holds state only in memory (not persisted across app restarts). Items 2-6 and 9 above remain blocked or not started pending real backend infrastructure and OAuth credentials, which cannot be provisioned inside this environment.

**AWS dev infrastructure (2026-07-23):** Started provisioning a real backend, on AWS, per `docs/ADR/0004-aws-cdk-ecs-fargate-dev-infrastructure.md`. `infra` (new Gradle module, AWS CDK in Kotlin) defines a dev-only environment in `ca-central-1`: VPC (no NAT gateway), ECR repository, ECS Fargate cluster/service, RDS PostgreSQL with credentials generated into AWS Secrets Manager. `backend/authentication` (new Gradle module, Ktor) is a skeleton exposing only `GET /health`, meant to prove this deploy pipeline works before any real auth logic is built on top of it. Neither has been deployed yet — that requires installing Node.js, Docker, and AWS CLI credentials locally (none of which exist in the environment this was built in), then an explicit `cdk bootstrap`/`cdk deploy` pass. Real sign-in endpoints, session issuance, OAuth provider verification, the `users`/`consents` database schema, and Redis remain the next slice(s), blocked on this pipeline being proven out first.

**Exit criteria:** A user can securely create, access, lock, export, and delete an account.

## Phase 4: Conversational onboarding — DONE (client-side scaffold; see below)

1. Implement onboarding state machine — DONE
2. Implement question cards — DONE
3. Implement answer ranges — DONE (validation functions exist in `shared/profile`; not yet wired to block "Next" beyond requiring a non-blank answer)
4. Implement skip and explain actions — DONE
5. Save draft progress — DONE (in-memory only; see below)
6. Create initial profile — DONE
7. Create initial retirement goal — DONE

**Client-side scaffold (2026-07-24):** Two new modules were added (docs/ARCHITECTURE.md section 15): `shared/profile` (domain-only `Profile`/`RetirementGoal`/`ProfileRepository`, no UI, reusable by the Phase 5 retirement engine) and `shared/onboarding` (the 12-question flow from docs/PRD.md section 9.1, reached via a new "Build my plan" action on Today's empty state). Answers are held in `OnboardingState` and saved after every question via `InMemoryOnboardingDraftRepository`; on completion the resulting `Profile`/`RetirementGoal` are saved via `InMemoryProfileRepository`. Both repositories are in-memory only — the same honest caveat as Phase 3's `InMemoryConsentRepository`, since no backend exists yet to persist them across app restarts. The completion screen (`OnboardingCompleteScreen`) echoes back only what the user entered; it does not compute or display any readiness percentage or projected dollar figures, since that requires the Phase 5 retirement engine.

**Exit criteria:** A user can complete the initial assessment and receive a placeholder readiness result. Met, with the in-memory storage caveat above.

## Phase 5: Retirement engine — DONE (deterministic engine; see below)

1. Create financial value objects — DONE (`Money`, fixed-point; `Rate`, `Double`-backed — docs/ADR/0006-money-and-decimal-representation-for-retirement-engine.md)
2. Create assumptions — DONE (`Assumptions`; no default/baked-in rates — see caveats below)
3. Create annual projection model — DONE (`AnnualProjectionEntry`, `VersionedProjection`, `CalculationMetadata`)
4. Implement contributions — DONE
5. Implement growth — DONE
6. Implement inflation — DONE
7. Implement retirement expenses — DONE
8. Implement income sources — DONE (employment, pension, other; explicitly excludes CPP/OAS/GIS, which is Phase 6)
9. Implement account withdrawals — DONE (sequential: non-registered → RRSP/RRIF → TFSA; clamped at zero, reports unmet shortfall)
10. Add golden test cases — DONE (3 of 10; see caveat below)

**Client-side scaffold (2026-07-27):** New domain-only Gradle module `shared/retirement_engine` (docs/ARCHITECTURE.md section 15) implements `project(request, calculationDate): VersionedProjection`, a pure function folding year-by-year from current age to `Assumptions.projectionEndAge` (default 100). `governmentBenefits`, `grossIncome`, `estimatedTaxes`, and `afterTaxIncome` are always `ProjectionValue.NotYetModeled` this phase — a partial "gross income" that silently excluded CPP/OAS would look misleadingly complete, so all four dependent fields are marked, not just government benefits (same honesty precedent as Phase 3/4's `AuthResult.NotConfigured`). Debt is carried flat from `Profile.expectedDebtAtRetirement` (no amortization curve — onboarding collects only a single point value). `CalculationMetadata` stamps `engineVersion`, `assumptionSetVersion`, `calculationDate`, `formulaIdentifiers`, and explicit `"NOT_IMPLEMENTED"` sentinels for `taxRuleVersion`/`governmentBenefitRuleVersion`.

**Known limitations:** No default `Assumptions` factory — callers must supply explicit, sourced rates (no approved default assumption set exists yet). Golden households (docs/TEST_STRATEGY.md section 24.2): only #1 (single employee, no pension), #3 (defined benefit pension), and #9 (early retirement at 55) were implemented this phase — #7 and #10 followed once Phase 6 landed; #2, #5, #6, #8 need couples modeling, real mortgage amortization, or a tax engine (all Release two / future phases), and #4 (self-employed) is a cheap future follow-up. Property-based invariants (section 24.3) use a hand-rolled seeded `kotlin.random.Random` sampler rather than a new test dependency. No consumer (UI/persistence) exists yet — this phase is domain-only.

**Exit criteria:** The deterministic engine can generate a versioned annual projection. Met.

## Phase 6: Government benefits — DONE (statutory timing adjustment on user-supplied estimates; see below)

1. Implement CPP rule interface — DONE (`cppAdjustedAnnualAmount`; actuarial adjustment only, not earnings-history calculation — see caveat below)
2. Implement OAS rule interface — DONE (`oasAdjustedAnnualAmount`; deferral-only, no early-start option, matching reality)
3. Implement GIS rule interface — DONE (`gisAnnualAmount`; linear approximation of the real piecewise-banded table — see caveat below)
4. Add versioned rule repository — DONE (`BenefitRuleRepository` interface + `CanadaBenefitRuleRepositoryV1`, `ruleVersion = "CANADA_BENEFITS_V1"`)
5. Add official source metadata — DONE (`BenefitRuleSet.sourceDescription`/`sourceVerifiedDate`; see sourcing caveat below)
6. Add benefit tests — DONE (formula unit tests, rule repository sanity tests, 2 new golden households, 1 new property invariant)

**Client-side scaffold (2026-07-27):** New domain-only Gradle module `shared/benefits_engine` (docs/ARCHITECTURE.md section 15), consumed by `shared/retirement_engine`. `Money`/`Rate` relocated from `shared/retirement_engine` to `shared/core` to avoid a circular module dependency (docs/ADR/0007-government-benefit-scope-and-sourcing-for-phase-6.md). `RetirementProjectionEngine.project()` gains a defaulted `benefitRuleRepository` parameter; `AnnualProjectionEntry.governmentBenefits` becomes `ProjectionValue.Known` once at least one of `Assumptions.estimatedCppAmountAtAge65`/`estimatedOasAmountAtAge65` is supplied, otherwise stays `NotYetModeled` (fully backward compatible with every Phase 5 caller).

**Known limitations:** Does not compute CPP from a real earnings history or determine OAS/GIS eligibility from residency years — accepts a user-supplied age-65 estimate and applies only the statutory timing adjustment (Release two: full earnings/residency-based calculation). GIS uses a linear approximation between two published anchor points, not the real piecewise-banded reduction table. GIS's max amount and income cutoff are secondary-sourced (aggregator sites quoting canada.ca; direct canada.ca retrieval returned HTTP 403 in this environment) and are pending official re-verification before production use. Golden household #8 (high income OAS recovery tax) remains deferred — it requires a tax engine, not yet built.

**Exit criteria:** The engine can produce sourced and versioned government benefit estimates. Met, with the scope and sourcing caveats above.

## Phase 7: Scenario planning — DONE (comparison engine; see below)

1. Create scenario model — DONE (`Scenario`, `ScenarioChangeSet`, `ScenarioProjectionSummary`, `ScenarioNarrative`)
2. Clone base plan — DONE (`cloneBasePlan`; identity — `ProjectionRequest.Ready` is already immutable)
3. Apply scenario changes — DONE (`applyScenarioChange`)
4. Run calculations — DONE (`runScenario`/`createAndRunScenario`, reusing `RetirementProjectionEngine.project()` unmodified)
5. Compare scenarios — DONE (`compareScenarios`, max 3 per docs/PRD.md section 19.5)
6. Display comparison cards — NOT STARTED (no UI consumer this phase; see caveat below)
7. Add natural language scenario preview — DEFERRED (needs the Phase 8 AI advisor)

**Client-side scaffold (2026-07-27):** New domain-only Gradle module `shared/scenario_engine` (docs/ARCHITECTURE.md section 15), depending on `shared/core`, `shared/retirement_engine`, and `shared/benefits_engine`. Only 6 of docs/PRD.md section 8.3's 9 scenario types are supported this phase (retire earlier/delay retirement, delay CPP, delay OAS, increase savings, change retirement spending) — see docs/ADR/0008-scenario-planning-scope-and-comparison-metrics-for-phase-7.md.

**Known limitations:** "Pay off mortgage," "downsize home," and "work part time" scenario types are deferred — they need debt-payoff/housing-equity/partial-income modeling that doesn't exist yet (Release two). `ScenarioProjectionSummary.monthlyAfterTaxIncomeAtRetirement`/`lifetimeTaxesPaid` are always `ProjectionValue.NotYetModeled` (no tax engine yet). `mainAdvantage`/`mainTradeoff`/`keyRisk` are always `ScenarioNarrative.NotYetGenerated` (needs the Phase 8 AI advisor). The natural-language scenario preview (item 7) is deferred to Phase 8 for the same reason. `shared/navigation`'s `ExploreScreen.kt` is left unwired this phase — no ViewModel or base-plan source exists yet to drive it. `PlanId` has no referential integrity (no persisted `Plan` entity exists to validate against).

**Exit criteria:** A user can compare three retirement scenarios. Met at the engine level (items 1-5); UI wiring (item 6) and the natural-language preview (item 7) remain open, per the caveats above.

## Phase 8: AI advisor

1. Build backend AI gateway
2. Add provider abstraction
3. Add Anthropic implementation
4. Add intent classifier
5. Add tool registry
6. Add calculation tools
7. Add policy retrieval tool
8. Add structured response validation
9. Add streaming
10. Add conversation summaries
11. Add safety tests

**Exit criteria:** The AI can answer a retirement question using verified profile data, calculation tools, and sources.

## Phase 9: Recommendation engine

1. Create diagnostic rules
2. Create recommendation rules
3. Estimate impacts
4. Rank recommendations
5. Generate plain language explanations
6. Add recommendation acceptance
7. Add action plan

**Exit criteria:** The user receives three traceable priority actions.

## Phase 10: Reports

1. Create report data model
2. Create report template
3. Add projection summary
4. Add assumptions
5. Add recommendations
6. Add sources
7. Add disclaimers
8. Generate PDF
9. Add secure sharing

**Exit criteria:** The user can generate and share a professional retirement planning report.

## Phase 11: Production readiness

1. Security review
2. Privacy review
3. Accessibility review
4. Financial formula review
5. French content review
6. Performance testing
7. AI evaluation
8. Store compliance review
9. Incident response plan
10. Support process

## Release two (post-MVP)

Couples planning, provincial tax calculations for all provinces/territories, LIRA/LIF modelling, RRIF minimum withdrawal rules, pension income splitting, OAS recovery tax modelling, mortgage payoff scenarios, home downsizing scenarios, part time employment scenarios, business owner retirement scenarios, Monte Carlo simulation, secure document upload, statement extraction, voice conversation, advisor sharing.

## Future scope

Open banking integrations, CRA integration where legally/technically permitted, pension provider integrations, employer sponsored retirement programs, insurance analysis, estate planning workflows, long term care modelling, advisor marketplace, retirement community cost comparisons, household financial collaboration.
