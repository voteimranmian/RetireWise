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

class OasBenefitFormulasTest {
    @Test
    fun standardAgeAppliesNoAdjustment() {
        val result = oasAdjustedAnnualAmount(Money.ofDollars(8000.0), startAge = 65, TEST_RULE_SET)

        assertEquals(Money.ofDollars(8000.0), result)
    }

    @Test
    fun deferralIncreasesAmount() {
        // 60 months deferred * 0.6%/month = 36% increase.
        val result = oasAdjustedAnnualAmount(Money.ofDollars(8000.0), startAge = 70, TEST_RULE_SET)

        assertEquals(Money.ofDollars(10880.0), result)
    }

    @Test
    fun startAgeBelowStandardIsClampedToStandardAge() {
        // OAS has no early-start option: an age below 65 is treated as 65.
        val belowStandard = oasAdjustedAnnualAmount(Money.ofDollars(8000.0), startAge = 60, TEST_RULE_SET)
        val atStandard = oasAdjustedAnnualAmount(Money.ofDollars(8000.0), startAge = 65, TEST_RULE_SET)

        assertEquals(atStandard, belowStandard)
    }

    @Test
    fun startAgeAboveMaximumIsClampedToMaximum() {
        val aboveMax = oasAdjustedAnnualAmount(Money.ofDollars(8000.0), startAge = 80, TEST_RULE_SET)
        val atMax = oasAdjustedAnnualAmount(Money.ofDollars(8000.0), startAge = 70, TEST_RULE_SET)

        assertEquals(atMax, aboveMax)
    }
}
