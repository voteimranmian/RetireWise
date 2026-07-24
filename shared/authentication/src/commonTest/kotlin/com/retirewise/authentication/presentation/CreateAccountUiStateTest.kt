package com.retirewise.authentication.presentation

import com.retirewise.authentication.domain.AuthProvider
import com.retirewise.authentication.domain.AuthResult
import com.retirewise.authentication.domain.AuthSession
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateAccountUiStateTest {
    @Test
    fun successMapsToIdleSoTheCallerCanNavigateAway() {
        val result = AuthResult.Success(AuthSession(userId = "u1", provider = AuthProvider.Email))
        assertEquals(CreateAccountUiState.Idle, createAccountUiState(result))
    }

    @Test
    fun notConfiguredMapsToProviderNotConfiguredWithTheSameProvider() {
        val result = AuthResult.NotConfigured(AuthProvider.Apple)
        assertEquals(CreateAccountUiState.ProviderNotConfigured(AuthProvider.Apple), createAccountUiState(result))
    }

    @Test
    fun failureMapsToErrorWithTheSameMessage() {
        val result = AuthResult.Failure("network error")
        assertEquals(CreateAccountUiState.Error("network error"), createAccountUiState(result))
    }
}
