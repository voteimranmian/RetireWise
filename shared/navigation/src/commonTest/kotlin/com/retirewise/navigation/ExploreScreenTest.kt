package com.retirewise.navigation

import com.retirewise.scenariocomparison.domain.ScenarioLever
import com.retirewise.scenariocomparison.domain.isSupported
import com.retirewise.scenariocomparison.presentation.scenarioLeverIcon
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ExploreScreenTest {
    @Test
    fun containsAllNineScenarioTypesFromPrd() {
        assertEquals(9, ScenarioLever.entries.size)
    }

    @Test
    fun sixLeversAreSupportedAndThreeAreNot() {
        val supportedCount = ScenarioLever.entries.count { it.isSupported() }
        val unsupportedCount = ScenarioLever.entries.count { !it.isSupported() }

        assertEquals(6, supportedCount)
        assertEquals(3, unsupportedCount)
        assertTrue(ScenarioLever.PAY_OFF_MORTGAGE.let { !it.isSupported() })
        assertTrue(ScenarioLever.DOWNSIZE_HOME.let { !it.isSupported() })
        assertTrue(ScenarioLever.WORK_PART_TIME.let { !it.isSupported() })
    }

    @Test
    fun everyLeverHasAMappedIcon() {
        // scenarioLeverIcon(lever) is exhaustive over ScenarioLever and would
        // fail to compile if a lever were unmapped; this just confirms it's
        // callable for every entry without throwing.
        ScenarioLever.entries.forEach { lever -> scenarioLeverIcon(lever) }
    }
}
