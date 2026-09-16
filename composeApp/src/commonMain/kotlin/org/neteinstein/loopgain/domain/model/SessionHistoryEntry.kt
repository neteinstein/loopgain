package org.neteinstein.loopgain.domain.model

/**
 * A logged session. Only what the deck's own rules say to keep: which four cards were drawn
 * (so they can be held back for a year of monthly sessions) and one session-level line — never
 * anyone's individual answers.
 */
data class SessionHistoryEntry(
    val sessionNumber: Int,
    /** Epoch milliseconds, so callers can format without a platform date dependency. */
    val loggedAtEpochMillis: Long,
    val participantCount: Int,
    val plannedSeconds: Int,
    val usedSeconds: Int,
    val drawnCardIds: List<String>,
    val reflectionNote: String,
)
