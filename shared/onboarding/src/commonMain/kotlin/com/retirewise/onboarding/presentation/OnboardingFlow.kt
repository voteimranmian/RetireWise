package com.retirewise.onboarding.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.retirewise.designsystem.RetireWiseProgressBar
import com.retirewise.designsystem.RetireWiseTheme
import com.retirewise.onboarding.domain.OnboardingDraftRepository
import com.retirewise.onboarding.domain.OnboardingQuestion
import com.retirewise.onboarding.domain.OnboardingResponses
import com.retirewise.onboarding.domain.OnboardingState
import com.retirewise.onboarding.domain.goalFrom
import com.retirewise.onboarding.domain.onboardingAdvance
import com.retirewise.onboarding.domain.onboardingCurrentQuestion
import com.retirewise.onboarding.domain.onboardingGoBack
import com.retirewise.onboarding.domain.onboardingProgress
import com.retirewise.onboarding.domain.profileFrom
import com.retirewise.profile.domain.CanadianProvince
import com.retirewise.profile.domain.PlanningMode
import com.retirewise.profile.domain.ProfileRepository
import com.retirewise.profile.domain.RetirementPriority
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

/**
 * The 12-question initial assessment (docs/PRD.md section 9.1). Loads any
 * in-progress draft on start, saves after every answer, and on completion
 * saves the resulting [com.retirewise.profile.domain.Profile] /
 * [com.retirewise.profile.domain.RetirementGoal] before calling [onFinished].
 */
@Composable
fun OnboardingFlow(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val profileRepository = koinInject<ProfileRepository>()
    val draftRepository = koinInject<OnboardingDraftRepository>()
    val coroutineScope = rememberCoroutineScope()
    var state by remember { mutableStateOf(OnboardingState()) }
    var draftLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        draftRepository.load()?.let { state = it }
        draftLoaded = true
    }

    if (!draftLoaded) return

    fun persist(newState: OnboardingState) {
        state = newState
        coroutineScope.launch { draftRepository.save(newState) }
    }

    val canGoBack = state.currentQuestionIndex > 0
    val goBack: () -> Unit = { persist(onboardingGoBack(state)) }
    val advance: (
        OnboardingResponses,
    ) -> Unit = { responses -> persist(onboardingAdvance(state.copy(responses = responses))) }
    val question = onboardingCurrentQuestion(state)

    Column(modifier = modifier.fillMaxSize().padding(RetireWiseTheme.spacing.lg)) {
        if (question != null) {
            RetireWiseProgressBar(
                progress = onboardingProgress(state),
                label = "Question ${state.currentQuestionIndex + 1} of ${OnboardingQuestion.entries.size}",
                modifier = Modifier.padding(bottom = RetireWiseTheme.spacing.lg),
            )
        }

        when (question) {
            OnboardingQuestion.Age ->
                NumericQuestionStep(
                    question = question,
                    initialText = state.responses.age?.toString().orEmpty(),
                    canGoBack = canGoBack,
                    onBack = goBack,
                    onSkip = { advance(state.responses.copy(age = null)) },
                    onNext = { text -> advance(state.responses.copy(age = text.toIntOrNull())) },
                )
            OnboardingQuestion.Province ->
                OnboardingSingleChoiceQuestion(
                    question = question,
                    options = CanadianProvince.entries,
                    selected = state.responses.province,
                    optionLabel = { it.displayLabel },
                    onSelect = { persist(state.copy(responses = state.responses.copy(province = it))) },
                    onNext = { advance(state.responses) },
                    onBack = goBack,
                    onSkip = { advance(state.responses.copy(province = null)) },
                    onDontKnow = { advance(state.responses.copy(province = null)) },
                    canGoBack = canGoBack,
                )
            OnboardingQuestion.TargetRetirementAge ->
                NumericQuestionStep(
                    question = question,
                    initialText = state.responses.targetRetirementAge?.toString().orEmpty(),
                    canGoBack = canGoBack,
                    onBack = goBack,
                    onSkip = { advance(state.responses.copy(targetRetirementAge = null)) },
                    onNext = { text -> advance(state.responses.copy(targetRetirementAge = text.toIntOrNull())) },
                )
            OnboardingQuestion.AnnualIncome ->
                NumericQuestionStep(
                    question = question,
                    initialText = state.responses.annualIncome?.toString().orEmpty(),
                    canGoBack = canGoBack,
                    onBack = goBack,
                    onSkip = { advance(state.responses.copy(annualIncome = null)) },
                    onNext = { text -> advance(state.responses.copy(annualIncome = text.toDoubleOrNull())) },
                )
            OnboardingQuestion.RetirementSavings ->
                NumericQuestionStep(
                    question = question,
                    initialText = state.responses.retirementSavings?.toString().orEmpty(),
                    canGoBack = canGoBack,
                    onBack = goBack,
                    onSkip = { advance(state.responses.copy(retirementSavings = null)) },
                    onNext = { text -> advance(state.responses.copy(retirementSavings = text.toDoubleOrNull())) },
                )
            OnboardingQuestion.WorkplacePension ->
                OnboardingYesNoQuestion(
                    question = question,
                    selected = state.responses.hasWorkplacePension,
                    onSelect = { persist(state.copy(responses = state.responses.copy(hasWorkplacePension = it))) },
                    onNext = { advance(state.responses) },
                    onBack = goBack,
                    onSkip = { advance(state.responses.copy(hasWorkplacePension = null)) },
                    onDontKnow = { advance(state.responses.copy(hasWorkplacePension = null)) },
                    canGoBack = canGoBack,
                )
            OnboardingQuestion.MonthlyContribution ->
                NumericQuestionStep(
                    question = question,
                    initialText = state.responses.monthlyContribution?.toString().orEmpty(),
                    canGoBack = canGoBack,
                    onBack = goBack,
                    onSkip = { advance(state.responses.copy(monthlyContribution = null)) },
                    onNext = { text -> advance(state.responses.copy(monthlyContribution = text.toDoubleOrNull())) },
                )
            OnboardingQuestion.HomeOwnership ->
                OnboardingYesNoQuestion(
                    question = question,
                    selected = state.responses.ownsHome,
                    onSelect = { persist(state.copy(responses = state.responses.copy(ownsHome = it))) },
                    onNext = { advance(state.responses) },
                    onBack = goBack,
                    onSkip = { advance(state.responses.copy(ownsHome = null)) },
                    onDontKnow = { advance(state.responses.copy(ownsHome = null)) },
                    canGoBack = canGoBack,
                )
            OnboardingQuestion.ExpectedDebt ->
                NumericQuestionStep(
                    question = question,
                    initialText = state.responses.expectedDebtAtRetirement?.toString().orEmpty(),
                    canGoBack = canGoBack,
                    onBack = goBack,
                    onSkip = { advance(state.responses.copy(expectedDebtAtRetirement = null)) },
                    onNext = {
                            text ->
                        advance(state.responses.copy(expectedDebtAtRetirement = text.toDoubleOrNull()))
                    },
                )
            OnboardingQuestion.TargetMonthlySpending ->
                NumericQuestionStep(
                    question = question,
                    initialText = state.responses.targetMonthlySpending?.toString().orEmpty(),
                    canGoBack = canGoBack,
                    onBack = goBack,
                    onSkip = { advance(state.responses.copy(targetMonthlySpending = null)) },
                    onNext = { text -> advance(state.responses.copy(targetMonthlySpending = text.toDoubleOrNull())) },
                )
            OnboardingQuestion.PlanningMode ->
                OnboardingSingleChoiceQuestion(
                    question = question,
                    options = PlanningMode.entries,
                    selected = state.responses.planningMode,
                    optionLabel = { it.displayLabel },
                    onSelect = { persist(state.copy(responses = state.responses.copy(planningMode = it))) },
                    onNext = { advance(state.responses) },
                    onBack = goBack,
                    onSkip = { advance(state.responses.copy(planningMode = null)) },
                    onDontKnow = { advance(state.responses.copy(planningMode = null)) },
                    canGoBack = canGoBack,
                )
            OnboardingQuestion.RetirementPriorities ->
                OnboardingMultiChoiceQuestion(
                    question = question,
                    options = RetirementPriority.entries,
                    selected = state.responses.priorities,
                    optionLabel = { it.displayLabel },
                    onToggle = { option ->
                        val updated =
                            if (option in state.responses.priorities) {
                                state.responses.priorities - option
                            } else {
                                state.responses.priorities + option
                            }
                        persist(state.copy(responses = state.responses.copy(priorities = updated)))
                    },
                    onNext = { advance(state.responses) },
                    onBack = goBack,
                    onSkip = { advance(state.responses.copy(priorities = emptySet())) },
                    onDontKnow = { advance(state.responses.copy(priorities = emptySet())) },
                    canGoBack = canGoBack,
                )
            null -> {
                val profile = profileFrom(state.responses)
                val goal = goalFrom(state.responses)
                OnboardingCompleteScreen(
                    profile = profile,
                    goal = goal,
                    onDone = {
                        coroutineScope.launch {
                            profileRepository.save(profile, goal)
                            draftRepository.clear()
                            onFinished()
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun NumericQuestionStep(
    question: OnboardingQuestion,
    initialText: String,
    canGoBack: Boolean,
    onBack: () -> Unit,
    onSkip: () -> Unit,
    onNext: (String) -> Unit,
) {
    var text by remember(question) { mutableStateOf(initialText) }
    OnboardingNumericQuestion(
        question = question,
        value = text,
        onValueChange = { text = it },
        onNext = { onNext(text) },
        onBack = onBack,
        onSkip = onSkip,
        onDontKnow = onSkip,
        canGoBack = canGoBack,
    )
}
