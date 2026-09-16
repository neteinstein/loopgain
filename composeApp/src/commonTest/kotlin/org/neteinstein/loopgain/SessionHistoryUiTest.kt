package org.neteinstein.loopgain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.neteinstein.loopgain.data.source.bundledDeck
import org.neteinstein.loopgain.domain.model.CardCategory
import org.neteinstein.loopgain.domain.model.Language
import org.neteinstein.loopgain.domain.model.SessionHistoryEntry
import org.neteinstein.loopgain.ui.viewmodel.toHistoryUiState

class SessionHistoryUiTest {

    private fun cardId(category: CardCategory, index: Int): String =
        bundledDeck.filter { it.category == category }[index].id

    @Test
    fun emptyHistoryHasZeroCountsForEveryCategory() {
        val ui = emptyList<SessionHistoryEntry>().toHistoryUiState(Language.EN)
        assertTrue(ui.entries.isEmpty())
        assertEquals(CardCategory.ORDER.size, ui.categoryFrequency.size)
        assertTrue(ui.categoryFrequency.all { it.count == 0 })
    }

    @Test
    fun frequencyCountsRealDrawsPerCategoryOnly() {
        val motto = cardId(CardCategory.MOTTO, 0)
        val positive = cardId(CardCategory.POSITIVE_REINFORCEMENT, 0)
        val improvements = cardId(CardCategory.IMPROVEMENTS, 0)
        val personal = cardId(CardCategory.PERSONAL_QUESTION, 0)

        val entries = listOf(
            SessionHistoryEntry(
                sessionNumber = 1,
                loggedAtEpochMillis = 0L,
                participantCount = 3,
                plannedSeconds = 1800,
                usedSeconds = 1700,
                drawnCardIds = listOf(motto, positive, improvements, personal),
                reflectionNote = "Good session",
            ),
            SessionHistoryEntry(
                sessionNumber = 2,
                loggedAtEpochMillis = 0L,
                participantCount = 4,
                plannedSeconds = 2400,
                usedSeconds = 2200,
                drawnCardIds = listOf(motto, positive),
                reflectionNote = "",
            ),
        )

        val ui = entries.toHistoryUiState(Language.EN)

        assertEquals(2, ui.entries.size)
        assertEquals(2, ui.categoryFrequency.first { it.category == CardCategory.MOTTO }.count)
        assertEquals(2, ui.categoryFrequency.first { it.category == CardCategory.POSITIVE_REINFORCEMENT }.count)
        assertEquals(1, ui.categoryFrequency.first { it.category == CardCategory.IMPROVEMENTS }.count)
        assertEquals(1, ui.categoryFrequency.first { it.category == CardCategory.PERSONAL_QUESTION }.count)

        // maxCount is shared across all categories so bars are comparable.
        assertTrue(ui.categoryFrequency.all { it.maxCount == 2 })

        // A blank reflection note falls back to a language-appropriate placeholder, never blank text.
        assertEquals("No note added.", ui.entries[1].reflectionNote)
        assertEquals("Good session", ui.entries[0].reflectionNote)
    }

    @Test
    fun portugueseHistoryMetaUsesPortugueseWordForPeople() {
        val entries = listOf(
            SessionHistoryEntry(
                sessionNumber = 1,
                loggedAtEpochMillis = 0L,
                participantCount = 1,
                plannedSeconds = 600,
                usedSeconds = 300,
                drawnCardIds = emptyList(),
                reflectionNote = "",
            ),
        )
        val ui = entries.toHistoryUiState(Language.PT)
        assertEquals("1 pessoa · 5 min", ui.entries.single().meta)
    }
}
