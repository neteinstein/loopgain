package org.neteinstein.loopgain

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.neteinstein.loopgain.data.repository.DefaultCardRepository
import org.neteinstein.loopgain.domain.model.CardCategory
import org.neteinstein.loopgain.domain.model.CardLevel
import org.neteinstein.loopgain.domain.model.SessionConfig
import org.neteinstein.loopgain.domain.model.SessionStage
import org.neteinstein.loopgain.domain.model.SessionTurnPhase
import org.neteinstein.loopgain.domain.session.SessionEngine
import org.neteinstein.loopgain.domain.session.SessionState

class SessionEngineTest {

    private val repository = DefaultCardRepository()
    private val config = SessionConfig()
    private val engine = SessionEngine(repository, config)

    private fun readyState(): SessionState {
        var s = SessionState()
        s = engine.renamePerson(s, s.people[0].id, "Alice")
        s = engine.renamePerson(s, s.people[1].id, "Bob")
        s = engine.renamePerson(s, s.people[2].id, "Cate")
        return s
    }

    @Test
    fun addRemoveRenamePerson() {
        var s = SessionState()
        val before = s.people.size
        s = engine.addPerson(s)
        assertEquals(before + 1, s.people.size)

        val id = s.people.last().id
        s = engine.renamePerson(s, id, "Dana")
        assertEquals("Dana", s.people.last().name)

        s = engine.removePerson(s, id)
        assertEquals(before, s.people.size)
    }

    @Test
    fun isReadyRequiresAtLeastTwoNamedPeople() {
        var s = SessionState()
        assertFalse(s.isReady) // default people all blank
        s = engine.renamePerson(s, s.people[0].id, "Alice")
        assertFalse(s.isReady)
        s = engine.renamePerson(s, s.people[1].id, "Bob")
        assertTrue(s.isReady)
    }

    @Test
    fun startSessionDrawsAllFourAndStartsClock() {
        val s = engine.startSession(readyState(), heldBackIds = emptySet(), random = Random(1))
        assertEquals(SessionStage.DRAW, s.stage)
        assertTrue(s.isAllDrawn)
        assertTrue(s.running)
        assertEquals(config.suggestedMinutes(3), s.sessionSecondsLeft / 60)
    }

    @Test
    fun startSessionIsNoOpWhenNotReady() {
        val notReady = SessionState()
        val s = engine.startSession(notReady, heldBackIds = emptySet())
        assertEquals(SessionStage.SETUP, s.stage)
        assertTrue(s.drawn.isEmpty())
    }

    @Test
    fun mottoDrawIgnoresLevelSelection() {
        var s = readyState()
        // Motto has no entry in `levels` at all, and drawOne must not require one.
        assertNull(s.levels[CardCategory.MOTTO])
        s = engine.drawOne(s, CardCategory.MOTTO, random = Random(2))
        assertEquals(null, s.drawn.getValue(CardCategory.MOTTO).level)
    }

    @Test
    fun drawOneRespectsSelectedLevel() {
        var s = readyState()
        s = engine.setLevel(s, CardCategory.PERSONAL_QUESTION, CardLevel.THREE)
        s = engine.drawOne(s, CardCategory.PERSONAL_QUESTION, random = Random(3))
        assertEquals(CardLevel.THREE, s.drawn.getValue(CardCategory.PERSONAL_QUESTION).level)
    }

    @Test
    fun drawOneFallsBackWhenAllCardsAreHeldBack() {
        val allPositiveIds = repository.byCategory(CardCategory.POSITIVE_REINFORCEMENT).map { it.id }.toSet()
        var s = readyState().copy(heldBackIds = allPositiveIds) // holding back the entire category

        val poolBeforeDraw = repository.pool(CardCategory.POSITIVE_REINFORCEMENT, s.levels[CardCategory.POSITIVE_REINFORCEMENT], s.heldBackIds)
        assertEquals(12, poolBeforeDraw.heldBack)
        assertTrue(poolBeforeDraw.drawable.isNotEmpty(), "a card must still be drawable even if every card was held back")

        s = engine.drawOne(s, CardCategory.POSITIVE_REINFORCEMENT, random = Random(4))
        assertTrue(s.drawn.containsKey(CardCategory.POSITIVE_REINFORCEMENT))
    }

    @Test
    fun readStageWalksDrawnCardsThenMovesToWrite() {
        var s = engine.startSession(readyState(), heldBackIds = emptySet(), random = Random(5))
        s = engine.enterRead(s)
        assertEquals(SessionStage.READ, s.stage)
        assertEquals(0, s.readIndex)

        repeat(3) { s = engine.advanceRead(s) }
        assertEquals(SessionStage.READ, s.stage)
        assertEquals(3, s.readIndex)

        s = engine.advanceRead(s) // past the last card
        assertEquals(SessionStage.WRITE, s.stage)
        assertEquals(config.writeMinutes * 60, s.writeSecondsLeft)
    }

    @Test
    fun roundsAdvanceThroughFeedbackAndPersonalPhaseThenNextPerson() {
        var s = engine.startRounds(readyState())
        assertEquals(SessionTurnPhase.FEEDBACK, s.turnPhase)
        assertEquals(0, s.turnIndex)

        s = engine.advanceRounds(s) // feedback -> personal, same person
        assertEquals(SessionTurnPhase.PERSONAL, s.turnPhase)
        assertEquals(0, s.turnIndex)

        s = engine.advanceRounds(s) // personal -> next person, feedback again
        assertEquals(SessionTurnPhase.FEEDBACK, s.turnPhase)
        assertEquals(1, s.turnIndex)
    }

    @Test
    fun roundsMoveToReflectAfterTheLastPerson() {
        var s = engine.startRounds(readyState()) // 3 named people
        repeat(2) {
            s = engine.advanceRounds(s) // feedback -> personal
            s = engine.advanceRounds(s) // personal -> next
        }
        assertEquals(2, s.turnIndex)
        s = engine.advanceRounds(s) // feedback -> personal (last person)
        s = engine.advanceRounds(s) // personal -> no one left -> reflect
        assertEquals(SessionStage.REFLECT, s.stage)
        assertFalse(s.running)
    }

    @Test
    fun tickCountsDownSessionAndTurnClocksButOnlyWhileRunning() {
        var s = engine.startRounds(readyState()).copy(running = true, sessionSecondsLeft = 100, turnSecondsLeft = 10)
        s = engine.tick(s, deltaSeconds = 5)
        assertEquals(95, s.sessionSecondsLeft)
        assertEquals(5, s.turnSecondsLeft)

        s = s.copy(running = false)
        val unchanged = engine.tick(s, deltaSeconds = 5)
        assertEquals(s, unchanged)
    }

    @Test
    fun turnClockIsAllowedToGoNegativeWhenOverBudget() {
        var s = engine.startRounds(readyState()).copy(running = true, turnSecondsLeft = 2)
        s = engine.tick(s, deltaSeconds = 5)
        assertEquals(-3, s.turnSecondsLeft)
    }

    @Test
    fun writeClockFloorsAtZero() {
        var s = engine.startWrite(readyState()).copy(running = true, writeSecondsLeft = 2)
        s = engine.tick(s, deltaSeconds = 5)
        assertEquals(0, s.writeSecondsLeft)
    }

    @Test
    fun resetKeepsRosterAndLevelsButClearsTheRunSession() {
        var s = engine.startSession(readyState(), heldBackIds = emptySet(), random = Random(6))
        s = engine.setReflection(s, "Went well")
        s = engine.logSession(s)
        assertEquals(SessionStage.DONE, s.stage)

        s = engine.reset(s)
        assertEquals(SessionStage.SETUP, s.stage)
        assertTrue(s.drawn.isEmpty())
        assertEquals("", s.reflection)
        assertEquals(3, s.namedPeople.size) // roster survives a reset
    }
}
