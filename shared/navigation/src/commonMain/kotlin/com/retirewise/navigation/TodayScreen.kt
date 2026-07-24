package com.retirewise.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.retirewise.designsystem.RetireWiseEmptyState
import com.retirewise.designsystem.RetireWiseTheme

/**
 * Today — retirement overview (docs/PRD.md section 8.1/19.2). The real
 * dashboard (readiness status, projected retirement age, top actions, etc.)
 * needs a user profile and the deterministic retirement engine, neither of
 * which exist yet (Phase 4/Phase 5 per docs/RELEASE_PLAN.md). Phase 2 ships
 * the destination itself with an honest empty state rather than fabricated
 * numbers, per CLAUDE.md rule 15 (handle empty states).
 */
@Composable
fun TodayScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(RetireWiseTheme.spacing.lg),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        RetireWiseEmptyState(
            title = "Your retirement outlook isn't ready yet",
            message =
                "Once you build your plan, you'll see your retirement readiness, " +
                    "projected income, and top actions here.",
            icon = Icons.Filled.CalendarToday,
        )
    }
}
