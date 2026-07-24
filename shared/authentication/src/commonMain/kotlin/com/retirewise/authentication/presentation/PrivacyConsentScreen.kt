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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.retirewise.authentication.domain.ConsentRecord
import com.retirewise.authentication.domain.ConsentRepository
import com.retirewise.authentication.domain.ConsentType
import com.retirewise.authentication.domain.ConsentVersions
import com.retirewise.designsystem.RetireWiseButton
import com.retirewise.designsystem.RetireWiseButtonVariant
import com.retirewise.designsystem.RetireWiseCheckboxOption
import com.retirewise.designsystem.RetireWiseTheme
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

/**
 * Required consent to the app's data handling practices, per docs/PRD.md
 * section 7.1 and docs/SECURITY.md section 23.2. Must be accepted before a
 * user can proceed to [com.retirewise.navigation] (the constructor of this
 * screen's caller decides where "continue" leads).
 */
@Composable
fun PrivacyConsentScreen(
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val consentRepository = koinInject<ConsentRepository>()
    val colors = RetireWiseTheme.colors
    val typography = RetireWiseTheme.typography
    val coroutineScope = rememberCoroutineScope()
    var accepted by remember { mutableStateOf(false) }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(RetireWiseTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(RetireWiseTheme.spacing.lg),
    ) {
        Text(text = "Your privacy", style = typography.headlineLarge, color = colors.textPrimary)
        Text(
            text =
                "RetireWise stores the financial information you provide so it can build your " +
                    "retirement plan. We never sell your data, and any information sent to an AI " +
                    "provider is minimized and covered by a separate consent you control.",
            style = typography.bodyLarge,
            color = colors.textSecondary,
        )

        RetireWiseCheckboxOption(
            label = "I have read and accept the Privacy Policy",
            checked = accepted,
            onCheckedChange = { accepted = it },
        )

        RetireWiseButton(
            label = "Continue",
            onClick = {
                coroutineScope.launch {
                    consentRepository.recordConsent(
                        ConsentRecord(type = ConsentType.Privacy, version = ConsentVersions.PRIVACY, granted = true),
                    )
                    onContinue()
                }
            },
            variant = RetireWiseButtonVariant.Primary,
            enabled = privacyConsentContinueEnabled(accepted),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
