package org.neteinstein.loopgain

import kotlin.test.Test
import kotlin.test.assertEquals
import org.neteinstein.loopgain.domain.model.SessionConfig

class SessionConfigTest {

    private val config = SessionConfig()

    @Test
    fun suggestedMinutesMatchesTheDeckInstructions() {
        // 10 min per person + 10 for start/close, 5 people = 60 min — the deck's own example.
        assertEquals(60, config.suggestedMinutes(5))
    }

    @Test
    fun clampsParticipantsBelowTwo() {
        assertEquals(config.suggestedMinutes(2), config.suggestedMinutes(1))
        assertEquals(config.suggestedMinutes(2), config.suggestedMinutes(0))
    }

    @Test
    fun clampsParticipantsAboveFifteen() {
        assertEquals(config.suggestedMinutes(15), config.suggestedMinutes(20))
    }

    @Test
    fun writeAndOverheadDefaultsMatchTheDeck() {
        assertEquals(5, config.writeMinutes)
        assertEquals(10, config.overheadMinutes)
        assertEquals(10, config.minutesPerPerson)
    }
}
