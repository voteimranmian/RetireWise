package com.retirewise.scenariocomparison.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Work
import com.retirewise.scenariocomparison.domain.ScenarioLever
import kotlin.test.Test
import kotlin.test.assertEquals

class ScenarioLeverIconTest {
    @Test
    fun everyLeverHasAMappedIcon() {
        val expectedIcons =
            mapOf(
                ScenarioLever.RETIRE_EARLIER to Icons.Filled.EventAvailable,
                ScenarioLever.DELAY_RETIREMENT to Icons.Filled.AccessTime,
                ScenarioLever.DELAY_CPP to Icons.Filled.AccountBalance,
                ScenarioLever.DELAY_OAS to Icons.Filled.AccountBalanceWallet,
                ScenarioLever.INCREASE_SAVINGS to Icons.Filled.TrendingUp,
                ScenarioLever.PAY_OFF_MORTGAGE to Icons.Filled.Payments,
                ScenarioLever.DOWNSIZE_HOME to Icons.Filled.Home,
                ScenarioLever.WORK_PART_TIME to Icons.Filled.Work,
                ScenarioLever.CHANGE_RETIREMENT_SPENDING to Icons.Filled.SwapHoriz,
            )

        ScenarioLever.entries.forEach { lever ->
            assertEquals(expectedIcons.getValue(lever), scenarioLeverIcon(lever))
        }
    }
}
