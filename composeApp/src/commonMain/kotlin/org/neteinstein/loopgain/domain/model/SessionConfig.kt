package org.neteinstein.loopgain.domain.model

/**
 * Timer math for a session, straight from the printed deck's Instructions card and the workshop
 * talk: ten minutes per person plus ten for start/close, five silent minutes to write.
 */
data class SessionConfig(
    val minutesPerPerson: Int = 10,
    val overheadMinutes: Int = 10,
    val writeMinutes: Int = 5,
    val avoidRepeats: Boolean = true,
    val language: Language = Language.EN,
) {
    /** Clamps [participants] to the deck's stated 2–15 range and the result to 5–180 minutes. */
    fun suggestedMinutes(participants: Int): Int {
        val clamped = participants.coerceIn(PARTICIPANTS_RANGE)
        val minutes = minutesPerPerson * clamped + overheadMinutes
        return minutes.coerceIn(DURATION_MINUTES_RANGE)
    }

    companion object {
        val PARTICIPANTS_RANGE = 2..15
        val DURATION_MINUTES_RANGE = 5..180
    }
}
