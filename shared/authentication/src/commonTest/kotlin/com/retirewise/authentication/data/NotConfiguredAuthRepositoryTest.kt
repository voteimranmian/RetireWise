package com.retirewise.authentication.data

import com.retirewise.authentication.domain.AuthProvider
import com.retirewise.authentication.domain.AuthResult
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class NotConfiguredAuthRepositoryTest {
    @Test
    fun everyProviderReportsNotConfigured() =
        runBlocking {
            val repository = NotConfiguredAuthRepository()

            AuthProvider.entries.forEach { provider ->
                assertEquals(AuthResult.NotConfigured(provider), repository.signIn(provider))
            }
        }
}
