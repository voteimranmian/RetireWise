package com.retirewise.authentication.data

import com.retirewise.authentication.domain.ConsentRecord
import com.retirewise.authentication.domain.ConsentType
import com.retirewise.authentication.domain.ConsentVersions
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class InMemoryConsentRepositoryTest {
    @Test
    fun consentTypeIsNotGrantedBeforeAnyRecord() =
        runBlocking {
            val repository = InMemoryConsentRepository()

            assertFalse(repository.isGranted(ConsentType.Privacy))
        }

    @Test
    fun recordingConsentIsReflectedByIsGranted() =
        runBlocking {
            val repository = InMemoryConsentRepository()

            repository.recordConsent(ConsentRecord(ConsentType.AiData, ConsentVersions.AI_DATA, granted = true))

            assertTrue(repository.isGranted(ConsentType.AiData))
            assertFalse(repository.isGranted(ConsentType.Privacy))
        }

    @Test
    fun decliningConsentIsReflectedByIsGranted() =
        runBlocking {
            val repository = InMemoryConsentRepository()

            repository.recordConsent(ConsentRecord(ConsentType.AiData, ConsentVersions.AI_DATA, granted = false))

            assertFalse(repository.isGranted(ConsentType.AiData))
        }
}
