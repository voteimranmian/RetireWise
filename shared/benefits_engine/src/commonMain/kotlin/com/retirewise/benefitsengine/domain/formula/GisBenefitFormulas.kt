package com.retirewise.benefitsengine.domain.formula

import com.retirewise.benefitsengine.domain.BenefitRuleSet
import com.retirewise.core.value.Money
import com.retirewise.core.value.Rate

/** Formula identifier stamped into `CalculationMetadata.formulaIdentifiers`. */
const val FORMULA_BENEFIT_GIS_LINEAR_APPROXIMATION_V1 = "BENEFIT_GIS_LINEAR_APPROXIMATION_V1"

/**
 * Estimates the annual GIS (single-pensioner rate) using a straight line
 * between the two published anchor points: the maximum amount at zero
 * non-OAS income, and zero GIS at the official income cutoff. The real GIS
 * reduction schedule is a piecewise-banded table, not a straight line —
 * this is a documented MVP approximation
 * (docs/ADR/0007-government-benefit-scope-and-sourcing-for-phase-6.md).
 *
 * GIS is only payable to OAS recipients; callers must not invoke this
 * before OAS has started. [nonOasIncome] should exclude OAS itself and
 * (per this MVP's documented simplification) excludes account withdrawals.
 * Clamped at zero — never negative.
 */
fun gisAnnualAmount(
    nonOasIncome: Money,
    ruleSet: BenefitRuleSet,
): Money {
    val reductionRate =
        Rate(ruleSet.gisMaxAnnualAmountSingle.toDollarDouble() / ruleSet.gisIncomeCutoffSingle.toDollarDouble())
    val reduction = nonOasIncome * reductionRate

    return (ruleSet.gisMaxAnnualAmountSingle - reduction).coerceAtLeast(Money.ZERO)
}
