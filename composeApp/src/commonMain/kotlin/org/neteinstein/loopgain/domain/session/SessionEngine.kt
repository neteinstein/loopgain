package org.neteinstein.loopgain.domain.session

import kotlin.random.Random
import org.neteinstein.loopgain.data.repository.CardRepository
import org.neteinstein.loopgain.domain.model.CardCategory
import org.neteinstein.loopgain.domain.model.Person
import org.neteinstein.loopgain.domain.model.SessionConfig
import org.neteinstein.loopgain.domain.model.SessionStage
import org.neteinstein.loopgain.domain.model.SessionTurnPhase

/**
 * Pure state transitions for a session — no coroutines, no Compose, no DI. A ViewModel wraps this
 * in a [kotlinx.coroutines.flow.StateFlow] and drives [tick] from a timer; everything else is a
 * plain function of the current [SessionState], which is what makes it straightforward to unit
 * test and to share between the phone and tablet UIs.
 */
class SessionEngine(
    private val cardRepository: CardRepository,
    private val config: SessionConfig,
) {
    fun totalSeconds(state: SessionState): Int =
        config.suggestedMinutes(state.namedPeople.size.coerceAtLeast(1)) * 60

    fun addPerson(state: SessionState): SessionState = state.copy(
        people = state.people + Person(state.nextPersonId, ""),
        nextPersonId = state.nextPersonId + 1,
    )

    fun removePerson(state: SessionState, id: Int): SessionState =
        state.copy(people = state.people.filterNot { it.id == id })

    fun renamePerson(state: SessionState, id: Int, name: String): SessionState = state.copy(
        people = state.people.map { if (it.id == id) it.copy(name = name) else it },
    )

    fun setLevel(state: SessionState, category: CardCategory, level: org.neteinstein.loopgain.domain.model.CardLevel): SessionState {
        if (cardRepository.levelsFor(category).isEmpty()) return state // e.g. Motto: no-op
        return state.copy(levels = state.levels + (category to level), drawn = emptyMap())
    }

    /** Starts the session: draws all four cards and moves to [SessionStage.DRAW]. */
    fun startSession(state: SessionState, heldBackIds: Set<String>, random: Random = Random.Default): SessionState {
        if (!state.isReady) return state
        val withHeldBack = state.copy(heldBackIds = heldBackIds)
        val drawn = drawAllCards(withHeldBack, random)
        return withHeldBack.copy(
            stage = SessionStage.DRAW,
            drawn = drawn,
            running = true,
            sessionSecondsLeft = totalSeconds(state),
            readIndex = 0,
            turnIndex = 0,
            turnPhase = SessionTurnPhase.FEEDBACK,
        )
    }

    fun drawOne(state: SessionState, category: CardCategory, random: Random = Random.Default): SessionState {
        val level = state.levels[category]
        val card = cardRepository.random(category, level, state.heldBackIds, random)
        return state.copy(drawn = state.drawn + (category to card))
    }

    fun drawAll(state: SessionState, random: Random = Random.Default): SessionState =
        state.copy(drawn = drawAllCards(state, random))

    private fun drawAllCards(state: SessionState, random: Random): Map<CardCategory, org.neteinstein.loopgain.domain.model.QuestionCard> =
        CardCategory.ORDER.associateWith { category ->
            cardRepository.random(category, state.levels[category], state.heldBackIds, random)
        }

    fun enterRead(state: SessionState): SessionState =
        if (state.isAllDrawn) state.copy(stage = SessionStage.READ, readIndex = 0) else state

    fun previousReadCard(state: SessionState): SessionState =
        state.copy(readIndex = (state.readIndex - 1).coerceAtLeast(0))

    /** Advances to the next read card, or into [SessionStage.WRITE] once the last one was shown. */
    fun advanceRead(state: SessionState): SessionState =
        if (state.readIndex + 1 < state.drawnInOrder.size) {
            state.copy(readIndex = state.readIndex + 1)
        } else {
            startWrite(state)
        }

    /** Tablet-style flow: go straight from drawing to writing without a dedicated read stage. */
    fun startWrite(state: SessionState): SessionState = state.copy(
        stage = SessionStage.WRITE,
        writeSecondsLeft = config.writeMinutes * 60,
    )

    fun startRounds(state: SessionState): SessionState = state.copy(
        stage = SessionStage.ROUNDS,
        turnIndex = 0,
        turnPhase = SessionTurnPhase.FEEDBACK,
        turnSecondsLeft = config.minutesPerPerson * 60,
    )

    /** Feedback phase → personal phase → next person, or → [SessionStage.REFLECT] when done. */
    fun advanceRounds(state: SessionState): SessionState = when {
        state.turnPhase == SessionTurnPhase.FEEDBACK ->
            state.copy(turnPhase = SessionTurnPhase.PERSONAL)

        state.turnIndex + 1 >= state.namedPeople.size ->
            state.copy(stage = SessionStage.REFLECT, running = false)

        else -> state.copy(
            turnIndex = state.turnIndex + 1,
            turnPhase = SessionTurnPhase.FEEDBACK,
            turnSecondsLeft = config.minutesPerPerson * 60,
        )
    }

    fun skipToReflect(state: SessionState): SessionState =
        state.copy(stage = SessionStage.REFLECT, running = false)

    fun setReflection(state: SessionState, text: String): SessionState =
        state.copy(reflection = text)

    fun logSession(state: SessionState): SessionState =
        state.copy(stage = SessionStage.DONE, running = false)

    /** Keeps the roster and depth levels; clears everything specific to the session just run. */
    fun reset(state: SessionState): SessionState = state.copy(
        stage = SessionStage.SETUP,
        drawn = emptyMap(),
        heldBackIds = emptySet(),
        running = false,
        readIndex = 0,
        turnIndex = 0,
        turnPhase = SessionTurnPhase.FEEDBACK,
        sessionSecondsLeft = 0,
        writeSecondsLeft = 0,
        turnSecondsLeft = 0,
        reflection = "",
    )

    fun dismissHint(state: SessionState, stage: SessionStage): SessionState =
        state.copy(dismissedHints = state.dismissedHints + stage)

    /** One tick of wall-clock time. No-op while paused (e.g. during setup, reflect or done). */
    fun tick(state: SessionState, deltaSeconds: Int): SessionState {
        if (!state.running) return state
        var next = state.copy(sessionSecondsLeft = (state.sessionSecondsLeft - deltaSeconds).coerceAtLeast(0))
        if (state.stage == SessionStage.WRITE) {
            next = next.copy(writeSecondsLeft = (next.writeSecondsLeft - deltaSeconds).coerceAtLeast(0))
        }
        if (state.stage == SessionStage.ROUNDS) {
            next = next.copy(turnSecondsLeft = next.turnSecondsLeft - deltaSeconds)
        }
        return next
    }
}
