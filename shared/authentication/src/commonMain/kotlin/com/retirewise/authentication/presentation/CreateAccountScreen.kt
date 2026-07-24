package com.retirewise.authentication.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.retirewise.authentication.domain.AuthProvider
import com.retirewise.authentication.domain.AuthRepository
import com.retirewise.authentication.domain.AuthResult
import com.retirewise.designsystem.RetireWiseButton
import com.retirewise.designsystem.RetireWiseButtonVariant
import com.retirewise.designsystem.RetireWiseEmptyState
import com.retirewise.designsystem.RetireWiseErrorState
import com.retirewise.designsystem.RetireWiseTheme
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

/**
 * Account creation entry point, per docs/PRD.md section 7.1. Every provider
 * currently reports [com.retirewise.authentication.domain.AuthResult.NotConfigured]
 * (see [com.retirewise.authentication.data.NotConfiguredAuthRepository]) since
 * no backend account service or OAuth credentials exist yet in this
 * environment.
 *
 * [onSkipForPreview] is a temporary, clearly-labeled dev-preview affordance
 * that lets the rest of the app remain navigable while real sign-in is
 * unimplemented — remove once at least one provider is real.
 */
@Composable
fun CreateAccountScreen(
    onSignedIn: () -> Unit,
    onSkipForPreview: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val authRepository = koinInject<AuthRepository>()
    val colors = RetireWiseTheme.colors
    val typography = RetireWiseTheme.typography
    val coroutineScope = rememberCoroutineScope()
    var uiState by remember { mutableStateOf<CreateAccountUiState>(CreateAccountUiState.Idle) }

    val onProviderClick: (AuthProvider) -> Unit = { provider ->
        uiState = CreateAccountUiState.CheckingProvider
        coroutineScope.launch {
            val result = authRepository.signIn(provider)
            if (result is AuthResult.Success) {
                onSignedIn()
            } else {
                uiState = createAccountUiState(result)
            }
        }
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(RetireWiseTheme.spacing.lg),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Create your account",
            style = typography.headlineLarge,
            color = colors.textPrimary,
            textAlign = TextAlign.Center,
        )

        Column(
            modifier = Modifier.padding(top = RetireWiseTheme.spacing.xl).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(RetireWiseTheme.spacing.sm),
        ) {
            AuthProvider.entries.forEach { provider ->
                RetireWiseButton(
                    label = provider.buttonLabel,
                    onClick = { onProviderClick(provider) },
                    variant = RetireWiseButtonVariant.Primary,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        when (val state = uiState) {
            is CreateAccountUiState.ProviderNotConfigured ->
                RetireWiseEmptyState(
                    title = "${state.provider.buttonLabel} isn't available yet",
                    message = "This build doesn't have sign-in wired up to a real account service yet.",
                    modifier = Modifier.padding(top = RetireWiseTheme.spacing.lg),
                )
            is CreateAccountUiState.Error ->
                RetireWiseErrorState(
                    title = "Something went wrong",
                    message = state.message,
                    modifier = Modifier.padding(top = RetireWiseTheme.spacing.lg),
                )
            CreateAccountUiState.Idle, CreateAccountUiState.CheckingProvider -> Unit
        }

        Text(
            text = "Skip for now (preview build)",
            style = typography.labelSmall,
            color = colors.textSecondary,
            modifier =
                Modifier
                    .padding(top = RetireWiseTheme.spacing.lg)
                    .clickable(onClick = onSkipForPreview),
        )
    }
}
