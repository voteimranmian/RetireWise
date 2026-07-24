package com.retirewise.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.retirewise.designsystem.RetireWiseEmptyState
import com.retirewise.designsystem.RetireWiseTheme

/**
 * Learn — personalized financial education (docs/PRD.md section 8.4).
 * Content prioritization needs a user profile and a learning content module
 * (neither exists yet), so Phase 2 ships an honest empty state.
 */
@Composable
fun LearnScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(RetireWiseTheme.spacing.lg),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        RetireWiseEmptyState(
            title = "Personalized learning is on the way",
            message =
                "Once you build your plan, we'll recommend articles based on " +
                    "what matters most to you.",
        )
    }
}
