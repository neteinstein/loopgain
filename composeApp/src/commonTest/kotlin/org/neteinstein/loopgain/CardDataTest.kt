package org.neteinstein.loopgain

import kotlin.test.Test
import kotlin.test.assertEquals
import org.neteinstein.loopgain.ui.screens.CardData
import org.neteinstein.loopgain.ui.screens.CardType

class CardDataTest {
    
    @Test
    fun testCardDataCreation() {
        val card = CardData(
            type = CardType.MOTTO,
            content = "Test content",
            rotation = 5f,
            offsetX = 10f,
            offsetY = 20f
        )
        
        assertEquals(CardType.MOTTO, card.type)
        assertEquals("Test content", card.content)
        assertEquals(5f, card.rotation)
        assertEquals(10f, card.offsetX)
        assertEquals(20f, card.offsetY)
    }
    
    @Test
    fun testCardDataWithDifferentType() {
        val card = CardData(
            type = CardType.PERSONAL_QUESTION,
            content = "Content only",
            rotation = 0f,
            offsetX = 0f,
            offsetY = 0f
        )
        
        assertEquals(CardType.PERSONAL_QUESTION, card.type)
        assertEquals("Content only", card.content)
    }
    
    @Test
    fun testAllCardTypes() {
        // Test that all card types can be created
        val mottoCard = CardData(CardType.MOTTO, "Motto", 0f, 0f, 0f)
        val questionCard = CardData(CardType.PERSONAL_QUESTION, "Question", 0f, 0f, 0f)
        val improvementCard = CardData(CardType.IMPROVEMENTS, "Improvement", 0f, 0f, 0f)
        val reinforcementCard = CardData(CardType.POSITIVE_REINFORCEMENT, "Reinforcement", 0f, 0f, 0f)
        
        assertEquals(CardType.MOTTO, mottoCard.type)
        assertEquals(CardType.PERSONAL_QUESTION, questionCard.type)
        assertEquals(CardType.IMPROVEMENTS, improvementCard.type)
        assertEquals(CardType.POSITIVE_REINFORCEMENT, reinforcementCard.type)
    }
}
