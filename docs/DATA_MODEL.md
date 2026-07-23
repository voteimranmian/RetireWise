# RetireWise — Data Model

Not implemented in Phase 0. This document defines the primary entities that will be built starting Phase 3 (profile) through Phase 7 (scenarios).

## 16.1 User

```
UserId
PreferredName
Email
Language
Province
TimeZone
CreatedAt
UpdatedAt
ConsentVersion
PrivacySettings
```

## 16.2 Household

```
HouseholdId
PlanningMode
PrimaryUserId
PartnerUserId
Dependants
```

## 16.3 RetirementGoal

```
GoalId
TargetRetirementAge
TargetMonthlySpending
DesiredLifestyle
Priority
LegacyGoal
TravelGoal
HousingGoal
```

## 16.4 IncomeSource

```
IncomeId
Type
CurrentAnnualAmount
StartDate
EndDate
GrowthRate
TaxTreatment
Owner
```

## 16.5 FinancialAccount

```
AccountId
AccountType
Owner
CurrentBalance
AnnualContribution
EmployerContribution
ExpectedReturn
TaxTreatment
InstitutionNameOptional
```

Account types: `RRSP`, `TFSA`, `FHSA`, `RRIF`, `LIRA`, `LIF`, Defined contribution pension, Non registered account, Cash, Other.

## 16.6 Pension

```
PensionId
PensionType
Owner
EstimatedAnnualBenefit
StartAge
IndexationRate
BridgeBenefit
SurvivorPercentage
Source
```

## 16.7 Property

```
PropertyId
PropertyType
CurrentValue
MortgageBalance
MortgageRate
Payment
ExpectedSaleAge
SaleCosts
FutureHousingPlan
```

## 16.8 Liability

```
LiabilityId
Type
Balance
InterestRate
Payment
ExpectedPayoffDate
```

## 16.9 Expense

```
ExpenseId
Category
CurrentMonthlyAmount
RetirementMonthlyAmount
StartAge
EndAge
InflationRate
Essential
```

## 16.10 Scenario

```
ScenarioId
Name
BasePlanId
ChangedAssumptions
CalculationVersion
CreatedAt
ProjectionSummary
```

## 16.11 Recommendation

```
RecommendationId
Type
Title
Explanation
EstimatedImpact
Priority
Confidence
Assumptions
Risks
Status
CreatedAt
ExpiresAt
```

## 16.12 GovernmentProgram

See `FINANCIAL_RULES.md` section 13.2 for fields.

## 16.13 Conversation

```
ConversationId
UserId
Title
Summary
CreatedAt
UpdatedAt
RetentionSetting
```

## 16.14 Message

```
MessageId
ConversationId
Role
Content
StructuredResponse
ToolCalls
SourceIds
CreatedAt
```

Do not store raw model reasoning. Store only user visible responses, tool inputs, tool outputs, sources, and operational metadata required for auditing.
