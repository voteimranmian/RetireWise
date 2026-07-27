package com.retirewise.profile.data

import com.retirewise.profile.domain.CanadianProvince
import com.retirewise.profile.domain.Profile
import com.retirewise.profile.domain.RetirementGoal
import com.retirewise.profile.domain.RetirementPriority
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class InMemoryProfileRepositoryTest {
    @Test
    fun profileAndGoalAreNullBeforeAnySave() =
        runBlocking {
            val repository = InMemoryProfileRepository()

            assertNull(repository.current())
            assertNull(repository.currentGoal())
        }

    @Test
    fun savedProfileAndGoalAreReturnedByReads() =
        runBlocking {
            val repository = InMemoryProfileRepository()
            val profile = Profile(age = 45, province = CanadianProvince.ONTARIO)
            val goal =
                RetirementGoal(
                    targetRetirementAge = 65,
                    targetMonthlySpending = 3000.0,
                    priorities = setOf(RetirementPriority.TRAVEL, RetirementPriority.FAMILY),
                )

            repository.save(profile, goal)

            assertEquals(profile, repository.current())
            assertEquals(goal, repository.currentGoal())
        }

    @Test
    fun savingAgainOverwritesThePreviousProfileAndGoal() =
        runBlocking {
            val repository = InMemoryProfileRepository()
            repository.save(Profile(age = 30), RetirementGoal(targetRetirementAge = 60))

            val updatedProfile = Profile(age = 31)
            val updatedGoal = RetirementGoal(targetRetirementAge = 62)
            repository.save(updatedProfile, updatedGoal)

            assertEquals(updatedProfile, repository.current())
            assertEquals(updatedGoal, repository.currentGoal())
        }
}
