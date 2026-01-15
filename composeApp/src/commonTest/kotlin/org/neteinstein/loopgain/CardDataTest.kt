package org.neteinstein.loopgain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CardDataTest {
    
    @Test
    fun testCardDataCreation() {
        val card = org.neteinstein.loopgain.ui.screens.CardData(
            title = "Test",
            content = "Test content",
            backgroundColor = androidx.compose.ui.graphics.Color.Blue,
            rotation = 5f,
            offsetX = 10f,
            offsetY = 20f
        )
        
        assertEquals("Test", card.title)
        assertEquals("Test content", card.content)
        assertEquals(5f, card.rotation)
        assertEquals(10f, card.offsetX)
        assertEquals(20f, card.offsetY)
    }
    
    @Test
    fun testCardDataWithEmptyTitle() {
        val card = org.neteinstein.loopgain.ui.screens.CardData(
            title = "",
            content = "Content only",
            backgroundColor = androidx.compose.ui.graphics.Color.Red,
            rotation = 0f,
            offsetX = 0f,
            offsetY = 0f
        )
        
        assertTrue(card.title.isEmpty())
        assertEquals("Content only", card.content)
    }
}
