package com.retirewise.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.retirewise.authentication.di.authenticationModule
import com.retirewise.authentication.presentation.AiConsentScreen
import com.retirewise.authentication.presentation.CreateAccountScreen
import com.retirewise.authentication.presentation.PrivacyConsentScreen
import com.retirewise.designsystem.RetireWiseTheme
import com.retirewise.navigation.di.appModule
import org.koin.compose.KoinApplication

/**
 * Application root. Wires up Koin DI, the shared theme, and the top-level
 * [Screen] navigation state; navigation within [Screen.MainApp] is handled
 * by [MainAppScaffold] (see ADR 0003). Welcome routes into the Phase 3
 * Create Account → Privacy Consent → AI Consent flow (docs/PRD.md section
 * 7.1) before reaching [Screen.MainApp].
 */
@Composable
fun App() {
    KoinApplication(application = { modules(appModule, authenticationModule) }) {
        RetireWiseTheme {
            var currentScreen by remember { mutableStateOf<Screen>(Screen.Welcome) }
            val screen = currentScreen

            when (screen) {
                is Screen.Welcome ->
                    WelcomeScreen(
                        onStartPlanClick = {
                            currentScreen = Screen.CreateAccount(startDestination = Destination.Today)
                        },
                        onAskQuestionClick = {
                            currentScreen = Screen.CreateAccount(startDestination = Destination.AskAi)
                        },
                        onViewDesignSystemClick = { currentScreen = Screen.DesignSystemShowcase },
                    )
                is Screen.CreateAccount ->
                    CreateAccountScreen(
                        onSignedIn = { currentScreen = Screen.PrivacyConsent(screen.startDestination) },
                        onSkipForPreview = { currentScreen = Screen.PrivacyConsent(screen.startDestination) },
                    )
                is Screen.PrivacyConsent ->
                    PrivacyConsentScreen(
                        onContinue = { currentScreen = Screen.AiConsent(screen.startDestination) },
                    )
                is Screen.AiConsent ->
                    AiConsentScreen(
                        onContinue = { currentScreen = Screen.MainApp(startDestination = screen.startDestination) },
                    )
                is Screen.MainApp ->
                    MainAppScaffold(
                        startDestination = screen.startDestination,
                        onExitToWelcome = { currentScreen = Screen.Welcome },
                    )
                is Screen.DesignSystemShowcase ->
                    DesignSystemShowcaseScreen(
                        onBackClick = { currentScreen = Screen.Welcome },
                    )
            }
        }
    }
}
