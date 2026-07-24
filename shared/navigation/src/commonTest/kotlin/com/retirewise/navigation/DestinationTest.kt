package com.retirewise.navigation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DestinationTest {
    @Test
    fun resolvesKnownRouteToDestination() {
        assertEquals(Destination.Today, destinationForRoute("today"))
        assertEquals(Destination.Plan, destinationForRoute("plan"))
        assertEquals(Destination.Explore, destinationForRoute("explore"))
        assertEquals(Destination.Learn, destinationForRoute("learn"))
        assertEquals(Destination.AskAi, destinationForRoute("ask_ai"))
    }

    @Test
    fun returnsNullForUnknownRoute() {
        assertNull(destinationForRoute("not_a_real_route"))
    }

    @Test
    fun returnsNullForNullRoute() {
        assertNull(destinationForRoute(null))
    }
}
