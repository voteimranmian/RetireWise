package com.retirewise.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.retirewise.designsystem.RetireWiseTheme
import com.retirewise.navigation.di.appModule
import org.koin.compose.KoinApplication

/**
 * Application root. Wires up Koin DI, the shared theme, and the top-level
 * [Screen] navigation state; navigation within [Screen.MainApp] is handled
 * by [MainAppScaffold] (see ADR 0003).
 */
@Composable
fun App() {
    KoinApplication(application = { modules(appModule) }) {
        RetireWiseTheme {
            var currentScreen by remember { mutableStateOf<Screen>(Screen.Welcome) }
            val screen = currentScreen

            when (screen) {
                is Screen.Welcome ->
                    WelcomeScreen(
                        onStartPlanClick = { currentScreen = Screen.MainApp(startDestination = Destination.Today) },
                        onAskQuestionClick = { currentScreen = Screen.MainApp(startDestination = Destination.AskAi) },
                        onViewDesignSystemClick = { currentScreen = Screen.DesignSystemShowcase },
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
