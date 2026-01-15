package org.neteinstein.loopgain

import org.neteinstein.loopgain.ui.navigation.Screen
import kotlin.test.Test
import kotlin.test.assertEquals

class NavigationTest {
    
    @Test
    fun testScreenRoutes() {
        assertEquals("loading", Screen.Loading.route)
        assertEquals("card_deck", Screen.CardDeck.route)
    }
    
    @Test
    fun testScreenRoutesAreUnique() {
        val routes = setOf(Screen.Loading.route, Screen.CardDeck.route)
        assertEquals(2, routes.size, "All screen routes should be unique")
    }
}
