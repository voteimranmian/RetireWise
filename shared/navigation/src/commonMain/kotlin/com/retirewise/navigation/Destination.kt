package com.retirewise.navigation

/**
 * The five primary application destinations (docs/PRD.md section 8), each
 * reachable from the main navigation bar/rail built in [MainAppScaffold].
 */
enum class Destination(
    val route: String,
    val label: String,
) {
    Today(route = "today", label = "Today"),
    Plan(route = "plan", label = "Plan"),
    Explore(route = "explore", label = "Explore"),
    Learn(route = "learn", label = "Learn"),
    AskAi(route = "ask_ai", label = "Ask AI"),
}

/**
 * Resolves which [Destination] (if any) a NavController back stack route
 * corresponds to. Independent of Compose/Navigation runtime so it can be
 * unit tested directly.
 */
fun destinationForRoute(route: String?): Destination? = Destination.entries.firstOrNull { it.route == route }
