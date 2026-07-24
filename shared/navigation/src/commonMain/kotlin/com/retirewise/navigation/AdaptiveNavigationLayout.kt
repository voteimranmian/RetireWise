package com.retirewise.navigation

/**
 * The window width, in dp, at or above which the main navigation switches
 * from a bottom bar (compact/phone) to a side rail (expanded/tablet or
 * desktop) — per CLAUDE.md rule 15/16 and docs/RELEASE_PLAN.md Phase 2
 * "Add platform adaptive behaviour". 600dp matches the common
 * compact/medium window size class breakpoint.
 */
const val NAVIGATION_RAIL_BREAKPOINT_DP = 600

/**
 * Whether the main navigation should render as a side rail rather than a
 * bottom bar for the given window width. Independent of Compose runtime so
 * it can be unit tested directly.
 */
fun usesNavigationRail(windowWidthDp: Int): Boolean = windowWidthDp >= NAVIGATION_RAIL_BREAKPOINT_DP
