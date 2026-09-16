package org.neteinstein.loopgain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.neteinstein.loopgain.data.repository.InMemorySessionHistoryRepository
import org.neteinstein.loopgain.domain.model.SessionHistoryEntry

class SessionHistoryRepositoryTest {

    private fun entry(number: Int, cardIds: List<String>) = SessionHistoryEntry(
        sessionNumber = number,
        loggedAtEpochMillis = 0L,
        participantCount = 3,
        plannedSeconds = 1800,
        usedSeconds = 900,
        drawnCardIds = cardIds,
        reflectionNote = "",
    )

    @Test
    fun clearAllForgetsLoggedSessionsAndHeldBackCards() {
        val repo = InMemorySessionHistoryRepository()
        repo.record(entry(1, listOf("motto_01", "positive_reinforcement_01")))
        assertTrue(repo.all().isNotEmpty())
        assertTrue(repo.recentlyUsedCardIds().isNotEmpty())

        repo.clearAll()

        assertTrue(repo.all().isEmpty())
        assertTrue(repo.recentlyUsedCardIds().isEmpty())
    }

    @Test
    fun nextSessionNumberRestartsAfterClearing() {
        val repo = InMemorySessionHistoryRepository()
        repo.record(entry(1, emptyList()))
        assertEquals(2, repo.nextSessionNumber())

        repo.clearAll()
        assertEquals(1, repo.nextSessionNumber())
    }
}
