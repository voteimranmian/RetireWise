package com.retirewise.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Work
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

    @Test
    fun everyScenarioTypeHasAMappedIcon() {
        val expectedIcons =
            mapOf(
                "Retire earlier" to Icons.Filled.EventAvailable,
                "Delay retirement" to Icons.Filled.AccessTime,
                "Delay CPP" to Icons.Filled.AccountBalance,
                "Delay OAS" to Icons.Filled.AccountBalanceWallet,
                "Increase savings" to Icons.Filled.TrendingUp,
                "Pay off mortgage" to Icons.Filled.Payments,
                "Downsize home" to Icons.Filled.Home,
                "Work part time" to Icons.Filled.Work,
                "Change retirement spending" to Icons.Filled.SwapHoriz,
            )

        exploreScenarioTypes().forEach { scenarioType ->
            assertEquals(expectedIcons.getValue(scenarioType), exploreScenarioIcon(scenarioType))
        }
    }

    @Test
    fun unknownScenarioTypeFallsBackToGenericIcon() {
        assertEquals(Icons.Filled.Description, exploreScenarioIcon("Something new"))
    }
}
