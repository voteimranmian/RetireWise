package com.retirewise.navigation

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

/**
 * The Phase 2 application shell: a [NavHost] hosting the five primary
 * destinations (docs/PRD.md section 8), paired with a bottom navigation bar
 * on compact widths or a side rail on expanded widths (docs/RELEASE_PLAN.md
 * Phase 2 "Add platform adaptive behaviour"). See ADR 0003 for why
 * androidx.navigation-compose was chosen over Voyager.
 *
 * System back (Android back gesture/button, similar affordances on other
 * platforms) pops the destination back stack as usual; once there is
 * nothing left to pop, [onExitToWelcome] is invoked so back always leads
 * somewhere rather than doing nothing.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainAppScaffold(
    onExitToWelcome: () -> Unit,
    onBuildPlanClick: () -> Unit,
    startDestination: Destination = Destination.Today,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = destinationForRoute(backStackEntry?.destination?.route)
    val isAtRootDestination = navController.previousBackStackEntry == null

    BackHandler(enabled = isAtRootDestination) {
        onExitToWelcome()
    }

    val onDestinationClick: (Destination) -> Unit = { destination ->
        if (destination != currentDestination) {
            navController.navigate(destination.route) {
                popUpTo(startDestination.route) { inclusive = false }
                launchSingleTop = true
            }
        }
    }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val useRail = usesNavigationRail(windowWidthDp = maxWidth.value.toInt())

        if (useRail) {
            Row(modifier = Modifier.fillMaxSize()) {
                DestinationNavigationRail(
                    currentDestination = currentDestination,
                    onDestinationClick = onDestinationClick,
                )
                DestinationNavHost(
                    navController = navController,
                    startDestination = startDestination,
                    onBuildPlanClick = onBuildPlanClick,
                    modifier = Modifier.weight(1f),
                )
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                DestinationNavHost(
                    navController = navController,
                    startDestination = startDestination,
                    onBuildPlanClick = onBuildPlanClick,
                    modifier = Modifier.weight(1f),
                )
                DestinationNavigationBar(
                    currentDestination = currentDestination,
                    onDestinationClick = onDestinationClick,
                )
            }
        }
    }
}

@Composable
private fun DestinationNavHost(
    navController: NavHostController,
    startDestination: Destination,
    onBuildPlanClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route,
        modifier = modifier,
    ) {
        composable(Destination.Today.route) { TodayScreen(onBuildPlanClick = onBuildPlanClick) }
        composable(Destination.Plan.route) { PlanScreen() }
        composable(Destination.Explore.route) { ExploreScreen() }
        composable(Destination.Learn.route) { LearnScreen() }
        composable(Destination.AskAi.route) { AskAiScreen() }
    }
}
