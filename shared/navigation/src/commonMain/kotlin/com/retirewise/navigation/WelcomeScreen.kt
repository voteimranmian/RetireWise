package com.retirewise.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.retirewise.designsystem.RetireWiseTheme
import org.koin.compose.koinInject

@Composable
fun WelcomeScreen(
    onStartPlanClick: () -> Unit,
    onAskQuestionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val content = koinInject<WelcomeContentProvider>().content()
    val colors = RetireWiseTheme.colors
    val typography = RetireWiseTheme.typography

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(RetireWiseTheme.spacing.lg),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Surface(
            color = colors.primaryContainer,
            contentColor = colors.primary,
            shape = RoundedCornerShape(percent = 50),
        ) {
            Text(
                text = content.badgeLabel,
                style = typography.labelSmall,
                modifier =
                    Modifier.padding(
                        horizontal = RetireWiseTheme.spacing.md,
                        vertical = RetireWiseTheme.spacing.xs,
                    ),
            )
        }

        Spacer(modifier = Modifier.padding(top = RetireWiseTheme.spacing.md))

        Text(
            text = "RetireWise",
            style = typography.headlineMedium,
            color = colors.primary,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.padding(top = RetireWiseTheme.spacing.sm))

        Text(
            text = content.headline,
            style = typography.displayLarge,
            color = colors.textPrimary,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.padding(top = RetireWiseTheme.spacing.sm))

        Text(
            text = content.supportingMessage,
            style = typography.bodyLarge,
            color = colors.textSecondary,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.padding(top = RetireWiseTheme.spacing.xl))

        Button(
            onClick = onStartPlanClick,
            shape = RoundedCornerShape(percent = 50),
            contentPadding =
                PaddingValues(
                    horizontal = RetireWiseTheme.spacing.xl,
                    vertical = RetireWiseTheme.spacing.md,
                ),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = content.primaryActionLabel },
        ) {
            Text(content.primaryActionLabel, style = typography.labelLarge)
        }

        Spacer(modifier = Modifier.padding(top = RetireWiseTheme.spacing.sm))

        OutlinedButton(
            onClick = onAskQuestionClick,
            shape = RoundedCornerShape(percent = 50),
            border = BorderStroke(1.dp, colors.divider),
            contentPadding =
                PaddingValues(
                    horizontal = RetireWiseTheme.spacing.xl,
                    vertical = RetireWiseTheme.spacing.md,
                ),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = content.secondaryActionLabel },
        ) {
            Text(content.secondaryActionLabel, style = typography.labelLarge)
        }
    }
}
