package com.retirewise.retirementengine.domain

import com.retirewise.core.value.Rate
import kotlin.test.Test
import kotlin.test.assertEquals

class AssumptionSetV1Test {
    @Test
    fun stampsTheExpectedVersionString() {
        assertEquals("DEFAULT_ASSUMPTIONS_V1", AssumptionSetV1.create().assumptionSetVersion)
    }

    @Test
    fun usesTheDocumentedRateValues() {
        val assumptions = AssumptionSetV1.create()

        assertEquals(Rate.ofPercent(2.0), assumptions.incomeGrowthRate)
        assertEquals(Rate.ofPercent(5.0), assumptions.expectedReturnRate)
        assertEquals(Rate.ofPercent(2.0), assumptions.inflationRate)
    }
}
