package org.neteinstein.loopgain.domain.session

import org.neteinstein.loopgain.domain.model.CardCategory
import org.neteinstein.loopgain.domain.model.CardLevel
import org.neteinstein.loopgain.domain.model.Person
import org.neteinstein.loopgain.domain.model.QuestionCard
import org.neteinstein.loopgain.domain.model.SessionStage
import org.neteinstein.loopgain.domain.model.SessionTurnPhase

/**
 * Canonical, layout-agnostic state for one live LoopGain session. Both the phone and tablet UIs
 * render from the same [SessionState] via [SessionEngine] — only presentation differs.
 */
data class SessionState(
    val stage: SessionStage = SessionStage.SETUP,
    val people: List<Person> = listOf(Person(1, ""), Person(2, ""), Person(3, "")),
    val nextPersonId: Int = 4,
    /** No entry for [CardCategory.MOTTO] — that category has no depth to pick. */
    val levels: Map<CardCategory, CardLevel> = mapOf(
        CardCategory.POSITIVE_REINFORCEMENT to CardLevel.TWO,
        CardCategory.IMPROVEMENTS to CardLevel.TWO,
        CardCategory.PERSONAL_QUESTION to CardLevel.TWO,
    ),
    val drawn: Map<CardCategory, QuestionCard> = emptyMap(),
    /** Card ids held back for this session, snapshotted from history when the session starts. */
    val heldBackIds: Set<String> = emptySet(),
    val readIndex: Int = 0,
    val turnIndex: Int = 0,
    val turnPhase: SessionTurnPhase = SessionTurnPhase.FEEDBACK,
    val sessionSecondsLeft: Int = 0,
    val writeSecondsLeft: Int = 0,
    /** Per-person turn budget. Allowed to go negative — running over is shown, not clamped. */
    val turnSecondsLeft: Int = 0,
    val running: Boolean = false,
    val reflection: String = "",
    val dismissedHints: Set<SessionStage> = emptySet(),
) {
    val namedPeople: List<String>
        get() = people.map { it.name.trim() }.filter { it.isNotEmpty() }

    val isReady: Boolean
        get() = namedPeople.size >= 2

    val isAllDrawn: Boolean
        get() = drawn.size == CardCategory.ORDER.size

    val drawnInOrder: List<QuestionCard>
        get() = CardCategory.ORDER.mapNotNull { drawn[it] }

    val isWarning: Boolean
        get() = sessionSecondsLeft in 1..300
}
