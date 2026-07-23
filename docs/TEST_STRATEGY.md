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
