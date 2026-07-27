package com.retirewise.onboarding.data

import com.retirewise.onboarding.domain.OnboardingResponses
import com.retirewise.onboarding.domain.OnboardingState
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class InMemoryOnboardingDraftRepositoryTest {
    @Test
    fun loadIsNullBeforeAnySave() =
        runBlocking {
            val repository = InMemoryOnboardingDraftRepository()

            assertNull(repository.load())
        }

    @Test
    fun savedDraftIsReturnedByLoad() =
        runBlocking {
            val repository = InMemoryOnboardingDraftRepository()
            val state = OnboardingState(responses = OnboardingResponses(age = 45), currentQuestionIndex = 3)

            repository.save(state)

            assertEquals(state, repository.load())
        }

    @Test
    fun clearRemovesTheSavedDraft() =
        runBlocking {
            val repository = InMemoryOnboardingDraftRepository()
            repository.save(OnboardingState(currentQuestionIndex = 2))

            repository.clear()

            assertNull(repository.load())
        }
}
