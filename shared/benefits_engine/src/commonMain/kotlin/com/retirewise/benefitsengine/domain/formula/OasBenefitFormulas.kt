package com.retirewise.benefitsengine.domain.formula

import com.retirewise.benefitsengine.domain.BenefitRuleSet
import com.retirewise.core.value.Money
import com.retirewise.core.value.Rate

/** Formula identifier stamped into `CalculationMetadata.formulaIdentifiers`. */
const val FORMULA_BENEFIT_OAS_DEFERRAL_V1 = "BENEFIT_OAS_DEFERRAL_V1"

/**
 * Applies the OAS statutory deferral increase to a user-supplied age-65
 * estimate. Unlike CPP, OAS has no early-start option in reality — [startAge]
 * is clamped into `[oasStandardAge, oasMaxStartAge]` (65-70), so a value
 * below 65 is treated as 65 (no adjustment) rather than throwing.
 */
fun oasAdjustedAnnualAmount(
    estimateAtAge65: Money,
    startAge: Int,
    ruleSet: BenefitRuleSet,
): Money {
    val clampedStartAge = startAge.coerceIn(ruleSet.oasStandardAge, ruleSet.oasMaxStartAge)
    val monthsDeferred = (clampedStartAge - ruleSet.oasStandardAge) * 12

    val adjustmentRate = Rate(monthsDeferred * ruleSet.oasLateIncreasePerMonth.fraction)

    return estimateAtAge65 + estimateAtAge65 * adjustmentRate
}
