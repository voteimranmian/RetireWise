package com.retirewise.retirementengine.domain

import com.retirewise.core.value.Money
import com.retirewise.core.value.Rate
import com.retirewise.profile.domain.CanadianProvince
import com.retirewise.profile.domain.Profile
import com.retirewise.profile.domain.RetirementGoal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

private val ASSUMPTIONS =
    Assumptions(
        incomeGrowthRate = Rate.ZERO,
        expectedReturnRate = Rate.ZERO,
        inflationRate = Rate.ZERO,
        assumptionSetVersion = "TEST_V1",
    )

private val COMPLETE_PROFILE =
    Profile(
        age = 40,
        province = CanadianProvince.ONTARIO,
        annualIncome = 80000.0,
        retirementSavings = 100000.0,
        monthlyContribution = 500.0,
        expectedDebtAtRetirement = 20000.0,
    )

private val COMPLETE_GOAL =
    RetirementGoal(
        targetRetirementAge = 65,
        targetMonthlySpending = 4000.0,
    )

class ProjectionRequestTest {
    @Test
    fun completeProfileAndGoalProduceReady() {
        val request = toProjectionRequest(COMPLETE_PROFILE, COMPLETE_GOAL, ASSUMPTIONS)

        val ready = assertIs<ProjectionRequest.Ready>(request)
        assertEquals(40, ready.currentAge)
        assertEquals(65, ready.retirementAge)
        assertEquals(CanadianProvince.ONTARIO, ready.province)
        assertEquals(MaritalStatus.SINGLE, ready.maritalStatus)
        assertEquals(Money.ofDollars(80000.0), ready.employmentIncome)
        assertEquals(Money.ofDollars(100000.0), ready.startingBalances.rrspOrRrif)
        assertEquals(Money.ofDollars(6000.0), ready.employeeAnnualContribution) // 500 * 12
        assertEquals(Money.ofDollars(20000.0), ready.expectedDebtAtRetirement)
        assertEquals(Money.ofDollars(48000.0), ready.targetAnnualSpending) // 4000 * 12
        assertEquals(ASSUMPTIONS, ready.assumptions)
    }

    @Test
    fun missingDebtDefaultsToZeroRatherThanBlockingTheRequest() {
        val profileWithoutDebt = COMPLETE_PROFILE.copy(expectedDebtAtRetirement = null)

        val request = toProjectionRequest(profileWithoutDebt, COMPLETE_GOAL, ASSUMPTIONS)
        val ready = assertIs<ProjectionRequest.Ready>(request)

        assertEquals(Money.ZERO, ready.expectedDebtAtRetirement)
    }

    @Test
    fun missingRequiredFieldsProduceIncompleteWithAllMissingFieldNames() {
        val blankProfile = Profile()
        val blankGoal = RetirementGoal()

        val request = toProjectionRequest(blankProfile, blankGoal, ASSUMPTIONS)

        val incomplete = assertIs<ProjectionRequest.Incomplete>(request)
        val expectedMissingFields =
            setOf(
                "age",
                "targetRetirementAge",
                "annualIncome",
                "retirementSavings",
                "monthlyContribution",
                "targetMonthlySpending",
            )
        assertEquals(expectedMissingFields, incomplete.missingFields)
    }

    @Test
    fun oneMissingFieldStillProducesIncomplete() {
        val profileMissingIncome = COMPLETE_PROFILE.copy(annualIncome = null)

        val request = toProjectionRequest(profileMissingIncome, COMPLETE_GOAL, ASSUMPTIONS)

        val incomplete = assertIs<ProjectionRequest.Incomplete>(request)
        assertEquals(setOf("annualIncome"), incomplete.missingFields)
    }
}
