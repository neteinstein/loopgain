package org.neteinstein.loopgain

import org.neteinstein.loopgain.ui.navigation.Screen
import kotlin.test.Test
import kotlin.test.assertEquals

class NavigationTest {

    @Test
    fun testScreenRoutes() {
        assertEquals("loading", Screen.Loading.route)
        assertEquals("card_deck", Screen.CardDeck.route)
        assertEquals("session", Screen.Session.route)
        assertEquals("settings", Screen.Settings.route)
    }

    @Test
    fun testScreenRoutesAreUnique() {
        val routes = setOf(Screen.Loading.route, Screen.CardDeck.route, Screen.Session.route, Screen.Settings.route)
        assertEquals(4, routes.size, "All screen routes should be unique")
    }
}
