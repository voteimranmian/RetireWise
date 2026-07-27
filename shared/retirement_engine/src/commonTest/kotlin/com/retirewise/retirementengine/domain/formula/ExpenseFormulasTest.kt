package com.retirewise.retirementengine.domain.formula

import com.retirewise.core.value.Money
import com.retirewise.core.value.Rate
import kotlin.test.Test
import kotlin.test.assertEquals

class ExpenseFormulasTest {
    @Test
    fun beforeRetirementAgeExpensesAreZero() {
        val result =
            retirementExpensesForYear(
                baseAnnualSpending = Money.ofDollars(40000.0),
                inflationRate = Rate.ofPercent(2.0),
                age = 64,
                retirementAge = 65,
                yearsFromStart = 29,
            )

        assertEquals(Money.ZERO, result)
    }

    @Test
    fun atRetirementAgeExpensesAreInflatedFromPlanStart() {
        val result =
            retirementExpensesForYear(
                baseAnnualSpending = Money.ofDollars(40000.0),
                inflationRate = Rate.ofPercent(2.0),
                age = 65,
                retirementAge = 65,
                yearsFromStart = 30,
            )

        assertEquals(inflateAmount(Money.ofDollars(40000.0), Rate.ofPercent(2.0), 30), result)
    }

    @Test
    fun afterRetirementAgeContinuesCompoundingFromPlanStart() {
        val result =
            retirementExpensesForYear(
                baseAnnualSpending = Money.ofDollars(40000.0),
                inflationRate = Rate.ofPercent(2.0),
                age = 70,
                retirementAge = 65,
                yearsFromStart = 35,
            )

        assertEquals(inflateAmount(Money.ofDollars(40000.0), Rate.ofPercent(2.0), 35), result)
    }
}
