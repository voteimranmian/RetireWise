package com.retirewise.retirementengine.domain

import com.retirewise.core.value.Money
import com.retirewise.core.value.Rate

/**
 * The projection assumptions from docs/FINANCIAL_RULES.md section 11.2 that
 * [com.retirewise.profile.domain.Profile] and
 * [com.retirewise.profile.domain.RetirementGoal] don't already collect.
 *
 * There is deliberately no default/factory instance with baked-in numeric
 * rates (e.g. "assume 6% growth"). Hardcoding an unversioned, unsourced
 * assumption set into application code is exactly what CLAUDE.md rules 5-6
 * warn against by extension. Every caller must supply explicit, sourced
 * values — this is a known limitation until a real, approved default
 * assumption set exists (see docs/RELEASE_PLAN.md Phase 5 status).
 */
data class Assumptions(
    val incomeGrowthRate: Rate,
    val expectedReturnRate: Rate,
    val inflationRate: Rate,
    val assumptionSetVersion: String,
    val projectionEndAge: Int = DEFAULT_PROJECTION_END_AGE,
    val employerAnnualContribution: Money = Money.ZERO,
    val pensionIncomeAtRetirement: Money = Money.ZERO,
    val pensionIndexedToInflation: Boolean = false,
    val otherRetirementIncome: Money = Money.ZERO,
    // CPP/OAS timing (Phase 6). All four remain nullable/defaulted so
    // omitting them (as every Phase 5 caller does) leaves
    // `AnnualProjectionEntry.governmentBenefits` as `NotYetModeled` rather
    // than fabricating a benefit amount. The *amount* fields are the
    // caller's own sourced age-65 estimate — this engine never invents a
    // CPP/OAS dollar figure — and only the statutory timing adjustment
    // (docs/ADR/0007) is computed from it.
    val cppStartAge: Int? = null,
    val oasStartAge: Int? = null,
    val estimatedCppAmountAtAge65: Money? = null,
    val estimatedOasAmountAtAge65: Money? = null,
) {
    companion object {
        const val DEFAULT_PROJECTION_END_AGE = 100
    }
}
