package com.retirewise.scenariocomparison.domain

import com.retirewise.core.value.Money
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ScenarioLeverTest {
    @Test
    fun sixOfNineLeversAreSupported() {
        val supported =
            setOf(
                ScenarioLever.RETIRE_EARLIER,
                ScenarioLever.DELAY_RETIREMENT,
                ScenarioLever.DELAY_CPP,
                ScenarioLever.DELAY_OAS,
                ScenarioLever.INCREASE_SAVINGS,
                ScenarioLever.CHANGE_RETIREMENT_SPENDING,
            )
        val unsupported =
            setOf(ScenarioLever.PAY_OFF_MORTGAGE, ScenarioLever.DOWNSIZE_HOME, ScenarioLever.WORK_PART_TIME)

        ScenarioLever.entries.forEach { lever ->
            assertEquals(lever in supported, lever.isSupported(), "unexpected support flag for $lever")
        }
        assertEquals(9, supported.size + unsupported.size)
    }

    @Test
    fun retireEarlierAndDelayRetirementMapToRetirementAge() {
        assertEquals(
            60,
            buildChangeSet(ScenarioLever.RETIRE_EARLIER, LeverInputValue.Age(60)).retirementAge,
        )
        assertEquals(
            70,
            buildChangeSet(ScenarioLever.DELAY_RETIREMENT, LeverInputValue.Age(70)).retirementAge,
        )
    }

    @Test
    fun delayCppMapsToCppStartAgeOnly() {
        val changeSet = buildChangeSet(ScenarioLever.DELAY_CPP, LeverInputValue.Age(70))
        assertEquals(70, changeSet.cppStartAge)
        assertEquals(null, changeSet.retirementAge)
        assertEquals(null, changeSet.oasStartAge)
        assertEquals(null, changeSet.employeeAnnualContribution)
        assertEquals(null, changeSet.targetAnnualSpending)
    }

    @Test
    fun delayOasMapsToOasStartAgeOnly() {
        val changeSet = buildChangeSet(ScenarioLever.DELAY_OAS, LeverInputValue.Age(70))
        assertEquals(70, changeSet.oasStartAge)
        assertTrue(changeSet.retirementAge == null && changeSet.cppStartAge == null)
    }

    @Test
    fun increaseSavingsMapsToEmployeeAnnualContributionOnly() {
        val amount = Money.ofDollars(5000.0)
        val changeSet = buildChangeSet(ScenarioLever.INCREASE_SAVINGS, LeverInputValue.Amount(amount))
        assertEquals(amount, changeSet.employeeAnnualContribution)
        assertEquals(null, changeSet.targetAnnualSpending)
    }

    @Test
    fun changeRetirementSpendingMapsToTargetAnnualSpendingOnly() {
        val amount = Money.ofDollars(40000.0)
        val changeSet = buildChangeSet(ScenarioLever.CHANGE_RETIREMENT_SPENDING, LeverInputValue.Amount(amount))
        assertEquals(amount, changeSet.targetAnnualSpending)
        assertEquals(null, changeSet.employeeAnnualContribution)
    }

    @Test
    fun unsupportedLeverThrows() {
        assertFails { buildChangeSet(ScenarioLever.PAY_OFF_MORTGAGE, LeverInputValue.Age(60)) }
    }

    @Test
    fun mismatchedInputKindThrows() {
        assertFails { buildChangeSet(ScenarioLever.RETIRE_EARLIER, LeverInputValue.Amount(Money.ZERO)) }
        assertFails { buildChangeSet(ScenarioLever.INCREASE_SAVINGS, LeverInputValue.Age(60)) }
    }

    @Test
    fun everyLeverHasTheDocumentedInputKind() {
        assertEquals(LeverInputKind.AGE, ScenarioLever.RETIRE_EARLIER.inputKind)
        assertEquals(LeverInputKind.AGE, ScenarioLever.DELAY_RETIREMENT.inputKind)
        assertEquals(LeverInputKind.AGE, ScenarioLever.DELAY_CPP.inputKind)
        assertEquals(LeverInputKind.AGE, ScenarioLever.DELAY_OAS.inputKind)
        assertEquals(LeverInputKind.MONEY, ScenarioLever.INCREASE_SAVINGS.inputKind)
        assertEquals(LeverInputKind.MONEY, ScenarioLever.CHANGE_RETIREMENT_SPENDING.inputKind)
        assertFalse(ScenarioLever.PAY_OFF_MORTGAGE.isSupported())
        assertFalse(ScenarioLever.DOWNSIZE_HOME.isSupported())
        assertFalse(ScenarioLever.WORK_PART_TIME.isSupported())
    }
}
