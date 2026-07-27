package com.retirewise.benefitsengine.domain.formula

import com.retirewise.benefitsengine.domain.BenefitRuleSet
import com.retirewise.core.value.Money
import com.retirewise.core.value.Rate

/** Formula identifier stamped into `CalculationMetadata.formulaIdentifiers`. */
const val FORMULA_BENEFIT_CPP_ACTUARIAL_ADJUSTMENT_V1 = "BENEFIT_CPP_ACTUARIAL_ADJUSTMENT_V1"

/**
 * Applies the CPP statutory actuarial adjustment to a user-supplied
 * age-65 estimate. Does not compute CPP from earnings history — that is
 * Release-two scope (docs/FINANCIAL_RULES.md section 11.6).
 *
 * [startAge] is clamped into `[cppMinStartAge, cppMaxStartAge]` (60-70)
 * rather than throwing, consistent with this codebase's existing
 * clamp-rather-than-throw convention (see `WithdrawalFormulas`).
 */
fun cppAdjustedAnnualAmount(
    estimateAtAge65: Money,
    startAge: Int,
    ruleSet: BenefitRuleSet,
): Money {
    val clampedStartAge = startAge.coerceIn(ruleSet.cppMinStartAge, ruleSet.cppMaxStartAge)
    val monthsFromStandardAge = (clampedStartAge - ruleSet.cppStandardAge) * 12

    val adjustmentRate =
        when {
            monthsFromStandardAge < 0 -> Rate(monthsFromStandardAge * ruleSet.cppEarlyReductionPerMonth.fraction)
            monthsFromStandardAge > 0 -> Rate(monthsFromStandardAge * ruleSet.cppLateIncreasePerMonth.fraction)
            else -> Rate.ZERO
        }

    return estimateAtAge65 + estimateAtAge65 * adjustmentRate
}
