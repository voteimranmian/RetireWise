package com.retirewise.retirementengine.domain

import com.retirewise.core.value.Money

/**
 * One row of an annual projection (docs/FINANCIAL_RULES.md section 11.3),
 * field-by-field. [grossIncome], [estimatedTaxes], and [afterTaxIncome] are
 * always [ProjectionValue.NotYetModeled] — no tax engine exists yet.
 * [governmentBenefits] is [ProjectionValue.Known] once at least one of
 * `Assumptions.estimatedCppAmountAtAge65`/`estimatedOasAmountAtAge65` is
 * supplied (Phase 6), otherwise it remains `NotYetModeled` — see
 * [ProjectionValue]'s KDoc for why the whole aggregate is marked rather
 * than a partial total.
 */
data class AnnualProjectionEntry(
    val age: Int,
    val calendarYear: Int,
    val employmentIncome: Money,
    val governmentBenefits: ProjectionValue,
    val pensionIncome: Money,
    val accountWithdrawals: Money,
    val otherIncome: Money,
    val grossIncome: ProjectionValue,
    val estimatedTaxes: ProjectionValue,
    val afterTaxIncome: ProjectionValue,
    val expenses: Money,
    /**
     * Computed from known income sources only — pre-tax:
     * `employmentIncome + pensionIncome + otherIncome + accountWithdrawals -
     * expenses`, **plus** the CPP/OAS/GIS total whenever [governmentBenefits]
     * is [ProjectionValue.Known]. Still excludes government benefits when
     * they haven't been supplied, and always excludes tax — results look
     * more conservative (a larger apparent shortfall) than reality until a
     * future tax engine lands.
     */
    val savingsSurplusOrDeficit: Money,
    val rrspOrRrifBalance: Money,
    val tfsaBalance: Money,
    val nonRegisteredBalance: Money,
    /**
     * Carried flat from [com.retirewise.profile.domain.Profile.expectedDebtAtRetirement]
     * for every projection year — onboarding collects only a single point
     * value, not a current balance/rate/payment schedule, so there is no
     * real amortization curve to compute yet. Known MVP simplification.
     */
    val debtBalance: Money,
    val netWorth: Money,
    val estateValue: Money,
    val planStatus: PlanStatus,
)
