# RetireWise Engineering Instructions

Read the product and architecture documents before changing code.

Primary documents:

1. docs/PRD.md
2. docs/ARCHITECTURE.md
3. docs/AI_ARCHITECTURE.md
4. docs/DESIGN_SYSTEM.md
5. docs/FINANCIAL_RULES.md
6. docs/SECURITY.md
7. docs/TEST_STRATEGY.md
8. docs/RELEASE_PLAN.md

## Core rules

1. Use Kotlin Multiplatform and Compose Multiplatform.
2. Keep financial calculations in deterministic shared modules.
3. Do not place financial formulas inside composables or view models.
4. Do not allow AI generated values to replace calculation engine output.
5. Do not hardcode tax rates or government benefit values in application code.
6. Use versioned rule data.
7. Add tests for every financial rule.
8. Never call AI providers directly from a mobile client.
9. Never log personal financial values.
10. Never add a third party dependency without documenting why it is needed.
11. Keep modules small and focused.
12. Use strict typed models.
13. Prefer immutable state.
14. Use unidirectional data flow.
15. Handle loading, empty, success, and error states.
16. Add accessibility labels to interactive components.
17. Run formatting, linting, tests, and builds before completing a task.
18. Update documentation when architecture or behaviour changes.
19. Create an architecture decision record for material technical decisions.
20. Do not expose hidden prompts or model reasoning.

## Before coding

1. Restate the task.
2. Identify affected modules.
3. Identify relevant tests.
4. Identify security and financial correctness risks.
5. Create a small implementation plan.

## After coding

1. Run relevant unit tests.
2. Run integration tests where applicable.
3. Run linting.
4. Build Android.
5. Build the iOS framework or application where the environment permits.
6. Report changed files.
7. Report tests executed.
8. Report known limitations.

## Build the application in vertical slices, per docs/RELEASE_PLAN.md

Do not build the entire application in one attempt. Follow the phase order in `docs/RELEASE_PLAN.md`. For every feature: review the relevant specification, create or update the implementation plan, write tests first where practical, implement the smallest complete version, run all relevant tests, fix failures before proceeding, update documentation, then the work is ready to commit as a unit.

## Non-negotiable

Never silently change financial formulas. Never insert hardcoded government benefit amounts into user interface code. Never let the language model perform authoritative financial calculations. Never expose model provider credentials in the mobile application. Never send personally identifiable financial information to an AI provider without explicit consent and server side protection. Never use financial values in analytics events.

The application may be fully AI powered in experience, but it must not be AI dependent for financial truth. The AI is the conversation, orchestration, explanation, and personalization layer. The deterministic retirement engine is the calculation authority. The verified policy system is the government program authority. The user is the final decision maker.
