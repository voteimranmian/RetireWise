package com.retirewise.retirementengine.domain.formula

import com.retirewise.core.value.Money
import com.retirewise.core.value.Rate
import kotlin.test.Test
import kotlin.test.assertEquals

class InflationFormulasTest {
    @Test
    fun zeroYearsFromStartReturnsBaseAmountUnchanged() {
        val result = inflateAmount(Money.ofDollars(1000.0), Rate.ofPercent(2.0), yearsFromStart = 0)

        assertEquals(Money.ofDollars(1000.0), result)
    }

    @Test
    fun compoundsAnnuallyOverMultipleYears() {
        // 1000 * 1.02^2 = 1040.40
        val result = inflateAmount(Money.ofDollars(1000.0), Rate.ofPercent(2.0), yearsFromStart = 2)

        assertEquals(Money.ofDollars(1040.40), result)
    }

    @Test
    fun zeroRateNeverInflatesRegardlessOfYears() {
        val result = inflateAmount(Money.ofDollars(1000.0), Rate.ZERO, yearsFromStart = 10)

        assertEquals(Money.ofDollars(1000.0), result)
    }
}
