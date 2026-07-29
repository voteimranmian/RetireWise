package com.retirewise.scenariocomparison.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.retirewise.designsystem.RetireWiseButton
import com.retirewise.designsystem.RetireWiseButtonVariant
import com.retirewise.designsystem.RetireWiseEmptyState
import com.retirewise.designsystem.RetireWiseTheme
import com.retirewise.profile.domain.ProfileRepository
import com.retirewise.retirementengine.domain.AssumptionSetV1
import com.retirewise.retirementengine.domain.ProjectionRequest
import com.retirewise.retirementengine.domain.formula.project
import com.retirewise.retirementengine.domain.toProjectionRequest
import com.retirewise.scenariocomparison.domain.ScenarioBuilderState
import com.retirewise.scenariocomparison.domain.ScenarioLever
import com.retirewise.scenariocomparison.domain.addScenario
import com.retirewise.scenariocomparison.domain.buildChangeSet
import com.retirewise.scenariocomparison.domain.canAddScenario
import com.retirewise.scenariocomparison.domain.removeScenario
import com.retirewise.scenarioengine.domain.PlanId
import com.retirewise.scenarioengine.domain.ScenarioId
import com.retirewise.scenarioengine.domain.formula.compareScenarios
import com.retirewise.scenarioengine.domain.formula.createAndRunScenario
import com.retirewise.scenarioengine.domain.formula.summarizeProjection
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.koinInject

private val BASE_PLAN_ID = PlanId("current-plan")

/**
 * docs/PRD.md sections 19.4/19.5: build up to 3 retirement scenarios from
 * the base plan and compare them side by side. The scenario levers/cards
 * this screen renders come from [ScenarioLever]/[ScenarioBuilderState]
 * (`shared/scenario_comparison`); the underlying math is entirely
 * `shared/scenario_engine` and `shared/retirement_engine`'s, unmodified.
 */
@Composable
fun ScenarioComparisonScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val profileRepository = koinInject<ProfileRepository>()
    var loaded by remember { mutableStateOf(false) }
    var state by remember { mutableStateOf(ScenarioBuilderState()) }
    var nextScenarioNumber by remember { mutableStateOf(1) }

    LaunchedEffect(Unit) {
        val profile = profileRepository.current()
        val goal = profileRepository.currentGoal()
        if (profile != null && goal != null) {
            val calculationDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            val projectionRequest = toProjectionRequest(profile, goal, AssumptionSetV1.create())
            if (projectionRequest is ProjectionRequest.Ready) {
                val baseSummary = summarizeProjection(projectionRequest, project(projectionRequest, calculationDate))
                state = state.copy(baseRequest = projectionRequest, baseSummary = baseSummary)
            }
        }
        loaded = true
    }

    if (!loaded) return

    val readyRequest = state.baseRequest
    val baseSummary = state.baseSummary

    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(RetireWiseTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(RetireWiseTheme.spacing.md),
    ) {
        Text(
            text = "Compare scenarios",
            style = RetireWiseTheme.typography.headlineLarge,
            color = RetireWiseTheme.colors.textPrimary,
        )

        if (readyRequest == null || baseSummary == null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                RetireWiseEmptyState(
                    title = "Build your plan first",
                    message =
                        "Scenario comparison needs a complete profile and retirement goal " +
                            "to run projections from.",
                    icon = Icons.Filled.CalendarToday,
                    actionLabel = "Back",
                    onActionClick = onBackClick,
                )
            }
            return@Column
        }

        val comparison = compareScenarios(baseSummary, state.builtScenarios)

        ScenarioSummaryCard(title = "Your base plan", summary = comparison.baseSummary)

        comparison.scenarioRows.forEach { row ->
            ScenarioSummaryCard(
                title = row.scenario.name,
                summary = row.summary,
                onRemoveClick = { state = removeScenario(state, row.scenario.id) },
            )
        }

        val configuringLever = state.configuringLever
        if (configuringLever != null) {
            ScenarioLeverInputForm(
                lever = configuringLever,
                onRunScenario = { input ->
                    val changeSet = buildChangeSet(configuringLever, input)
                    val calculationDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                    val scenario =
                        createAndRunScenario(
                            id = ScenarioId("scenario-$nextScenarioNumber"),
                            name = "Scenario $nextScenarioNumber: ${configuringLever.displayLabel}",
                            basePlanId = BASE_PLAN_ID,
                            base = readyRequest,
                            change = changeSet,
                            calculationDate = calculationDate,
                            createdAt = Clock.System.now(),
                        )
                    state = addScenario(state, scenario)
                    nextScenarioNumber += 1
                },
            )
        } else if (canAddScenario(state)) {
            Text(
                text = "Add a scenario",
                style = RetireWiseTheme.typography.headlineMedium,
                color = RetireWiseTheme.colors.textPrimary,
            )
            ScenarioLeverPicker(onLeverClick = { lever -> state = state.copy(configuringLever = lever) })
        }

        RetireWiseButton(label = "Back", onClick = onBackClick, variant = RetireWiseButtonVariant.Secondary)
    }
}
