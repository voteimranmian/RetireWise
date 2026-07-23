# RetireWise — Release Plan / Build Sequence

Claude Code builds the application in this order, in small testable vertical slices. Do not skip ahead. Each phase must meet its exit criteria before the next phase begins.

## Phase 0: Foundation — IN PROGRESS

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

## Phase 1: Design system

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

## Phase 2: Application shell

1. Create navigation
2. Create Today screen
3. Create Plan screen
4. Create Explore screen
5. Create Learn screen
6. Create Ask AI screen
7. Add platform adaptive behaviour

**Exit criteria:** All main destinations work on both platforms.

## Phase 3: Authentication and consent

1. Create account flow
2. Add Sign in with Apple
3. Add Google sign in
4. Add email sign in
5. Add session management
6. Add biometric lock
7. Add privacy consent
8. Add AI data consent
9. Add account deletion

**Exit criteria:** A user can securely create, access, lock, export, and delete an account.

## Phase 4: Conversational onboarding

1. Implement onboarding state machine
2. Implement question cards
3. Implement answer ranges
4. Implement skip and explain actions
5. Save draft progress
6. Create initial profile
7. Create initial retirement goal

**Exit criteria:** A user can complete the initial assessment and receive a placeholder readiness result.

## Phase 5: Retirement engine

1. Create financial value objects
2. Create assumptions
3. Create annual projection model
4. Implement contributions
5. Implement growth
6. Implement inflation
7. Implement retirement expenses
8. Implement income sources
9. Implement account withdrawals
10. Add golden test cases

**Exit criteria:** The deterministic engine can generate a versioned annual projection.

## Phase 6: Government benefits

1. Implement CPP rule interface
2. Implement OAS rule interface
3. Implement GIS rule interface
4. Add versioned rule repository
5. Add official source metadata
6. Add benefit tests

**Exit criteria:** The engine can produce sourced and versioned government benefit estimates.

## Phase 7: Scenario planning

1. Create scenario model
2. Clone base plan
3. Apply scenario changes
4. Run calculations
5. Compare scenarios
6. Display comparison cards
7. Add natural language scenario preview

**Exit criteria:** A user can compare three retirement scenarios.

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
