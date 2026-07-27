package com.retirewise.retirementengine.domain.formula

import com.retirewise.core.value.Money
import com.retirewise.core.value.Rate

const val FORMULA_EXPENSE_INFLATED_RETIREMENT_SPENDING_V1 = "EXPENSE_INFLATED_RETIREMENT_SPENDING_V1"

/**
 * Retirement spending for the given year: zero before [retirementAge], then
 * [baseAnnualSpending] compounded by [inflationRate] from the plan's start
 * year (see [inflateAmount]) from [retirementAge] onward.
 */
fun retirementExpensesForYear(
    baseAnnualSpending: Money,
    inflationRate: Rate,
    age: Int,
    retirementAge: Int,
    yearsFromStart: Int,
): Money {
    if (age < retirementAge) return Money.ZERO
    return inflateAmount(baseAnnualSpending, inflationRate, yearsFromStart)
}
