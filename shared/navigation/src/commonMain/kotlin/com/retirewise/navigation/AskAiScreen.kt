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
 * Ask AI — the primary conversational retirement advisor (docs/PRD.md
 * section 8.5). The conversational advisor needs the backend AI gateway
 * (Phase 8 per docs/RELEASE_PLAN.md) and must never call an AI provider
 * directly from the mobile client (CLAUDE.md rule 8), so Phase 2 ships the
 * destination itself with an honest empty state.
 */
@Composable
fun AskAiScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(RetireWiseTheme.spacing.lg),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        RetireWiseEmptyState(
            title = "Ask AI is coming soon",
            message =
                "Your personal retirement advisor will be able to answer questions " +
                    "using your verified profile, calculations, and trusted sources.",
        )
    }
}
