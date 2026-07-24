package com.retirewise.navigation

import kotlin.test.Test
import kotlin.test.assertEquals

class ExploreScreenTest {
    @Test
    fun containsAllScenarioTypesFromPrd() {
        assertEquals(
            listOf(
                "Retire earlier",
                "Delay retirement",
                "Delay CPP",
                "Delay OAS",
                "Increase savings",
                "Pay off mortgage",
                "Downsize home",
                "Work part time",
                "Change retirement spending",
            ),
            exploreScenarioTypes(),
        )
    }
}
