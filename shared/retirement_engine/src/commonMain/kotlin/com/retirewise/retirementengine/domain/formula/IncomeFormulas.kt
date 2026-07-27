package com.retirewise.retirementengine.domain.formula

import com.retirewise.core.value.Money
import com.retirewise.core.value.Rate

const val FORMULA_INCOME_EMPLOYMENT_GROWTH_V1 = "INCOME_EMPLOYMENT_GROWTH_V1"
const val FORMULA_INCOME_PENSION_AND_OTHER_PASSTHROUGH_V1 = "INCOME_PENSION_AND_OTHER_PASSTHROUGH_V1"

/**
 * Employment income for the given year: grows by [incomeGrowthRate] while
 * working, and drops to zero at/after [retirementAge] — no phased or
 * part-time retirement modeling ("Part time employment scenarios" is
 * Release-two scope per docs/RELEASE_PLAN.md). Explicitly excludes
 * CPP/OAS/GIS, which are Phase 6.
 */
fun employmentIncomeForYear(
    baseAnnualIncome: Money,
    incomeGrowthRate: Rate,
    age: Int,
    retirementAge: Int,
    yearsFromStart: Int,
): Money {
    if (age >= retirementAge) return Money.ZERO
    return inflateAmount(baseAnnualIncome, incomeGrowthRate, yearsFromStart)
}

/**
 * Pension or "other retirement income" for the given year: zero before
 * [retirementAge], then a pass-through of [baseAnnualAmount] — optionally
 * indexed to inflation from *retirement* (`age - retirementAge`), not from
 * the plan's start year, since [baseAnnualAmount] is already expressed as of
 * the retirement date; indexing from years-since-plan-start would over-
 * inflate it by the years spent working before retirement.
 */
fun retirementIncomeForYear(
    baseAnnualAmount: Money,
    indexedToInflation: Boolean,
    inflationRate: Rate,
    age: Int,
    retirementAge: Int,
): Money {
    if (age < retirementAge) return Money.ZERO
    if (!indexedToInflation) return baseAnnualAmount
    return inflateAmount(baseAnnualAmount, inflationRate, age - retirementAge)
}
