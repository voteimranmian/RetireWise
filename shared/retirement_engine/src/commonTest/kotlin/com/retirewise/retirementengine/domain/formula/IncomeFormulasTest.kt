package com.retirewise.retirementengine.domain.formula

import com.retirewise.core.value.Money
import com.retirewise.core.value.Rate
import kotlin.test.Test
import kotlin.test.assertEquals

class IncomeFormulasTest {
    @Test
    fun employmentIncomeGrowsWhileWorking() {
        val result =
            employmentIncomeForYear(
                baseAnnualIncome = Money.ofDollars(80000.0),
                incomeGrowthRate = Rate.ofPercent(3.0),
                age = 45,
                retirementAge = 65,
                yearsFromStart = 5,
            )

        assertEquals(inflateAmount(Money.ofDollars(80000.0), Rate.ofPercent(3.0), 5), result)
    }

    @Test
    fun employmentIncomeIsZeroAtRetirementAge() {
        val result =
            employmentIncomeForYear(
                baseAnnualIncome = Money.ofDollars(80000.0),
                incomeGrowthRate = Rate.ofPercent(3.0),
                age = 65,
                retirementAge = 65,
                yearsFromStart = 25,
            )

        assertEquals(Money.ZERO, result)
    }

    @Test
    fun employmentIncomeIsZeroAfterRetirementAge() {
        val result =
            employmentIncomeForYear(
                baseAnnualIncome = Money.ofDollars(80000.0),
                incomeGrowthRate = Rate.ofPercent(3.0),
                age = 70,
                retirementAge = 65,
                yearsFromStart = 30,
            )

        assertEquals(Money.ZERO, result)
    }

    @Test
    fun retirementIncomeIsZeroBeforeRetirement() {
        val result =
            retirementIncomeForYear(
                baseAnnualAmount = Money.ofDollars(24000.0),
                indexedToInflation = true,
                inflationRate = Rate.ofPercent(2.0),
                age = 64,
                retirementAge = 65,
            )

        assertEquals(Money.ZERO, result)
    }

    @Test
    fun nonIndexedRetirementIncomeIsAFlatPassThroughForever() {
        val atRetirement =
            retirementIncomeForYear(
                baseAnnualAmount = Money.ofDollars(24000.0),
                indexedToInflation = false,
                inflationRate = Rate.ofPercent(2.0),
                age = 65,
                retirementAge = 65,
            )
        val yearsLater =
            retirementIncomeForYear(
                baseAnnualAmount = Money.ofDollars(24000.0),
                indexedToInflation = false,
                inflationRate = Rate.ofPercent(2.0),
                age = 85,
                retirementAge = 65,
            )

        assertEquals(Money.ofDollars(24000.0), atRetirement)
        assertEquals(Money.ofDollars(24000.0), yearsLater)
    }

    @Test
    fun indexedRetirementIncomeIsUnindexedInTheRetirementYearItself() {
        // Regression test: pensionIncomeAtRetirement is already expressed as of
        // retirement, so it must not be inflated by years spent working before
        // retirement — only by years spent *in* retirement.
        val result =
            retirementIncomeForYear(
                baseAnnualAmount = Money.ofDollars(24000.0),
                indexedToInflation = true,
                inflationRate = Rate.ofPercent(2.0),
                age = 65,
                retirementAge = 65,
            )

        assertEquals(Money.ofDollars(24000.0), result)
    }

    @Test
    fun indexedRetirementIncomeCompoundsFromYearsSinceRetirementOnly() {
        val result =
            retirementIncomeForYear(
                baseAnnualAmount = Money.ofDollars(24000.0),
                indexedToInflation = true,
                inflationRate = Rate.ofPercent(2.0),
                age = 70,
                retirementAge = 65,
            )

        assertEquals(inflateAmount(Money.ofDollars(24000.0), Rate.ofPercent(2.0), yearsFromStart = 5), result)
    }
}
