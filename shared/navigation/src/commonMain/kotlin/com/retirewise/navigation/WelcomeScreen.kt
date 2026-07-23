package com.retirewise.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.retirewise.designsystem.RetireWiseButton
import com.retirewise.designsystem.RetireWiseButtonVariant
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

        RetireWiseButton(
            label = content.primaryActionLabel,
            onClick = onStartPlanClick,
            variant = RetireWiseButtonVariant.Primary,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.padding(top = RetireWiseTheme.spacing.sm))

        RetireWiseButton(
            label = content.secondaryActionLabel,
            onClick = onAskQuestionClick,
            variant = RetireWiseButtonVariant.Secondary,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
