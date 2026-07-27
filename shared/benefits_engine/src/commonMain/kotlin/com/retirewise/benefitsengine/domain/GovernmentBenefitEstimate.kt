package com.retirewise.benefitsengine.domain

import com.retirewise.core.value.Money

/** One year's combined government benefit estimate, plus the rule version that produced it. */
data class GovernmentBenefitEstimate(
    val cppAnnual: Money,
    val oasAnnual: Money,
    val gisAnnual: Money,
    val ruleVersion: String,
) {
    val totalAnnual: Money get() = cppAnnual + oasAnnual + gisAnnual
}
