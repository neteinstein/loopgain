package org.neteinstein.loopgain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.neteinstein.loopgain.data.source.bundledDeck
import org.neteinstein.loopgain.domain.model.CardCategory
import org.neteinstein.loopgain.domain.model.CardLevel

class DeckTest {

    @Test
    fun has48Cards() {
        assertEquals(48, bundledDeck.size)
    }

    @Test
    fun has12CardsPerCategory() {
        CardCategory.entries.forEach { category ->
            assertEquals(12, bundledDeck.count { it.category == category }, "$category should have 12 cards")
        }
    }

    @Test
    fun mottoCardsHaveNoLevel() {
        bundledDeck.filter { it.category == CardCategory.MOTTO }.forEach {
            assertEquals(null, it.level, "Motto card ${it.id} must have no level")
        }
    }

    @Test
    fun leveledCategoriesHaveExactlyFourPerLevel() {
        listOf(CardCategory.POSITIVE_REINFORCEMENT, CardCategory.IMPROVEMENTS, CardCategory.PERSONAL_QUESTION).forEach { category ->
            CardLevel.entries.forEach { level ->
                val count = bundledDeck.count { it.category == category && it.level == level }
                assertEquals(4, count, "$category should have 4 cards at $level")
            }
        }
    }

    @Test
    fun idsAreUnique() {
        val ids = bundledDeck.map { it.id }
        assertEquals(ids.size, ids.toSet().size, "Card ids must be unique")
    }

    @Test
    fun everyCardHasBilingualText() {
        bundledDeck.forEach { card ->
            assertTrue(card.en.isNotBlank(), "${card.id} is missing English text")
            assertTrue(card.pt.isNotBlank(), "${card.id} is missing Portuguese text")
        }
    }

    @Test
    fun codeMatchesPrintedDeckNotation() {
        val positiveOne = bundledDeck.first { it.id == "positive_reinforcement_01" }
        assertEquals("P-01", positiveOne.code)
        val mottoOne = bundledDeck.first { it.id == "motto_01" }
        assertEquals("M-01", mottoOne.code)
    }
}
