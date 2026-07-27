package com.retirewise.benefitsengine.domain

import com.retirewise.benefitsengine.domain.formula.gisAnnualAmount
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BenefitRuleRepositoryTest {
    @Test
    fun currentStampsTheExpectedRuleVersion() {
        assertEquals("CANADA_BENEFITS_V1", CanadaBenefitRuleRepositoryV1.current().ruleVersion)
    }

    @Test
    fun gisAnchorPointsAreInternallyConsistent() {
        val ruleSet = CanadaBenefitRuleRepositoryV1.current()

        // By construction, GIS must be exactly zero right at the published
        // income cutoff — the two calibration anchor points must agree.
        assertEquals(0L, gisAnnualAmount(ruleSet.gisIncomeCutoffSingle, ruleSet).minorUnits)
    }

    @Test
    fun sourcedRatesAreNonNegative() {
        val ruleSet = CanadaBenefitRuleRepositoryV1.current()

        assertTrue(ruleSet.cppEarlyReductionPerMonth.fraction >= 0.0)
        assertTrue(ruleSet.cppLateIncreasePerMonth.fraction >= 0.0)
        assertTrue(ruleSet.oasLateIncreasePerMonth.fraction >= 0.0)
        assertTrue(ruleSet.gisMaxAnnualAmountSingle.minorUnits >= 0L)
        assertTrue(ruleSet.gisIncomeCutoffSingle.minorUnits >= 0L)
    }
}
