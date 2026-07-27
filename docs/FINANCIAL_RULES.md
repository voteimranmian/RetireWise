# RetireWise — Financial Rules

Not implemented in Phase 0. This is the specification the deterministic `retirement_engine`, `tax_engine`, `benefits_engine`, and recommendation engine must follow starting Phase 5.

## 11. Retirement Calculation Engine

Deterministic shared Kotlin module: `retirement_engine`. No dependency on the user interface or AI provider.

### 11.1 Core calculations

Support annual projections from the user's current age to age 100. Calculate: employment income, savings contributions, employer contributions, investment growth, inflation, retirement expenses, CPP, OAS, GIS where relevant, employer pensions, RRSP balances, RRIF balances, TFSA balances, non registered assets, taxable income, estimated income tax, debt balances, mortgage balances, withdrawal requirements, estate value.

### 11.2 Projection inputs

```
Current age
Retirement age
Projection end age
Province or territory
Marital status
Employment income
Income growth rate
Current assets
Current debts
Contribution amounts
Expected rates of return
Inflation rate
Retirement spending
CPP start age
OAS start age
Pension income
Other retirement income
Property assumptions
Life expectancy assumption
```

### 11.3 Projection outputs (per year)

```
Age
Calendar year
Employment income
Government benefits
Pension income
Account withdrawals
Other income
Gross income
Estimated taxes
After tax income
Expenses
Savings surplus or deficit
RRSP or RRIF balance
TFSA balance
Non registered balance
Debt balance
Net worth
Estate value
Plan status
```

### 11.4 Financial precision

Use decimal financial types. Do not use floating point values for currency. Define standard rounding behaviour. Store currency values in Canadian dollars unless the field explicitly identifies another currency.

### 11.5 Formula versioning

Every calculation result must include: engine version, tax rule version, government benefit rule version, assumption set version, calculation date, formula identifiers used. This allows old reports to be reproduced and audited.

### 11.6 Phase 5 status

`shared/retirement_engine` implements section 11.1's annual projection for the inputs/scope available before Phase 6: employment income, employee/employer contributions, investment growth, inflation, retirement expenses, employer pension pass-through (optionally inflation-indexed), other retirement income, sequential account withdrawals (non-registered → RRSP/RRIF → TFSA, clamped at zero), and net worth/estate value. `engineVersion = "RETIREMENT_ENGINE_V1"` (docs/ADR/0006-money-and-decimal-representation-for-retirement-engine.md covers the `Money`/`Rate` types used throughout).

**Explicitly out of scope this phase, modeled as `ProjectionValue.NotYetModeled` rather than fabricated:** CPP/OAS/GIS (`governmentBenefits` — Phase 6), income tax (`estimatedTaxes`), and the two fields that depend on them (`grossIncome`, `afterTaxIncome`). `taxRuleVersion` and `governmentBenefitRuleVersion` are stamped `"NOT_IMPLEMENTED"` rather than omitted or null. Provincial tax, couples planning, RRIF minimum withdrawals, and real mortgage amortization remain Release-two scope; debt is carried flat (no amortization curve) since onboarding collects only a single point value for `expectedDebtAtRetirement`.

### 11.7 Phase 6 status

CPP, OAS, and GIS are now modeled in the new `shared/benefits_engine` module (`docs/ADR/0007-government-benefit-scope-and-sourcing-for-phase-6.md`), with an explicit scope limit: the engine accepts a **user-supplied benefit estimate at age 65** (`Assumptions.estimatedCppAmountAtAge65`/`estimatedOasAmountAtAge65`) and applies only the statutory timing adjustment (early/late actuarial reduction or increase) on top of it. It does not compute CPP from a 40-year earnings history and does not determine OAS/GIS eligibility from residency years — neither is collected by onboarding, and fabricating either would violate rule 5. `governmentBenefits` becomes `ProjectionValue.Known` once either estimate is supplied; it stays `NotYetModeled` only when neither is. `governmentBenefitRuleVersion` is now always stamped from `BenefitRuleRepository.current().ruleVersion` (`"CANADA_BENEFITS_V1"`) rather than `"NOT_IMPLEMENTED"`.

GIS uses a linear approximation between two published anchor points (max GIS at $0 non-OAS income; $0 GIS at the official income cutoff) rather than the real piecewise-banded reduction table — a documented MVP simplification. The GIS maximum annual amount and income cutoff are secondary-sourced (aggregator sites quoting canada.ca figures; direct canada.ca retrieval returned HTTP 403 in this environment) and are flagged in `BenefitRuleSet.sourceDescription` as pending official re-verification before production use. CPP's 0.6%/month early reduction, 0.7%/month late increase, and OAS's 0.6%/month deferral increase are long-standing, structural Service Canada rules, sourced with high confidence.

Income tax, provincial tax, full CPP/OAS/GIS eligibility calculation, the real GIS reduction table, and OAS recovery tax remain out of scope until Release two or a future tax-engine phase.

## 12. Retirement Readiness Model

Do not present a score as objective truth. Use a retirement readiness range supported by explainable factors:

1. Projected income replacement
2. Projected expense coverage
3. Savings sustainability
4. Debt at retirement
5. Emergency reserve
6. Guaranteed income percentage
7. Inflation resilience
8. Longevity resilience
9. Sequence risk exposure
10. Planning completeness

Display one of: Strong position, Generally on track, Some adjustments recommended, Significant gap identified, More information required. A numeric score may also be shown, but the user must be able to see how it was produced.

## 13. Government Policy Intelligence

Create a verified content and rules system. The AI must retrieve policy information from this system before answering questions about government programs.

### 13.1 Source hierarchy

1. Government of Canada
2. Canada Revenue Agency
3. Service Canada
4. Provincial or territorial government
5. Municipal government
6. Official pension authority
7. Official legislation or regulation
8. Recognized professional body

Secondary sources may explain a topic but must not override official sources.

### 13.2 Government program record

```
Program identifier
Program name
Jurisdiction
Program type
Plain language summary
Eligibility rules
Benefit calculation rules
Tax treatment
Application process
Important dates
Common mistakes
Planning opportunities
Official source
Source publication date
Last verified date
Next review date
Content reviewer
Rule version
Status
```

### 13.3 Policy update workflow

1. Scheduled service identifies source changes
2. New content is stored as a proposed revision
3. Automated comparison identifies changed values and rules
4. AI summarizes the changes
5. Human reviewer approves or rejects the revision
6. Approved revision receives a version number
7. Calculation rules are updated separately
8. Regression tests run
9. New version is deployed
10. Users affected by material changes receive an explanation

Do not automatically publish financial rule changes based only on AI interpretation.

## 20. Recommendation Engine

Recommendations must combine deterministic rules and AI explanation.

### 20.1 Recommendation generation process

1. Run plan diagnostics
2. Identify measurable gaps
3. Match gaps to approved recommendation rules
4. Calculate estimated impact
5. Rank by relevance and impact
6. Have AI translate the approved recommendation into plain language
7. Present no more than three priority actions

### 20.2 Recommendation categories

Savings, Debt, CPP timing, OAS timing, Retirement age, Spending, Pension decisions, Tax planning, Account withdrawal order, Housing, Emergency savings, Plan completeness.

### 20.3 Recommendation controls

```
Rule identifier
Rule version
Reason triggered
Estimated impact
Confidence
Assumptions
Expiry date
Required professional review
```
