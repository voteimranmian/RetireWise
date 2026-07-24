package com.retirewise.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.retirewise.designsystem.RetireWiseButton
import com.retirewise.designsystem.RetireWiseButtonVariant
import com.retirewise.designsystem.RetireWiseTheme
import org.koin.compose.koinInject

@Composable
fun WelcomeScreen(
    onStartPlanClick: () -> Unit,
    onAskQuestionClick: () -> Unit,
    modifier: Modifier = Modifier,
    onViewDesignSystemClick: (() -> Unit)? = null,
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
            color = colors.primaryFixed,
            contentColor = colors.onPrimaryFixed,
            shape = RoundedCornerShape(percent = 50),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(RetireWiseTheme.spacing.xs),
                modifier =
                    Modifier.padding(
                        horizontal = RetireWiseTheme.spacing.md,
                        vertical = RetireWiseTheme.spacing.xs,
                    ),
            ) {
                Icon(
                    imageVector = Icons.Filled.VerifiedUser,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                )
                Text(
                    text = content.badgeLabel,
                    style = typography.labelSmall,
                )
            }
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
            trailingIcon = Icons.Filled.ArrowForward,
        )

        Spacer(modifier = Modifier.padding(top = RetireWiseTheme.spacing.sm))

        RetireWiseButton(
            label = content.secondaryActionLabel,
            onClick = onAskQuestionClick,
            variant = RetireWiseButtonVariant.Secondary,
            modifier = Modifier.fillMaxWidth(),
        )

        if (onViewDesignSystemClick != null) {
            Spacer(modifier = Modifier.padding(top = RetireWiseTheme.spacing.md))

            Text(
                text = "Design system",
                style = typography.labelSmall,
                color = colors.textSecondary,
                modifier = Modifier.clickable(onClick = onViewDesignSystemClick),
            )
        }
    }
}
