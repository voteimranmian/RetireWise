package com.retirewise.benefitsengine.domain.formula

import com.retirewise.benefitsengine.domain.BenefitRuleSet
import com.retirewise.core.value.Money
import com.retirewise.core.value.Rate
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

private val TEST_RULE_SET =
    BenefitRuleSet(
        ruleVersion = "TEST_V1",
        sourceDescription = "test fixture",
        sourceVerifiedDate = LocalDate(2026, 1, 1),
        cppEarlyReductionPerMonth = Rate.ofPercent(0.6),
        cppLateIncreasePerMonth = Rate.ofPercent(0.7),
        oasLateIncreasePerMonth = Rate.ofPercent(0.6),
        gisMaxAnnualAmountSingle = Money.ofDollars(12000.0),
        gisIncomeCutoffSingle = Money.ofDollars(24000.0),
    )

class GisBenefitFormulasTest {
    @Test
    fun zeroIncomeReturnsMaximumAmount() {
        val result = gisAnnualAmount(Money.ZERO, TEST_RULE_SET)

        assertEquals(Money.ofDollars(12000.0), result)
    }

    @Test
    fun incomeAtCutoffReturnsZero() {
        val result = gisAnnualAmount(Money.ofDollars(24000.0), TEST_RULE_SET)

        assertEquals(Money.ZERO, result)
    }

    @Test
    fun incomeAboveCutoffClampsAtZeroRatherThanGoingNegative() {
        val result = gisAnnualAmount(Money.ofDollars(50000.0), TEST_RULE_SET)

        assertEquals(Money.ZERO, result)
    }

    @Test
    fun incomeBetweenAnchorsIsLinear() {
        // Halfway to the cutoff => half the maximum GIS remains.
        val result = gisAnnualAmount(Money.ofDollars(12000.0), TEST_RULE_SET)

        assertEquals(Money.ofDollars(6000.0), result)
    }
}
