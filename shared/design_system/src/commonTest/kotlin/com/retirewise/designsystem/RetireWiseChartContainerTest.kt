package com.retirewise.designsystem

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RetireWiseChartContainerTest {
    @Test
    fun bothActionsHiddenWhenNoCallbacksProvided() {
        val actions = retireWiseChartActions(onViewAssumptionsClick = null, onViewDataTableClick = null)

        assertFalse(actions.showAssumptionsAction)
        assertFalse(actions.showDataTableAction)
    }

    @Test
    fun onlyAssumptionsActionShownWhenOnlyThatCallbackProvided() {
        val actions = retireWiseChartActions(onViewAssumptionsClick = {}, onViewDataTableClick = null)

        assertTrue(actions.showAssumptionsAction)
        assertFalse(actions.showDataTableAction)
    }

    @Test
    fun onlyDataTableActionShownWhenOnlyThatCallbackProvided() {
        val actions = retireWiseChartActions(onViewAssumptionsClick = null, onViewDataTableClick = {})

        assertFalse(actions.showAssumptionsAction)
        assertTrue(actions.showDataTableAction)
    }

    @Test
    fun bothActionsShownWhenBothCallbacksProvided() {
        val actions = retireWiseChartActions(onViewAssumptionsClick = {}, onViewDataTableClick = {})

        assertTrue(actions.showAssumptionsAction)
        assertTrue(actions.showDataTableAction)
    }
}
