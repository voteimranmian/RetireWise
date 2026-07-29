package com.retirewise.scenarioengine.domain

import com.retirewise.core.value.Money
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ScenarioChangeSetTest {
    @Test
    fun allFieldsNullIsIdentity() {
        assertTrue(ScenarioChangeSet().isIdentity())
    }

    @Test
    fun anySingleNonNullFieldIsNotIdentity() {
        assertFalse(ScenarioChangeSet(retirementAge = 62).isIdentity())
        assertFalse(ScenarioChangeSet(cppStartAge = 70).isIdentity())
        assertFalse(ScenarioChangeSet(oasStartAge = 70).isIdentity())
        assertFalse(ScenarioChangeSet(employeeAnnualContribution = Money.ofDollars(1000.0)).isIdentity())
        assertFalse(ScenarioChangeSet(targetAnnualSpending = Money.ofDollars(1000.0)).isIdentity())
    }
}
