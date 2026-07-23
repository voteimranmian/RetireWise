package com.retirewise.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.retirewise.designsystem.RetireWiseTheme
import com.retirewise.navigation.di.appModule
import org.koin.compose.KoinApplication

/**
 * Phase 0 application root. Wires up Koin DI, the shared theme, and the
 * minimal [Screen] navigation state (see Screen.kt for why this is not
 * Voyager/androidx.navigation yet).
 */
@Composable
fun App() {
    KoinApplication(application = { modules(appModule) }) {
        RetireWiseTheme {
            var currentScreen by remember { mutableStateOf<Screen>(Screen.Welcome) }

            when (currentScreen) {
                is Screen.Welcome ->
                    WelcomeScreen(
                        onStartPlanClick = { currentScreen = Screen.StartPlanPlaceholder },
                        onAskQuestionClick = { currentScreen = Screen.AskQuestionPlaceholder },
                        onViewDesignSystemClick = { currentScreen = Screen.DesignSystemShowcase },
                    )
                is Screen.StartPlanPlaceholder ->
                    PlaceholderScreen(
                        message = "Start my plan is coming soon.",
                        onBackClick = { currentScreen = Screen.Welcome },
                    )
                is Screen.AskQuestionPlaceholder ->
                    PlaceholderScreen(
                        message = "Ask a question is coming soon.",
                        onBackClick = { currentScreen = Screen.Welcome },
                    )
                is Screen.DesignSystemShowcase ->
                    DesignSystemShowcaseScreen(
                        onBackClick = { currentScreen = Screen.Welcome },
                    )
            }
        }
    }
}

@Composable
private fun PlaceholderScreen(
    message: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(RetireWiseTheme.spacing.lg),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = message,
            color = RetireWiseTheme.colors.textPrimary,
        )

        androidx.compose.foundation.layout.Spacer(
            modifier = Modifier.padding(top = RetireWiseTheme.spacing.md),
        )

        OutlinedButton(onClick = onBackClick) {
            Text("Back")
        }
    }
}
