package com.retirewise.scenariocomparison.presentation

import com.retirewise.core.value.Money
import com.retirewise.scenariocomparison.domain.LeverInputValue
import com.retirewise.scenariocomparison.domain.ScenarioLever
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ScenarioLeverInputValueTest {
    @Test
    fun parsesAnAgeLeverAsAnAge() {
        assertEquals(LeverInputValue.Age(65), scenarioLeverInputValue(ScenarioLever.DELAY_CPP, "65"))
    }

    @Test
    fun parsesAMoneyLeverAsAnAmount() {
        assertEquals(
            LeverInputValue.Amount(Money.ofDollars(5000.0)),
            scenarioLeverInputValue(ScenarioLever.INCREASE_SAVINGS, "5000"),
        )
    }

    @Test
    fun blankOrUnparsableTextYieldsNull() {
        assertNull(scenarioLeverInputValue(ScenarioLever.DELAY_CPP, ""))
        assertNull(scenarioLeverInputValue(ScenarioLever.DELAY_CPP, "not a number"))
        assertNull(scenarioLeverInputValue(ScenarioLever.INCREASE_SAVINGS, "not a number"))
    }

    @Test
    fun unsupportedLeverAlwaysYieldsNull() {
        assertNull(scenarioLeverInputValue(ScenarioLever.PAY_OFF_MORTGAGE, "60"))
    }
}
