package com.retirewise.retirementengine.domain.formula

import com.retirewise.core.value.Money
import com.retirewise.core.value.Rate
import kotlin.math.pow

const val FORMULA_INFLATION_CPI_INDEXED_V1 = "INFLATION_CPI_INDEXED_V1"

/**
 * Compounds [baseAmount] forward by [yearsFromStart] years at [rate],
 * measured from the projection's first year (not from retirement), so
 * real-vs-nominal comparisons stay consistent across the whole projection.
 */
fun inflateAmount(
    baseAmount: Money,
    rate: Rate,
    yearsFromStart: Int,
): Money {
    val factor = (1.0 + rate.fraction).pow(yearsFromStart)
    return Money.ofDollars(baseAmount.toDollarDouble() * factor)
}
