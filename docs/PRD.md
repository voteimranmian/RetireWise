# RetireWise — Product Requirements Document

## 1. Master Instruction

Build a Kotlin Multiplatform mobile application for iOS and Android using Compose Multiplatform.

RetireWise is a fully AI powered retirement planning coach for Canadians, primarily people in their 40s and 50s.

The product must help users:

1. Understand their current retirement position
2. Build a personalized retirement plan
3. Ask questions conversationally
4. Explore retirement scenarios
5. Understand federal, provincial, and relevant municipal retirement programs
6. Optimize retirement timing, savings, taxes, government benefits, pensions, debt, housing, and withdrawals
7. Receive a clear action plan
8. Learn enough to make informed decisions independently

The product must feel like a senior Canadian retirement advisor with more than 20 years of experience at organizations such as PwC, McKinsey, and RBC.

The product is educational and planning focused. It must not claim to provide regulated investment advice, legal advice, accounting advice, insurance advice, or guaranteed financial outcomes.

Build the application in small, testable vertical slices. Never silently change financial formulas. Never insert hardcoded government benefit amounts into user interface code. Never let the language model perform authoritative financial calculations. Never expose model provider credentials in the mobile application. Never send personally identifiable financial information to an AI provider without explicit consent and server side protection. Never use financial values in analytics events.

## 2. Product Name

Project name: **RetireWise**

## 3. Product Vision

RetireWise is an AI powered retirement planning coach that turns complex Canadian retirement rules into a simple conversation, personalized projections, understandable choices, and practical actions.

The application should answer four questions for every user:

1. Where am I today?
2. Can I achieve the retirement I want?
3. Which choices would improve my outcome?
4. What should I do next?

The product should behave like a thoughtful advisor who listens, asks relevant questions, remembers context, explains tradeoffs, and helps the user make decisions — not like a spreadsheet placed inside a mobile application.

## 4. Product Principles

### 4.1 Conversation first
Users should be able to begin with a question rather than completing a lengthy form (e.g. "Can I retire at 60?"). The AI coach identifies the minimum information required to provide a useful answer.

### 4.2 Explain before requesting
Before asking for sensitive financial information, explain why the information is needed and how it affects the plan.

### 4.3 One decision at a time
Do not present many inputs on one screen. Guide users through a clear sequence.

### 4.4 Calculations must be deterministic
The AI may choose which calculation to run and explain results. The AI may not invent or estimate authoritative outputs when the deterministic calculation engine can produce them.

### 4.5 Every recommendation must be explainable
Each recommendation must show: what is suggested, why it may help, estimated financial impact, assumptions used, risks and tradeoffs, confidence level, and information still missing.

### 4.6 The user remains in control
Users can edit assumptions, reject recommendations, create alternative scenarios, delete conversations, delete financial data, export their information, and choose whether their information can be sent to the AI service.

### 4.7 Progressive personalization
The application should become more useful as the user provides more information. Do not require a complete profile before delivering value.

## 5. Primary Target Users

### 5.1 Core audience
Canadians between the ages of 40 and 59 who want to understand whether they are on track for retirement.

### 5.2 User segments

1. Employees with defined benefit pensions
2. Employees with defined contribution pensions
3. Employees without workplace pensions
4. Self employed professionals
5. Small business owners
6. Couples planning together
7. New Canadians
8. Canadians with significant real estate wealth
9. Canadians approaching retirement with debt
10. Canadians managing RRSP, TFSA, LIRA, pension, property, and non registered assets
11. Canadians who cannot afford or do not currently use a financial advisor

## 6. Core User Outcomes

A successful user should be able to:

1. Complete an initial retirement assessment in less than ten minutes
2. Receive an initial retirement range after answering fewer than twelve questions
3. Understand whether they are currently on track
4. Compare retiring at different ages
5. Understand the implications of starting CPP at different ages
6. Understand the implications of starting OAS at different ages
7. Compare RRSP and TFSA contribution strategies
8. Identify an estimated retirement income gap
9. Receive a prioritized action plan
10. Ask follow up questions in plain English
11. Export a report that can be discussed with a professional advisor

## 7. Product Scope

### 7.1 Minimum viable product

1. Account creation
2. Sign in with Apple
3. Google sign in
4. Email sign in
5. Biometric application lock
6. Conversational onboarding
7. Manual financial profile entry
8. Retirement readiness assessment
9. Retirement cash flow projection
10. CPP estimator
11. OAS estimator
12. RRSP projection
13. TFSA projection
14. Employer pension entry
15. Basic Canadian tax estimation
16. Retirement expense planning
17. Retirement age scenarios
18. CPP and OAS timing scenarios
19. AI retirement coach
20. Personalized action plan
21. Government programs library
22. Source citations
23. Assumption management
24. PDF retirement report
25. English and French localization structure
26. User data export
27. Account and data deletion
28. Privacy and consent controls

### 7.2 Release two

1. Couples planning
2. Provincial tax calculations for all provinces and territories
3. LIRA and LIF modelling
4. RRIF minimum withdrawal rules
5. Pension income splitting scenarios
6. OAS recovery tax modelling
7. Mortgage payoff scenarios
8. Home downsizing scenarios
9. Part time employment scenarios
10. Business owner retirement scenarios
11. Monte Carlo simulation
12. Secure document upload
13. Statement extraction
14. Voice conversation
15. Advisor sharing

### 7.3 Future scope

1. Open banking integrations
2. CRA integration where legally and technically permitted
3. Pension provider integrations
4. Employer sponsored retirement programs
5. Insurance analysis
6. Estate planning workflows
7. Long term care modelling
8. Advisor marketplace
9. Retirement community cost comparisons
10. Household financial collaboration

## 8. Application Navigation

Five primary destinations: **Today**, **Plan**, **Explore**, **Learn**, **Ask AI**.

### 8.1 Today — retirement overview
Retirement readiness status, projected retirement age, estimated retirement income, estimated retirement spending, funding gap or surplus, top three actions, recent questions, important government program updates.

### 8.2 Plan — financial profile and retirement plan
Goals, Income, Expenses, Savings, Investments, Pensions, Government benefits, Property, Debt, Assumptions.

### 8.3 Explore — scenario planning and comparison
Retire earlier, delay retirement, delay CPP, delay OAS, increase savings, pay off mortgage, downsize home, work part time, change retirement spending.

### 8.4 Learn — personalized financial education
Content prioritized based on the user's profile and questions.

### 8.5 Ask AI — the primary conversational retirement advisor
Has access to approved user profile information, calculation tools, scenario tools, and verified retirement content.

## 9. Conversational Onboarding

Opening question: "What would you like help with?"

Suggested answers:

1. Find out when I can retire
2. Check whether I am saving enough
3. Understand CPP and OAS
4. Build my complete retirement plan
5. Ask a retirement question

The application then asks only the questions necessary for that goal.

### 9.1 Minimum initial assessment questions

1. What is your age?
2. Which province or territory do you live in?
3. At what age would you like to retire?
4. What is your approximate annual income?
5. How much have you saved for retirement?
6. Do you have a workplace pension?
7. What do you contribute toward retirement each month?
8. Do you own your home?
9. How much debt do you expect to have at retirement?
10. Approximately how much would you like to spend each month in retirement?
11. Are you planning alone or with a spouse or partner?
12. What matters most to you in retirement?

Provide answer ranges where users may not know exact amounts. Always allow: "I do not know", "Skip for now", "Explain why you need this".

## 19. Core Screen Specifications

### 19.1 Welcome screen
Headline: "Plan your retirement with confidence"
Supporting message: "Get clear answers, personalized projections, and practical actions based on Canadian retirement programs."
Primary action: "Start my plan"
Secondary action: "Ask a question"

### 19.2 Today dashboard
Top card "Your retirement outlook": current outlook, target retirement age, estimated after tax monthly income, expected monthly spending, funding status. Then: top three actions, Ask AI prompt, plan completeness, government updates, recent scenarios.

### 19.3 Plan completeness screen
Categories: Goals, Income, Savings, Pensions, Property, Debt, Expenses, Government benefits. Each shows Complete / Partially complete / Not started, with an explanation of how completing each category improves accuracy.

### 19.4 Scenario builder
Allow natural language scenario creation (e.g. "What if I retire at 62 and delay CPP until 70?"). The AI translates the question into structured scenario changes and asks the user to approve them before running the calculation.

### 19.5 Comparison screen
Compare no more than three scenarios: retirement age, monthly after tax income, lifetime taxes, savings at age 80, savings at age 90, estimated estate, plan sustainability, main advantage, main tradeoff, key risk.

## 32. Build Sequence (Phases)

See `RELEASE_PLAN.md` for the full phased build sequence and exit criteria used to sequence Claude Code work.

## 34. Definition of Done

A feature is complete only when:

1. Acceptance criteria are met
2. Financial logic has tests
3. User interface states are complete
4. Accessibility has been considered
5. Analytics contains no financial values
6. Security implications are addressed
7. Documentation is updated
8. Android builds
9. iOS builds where the environment supports it
10. Tests pass
11. Errors are handled
12. Loading states are handled
13. Empty states are handled
14. French localization keys exist
15. No secrets are committed
16. No unresolved critical warnings remain

## 35. Product Success Measures

1. Initial assessment completion
2. Time to first useful answer
3. Percentage of users who create a scenario
4. Percentage of users who complete their plan
5. Percentage of users who accept an action
6. Monthly active planners
7. Returning users
8. Questions answered with verified sources
9. Calculation error rate
10. AI tool selection accuracy
11. User reported confidence improvement
12. Report generation rate
13. Retention after thirty days
14. Cost per active user
15. Professional escalation rate

## 36. Non Negotiable Product Rule

The application may be fully AI powered in experience, but it must not be AI dependent for financial truth.

The AI is the conversation, orchestration, explanation, and personalization layer. The deterministic retirement engine is the calculation authority. The verified policy system is the government program authority. The user is the final decision maker.
