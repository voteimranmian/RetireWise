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

class CppBenefitFormulasTest {
    @Test
    fun standardAgeAppliesNoAdjustment() {
        val result = cppAdjustedAnnualAmount(Money.ofDollars(10000.0), startAge = 65, TEST_RULE_SET)

        assertEquals(Money.ofDollars(10000.0), result)
    }

    @Test
    fun earlyStartReducesAmount() {
        // 60 months early * 0.6%/month = 36% reduction.
        val result = cppAdjustedAnnualAmount(Money.ofDollars(10000.0), startAge = 60, TEST_RULE_SET)

        assertEquals(Money.ofDollars(6400.0), result)
    }

    @Test
    fun lateStartIncreasesAmount() {
        // 60 months late * 0.7%/month = 42% increase.
        val result = cppAdjustedAnnualAmount(Money.ofDollars(10000.0), startAge = 70, TEST_RULE_SET)

        assertEquals(Money.ofDollars(14200.0), result)
    }

    @Test
    fun startAgeBelowMinimumIsClampedToMinimum() {
        val belowMin = cppAdjustedAnnualAmount(Money.ofDollars(10000.0), startAge = 50, TEST_RULE_SET)
        val atMin = cppAdjustedAnnualAmount(Money.ofDollars(10000.0), startAge = 60, TEST_RULE_SET)

        assertEquals(atMin, belowMin)
    }

    @Test
    fun startAgeAboveMaximumIsClampedToMaximum() {
        val aboveMax = cppAdjustedAnnualAmount(Money.ofDollars(10000.0), startAge = 80, TEST_RULE_SET)
        val atMax = cppAdjustedAnnualAmount(Money.ofDollars(10000.0), startAge = 70, TEST_RULE_SET)

        assertEquals(atMax, aboveMax)
    }
}
