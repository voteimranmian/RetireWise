package com.retirewise.authentication.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.retirewise.authentication.domain.ConsentRecord
import com.retirewise.authentication.domain.ConsentRepository
import com.retirewise.authentication.domain.ConsentType
import com.retirewise.authentication.domain.ConsentVersions
import com.retirewise.designsystem.RetireWiseButton
import com.retirewise.designsystem.RetireWiseButtonVariant
import com.retirewise.designsystem.RetireWiseTheme
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

/**
 * Optional consent to send profile information to an AI provider, per
 * docs/SECURITY.md section 23.2 (explicit consent, explain which data will
 * be sent, minimize the data). Unlike [PrivacyConsentScreen], declining here
 * does not block the rest of the app — it only disables AI personalization
 * until the user opts in later.
 */
@Composable
fun AiConsentScreen(
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val consentRepository = koinInject<ConsentRepository>()
    val colors = RetireWiseTheme.colors
    val typography = RetireWiseTheme.typography
    val coroutineScope = rememberCoroutineScope()

    fun recordAndContinue(granted: Boolean) {
        coroutineScope.launch {
            consentRepository.recordConsent(
                ConsentRecord(type = ConsentType.AiData, version = ConsentVersions.AI_DATA, granted = granted),
            )
            onContinue()
        }
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(RetireWiseTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(RetireWiseTheme.spacing.lg),
    ) {
        Text(text = "Ask AI, personalized to you", style = typography.headlineLarge, color = colors.textPrimary)
        Text(
            text =
                "With your permission, Ask AI can use a minimized version of your retirement " +
                    "profile — never your name, email, or account numbers — to give more relevant " +
                    "answers. You can change this at any time in settings.",
            style = typography.bodyLarge,
            color = colors.textSecondary,
        )

        RetireWiseButton(
            label = "Allow AI personalization",
            onClick = { recordAndContinue(granted = true) },
            variant = RetireWiseButtonVariant.Primary,
            modifier = Modifier.fillMaxWidth(),
        )
        RetireWiseButton(
            label = "Not now",
            onClick = { recordAndContinue(granted = false) },
            variant = RetireWiseButtonVariant.Secondary,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
