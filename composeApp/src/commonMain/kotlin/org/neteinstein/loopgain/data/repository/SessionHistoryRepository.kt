package org.neteinstein.loopgain.data.repository

import org.neteinstein.loopgain.domain.model.SessionHistoryEntry

/**
 * Logged sessions, most recent first. Backs both the "held back" draw rule (twelve cards per
 * category means a full year without a repeat, per the deck's own Instructions card) and a
 * facilitator-facing session log.
 *
 * The current implementation is in-memory only and does not survive a process restart — there is
 * no persistence dependency in this module yet. It is registered as a Koin singleton so a single
 * instance is shared for the app's lifetime; swapping in real storage later only means replacing
 * this implementation.
 */
interface SessionHistoryRepository {
    fun all(): List<SessionHistoryEntry>
    fun record(entry: SessionHistoryEntry)

    /** Card ids drawn in the most recent [maxSessions] logged sessions. */
    fun recentlyUsedCardIds(maxSessions: Int = 12): Set<String>

    fun nextSessionNumber(): Int
}

class InMemorySessionHistoryRepository : SessionHistoryRepository {
    private val entries = mutableListOf<SessionHistoryEntry>()

    override fun all(): List<SessionHistoryEntry> = entries.toList()

    override fun record(entry: SessionHistoryEntry) {
        entries.add(0, entry)
    }

    override fun recentlyUsedCardIds(maxSessions: Int): Set<String> =
        entries.take(maxSessions).flatMap { it.drawnCardIds }.toSet()

    override fun nextSessionNumber(): Int = entries.size + 1
}
