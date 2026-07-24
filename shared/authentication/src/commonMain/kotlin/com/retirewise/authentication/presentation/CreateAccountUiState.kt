package com.retirewise.authentication.presentation

import com.retirewise.authentication.domain.AuthProvider
import com.retirewise.authentication.domain.AuthResult

/** [CreateAccountScreen]'s state, independent of Compose runtime. */
sealed interface CreateAccountUiState {
    data object Idle : CreateAccountUiState

    data object CheckingProvider : CreateAccountUiState

    data class ProviderNotConfigured(val provider: AuthProvider) : CreateAccountUiState

    data class Error(val message: String) : CreateAccountUiState
}

/**
 * Maps an [AuthResult] to the state [CreateAccountScreen] should show. A
 * [AuthResult.Success] has no corresponding UI state here because the caller
 * navigates away on success rather than rendering anything.
 */
fun createAccountUiState(result: AuthResult): CreateAccountUiState =
    when (result) {
        is AuthResult.Success -> CreateAccountUiState.Idle
        is AuthResult.NotConfigured -> CreateAccountUiState.ProviderNotConfigured(result.provider)
        is AuthResult.Failure -> CreateAccountUiState.Error(result.message)
    }
