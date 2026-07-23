package com.retirewise.navigation

/**
 * Minimal Phase 0 navigation model. This is intentionally a small in-memory
 * screen stack rather than Voyager/androidx.navigation — with only one real
 * screen so far there is nothing to gain from an external navigation library.
 * A real navigation solution is introduced in Phase 2 (Application shell,
 * see docs/RELEASE_PLAN.md) once the five primary destinations exist.
 */
sealed interface Screen {
    data object Welcome : Screen

    data object StartPlanPlaceholder : Screen

    data object AskQuestionPlaceholder : Screen
}
