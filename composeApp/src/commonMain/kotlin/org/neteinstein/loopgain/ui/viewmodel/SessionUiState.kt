package org.neteinstein.loopgain.ui.viewmodel

import kotlin.math.abs
import org.neteinstein.loopgain.domain.model.CardCategory
import org.neteinstein.loopgain.domain.model.CardLevel
import org.neteinstein.loopgain.domain.model.Language
import org.neteinstein.loopgain.domain.model.QuestionCard
import org.neteinstein.loopgain.domain.model.SessionConfig
import org.neteinstein.loopgain.domain.model.SessionStage
import org.neteinstein.loopgain.domain.model.SessionTurnPhase
import org.neteinstein.loopgain.domain.session.SessionState

/**
 * Formatted, [Language]-resolved view of a [SessionState]. Colour-free like the domain model — a
 * screen maps [CardFaceUi.category] / [CardFaceUi.level] to a palette via `ui/theme`, this class
 * never picks colours itself. Shared by the phone and tablet screens.
 */
data class SessionUiState(
    val stage: SessionStage,
    val language: Language,
    val people: List<PersonRowUi>,
    val depthPickers: List<CategoryDepthUi>,
    val mathLine: String,
    val totalLabel: String,
    val startLabel: String,
    val canStart: Boolean,
    val piles: List<PileUi>,
    val drawnCount: Int,
    val canProceedFromDraw: Boolean,
    val drawnCards: List<CardFaceUi>,
    val readCard: CardFaceUi?,
    val readFootLabel: String,
    val readNextLabel: String,
    val writeClock: String,
    val writeUrgent: Boolean,
    val currentPersonName: String,
    val nextPersonName: String,
    val turnPhaseLabel: String,
    val turnClock: String,
    val turnOver: Boolean,
    val turnNote: String,
    val activeCards: List<CardFaceUi>,
    val order: List<OrderRowUi>,
    val orderFootLabel: String,
    val advanceLabel: String,
    val isWarning: Boolean,
    val reflection: String,
    val quickReflections: List<String>,
    val doneSummary: String,
    val heldBackNote: String,
    val hint: String?,
    val sessionClock: String,
    val sessionRunning: Boolean,
)

data class PersonRowUi(val id: Int, val index: Int, val name: String)

data class CategoryDepthUi(
    val category: CardCategory,
    val label: String,
    val hasLevels: Boolean,
    val selectedLevel: CardLevel?,
    val levels: List<CardLevel>,
)

data class PileUi(
    val category: CardCategory,
    val label: String,
    val isDrawn: Boolean,
    val code: String?,
    val level: CardLevel?,
    val availableCount: Int,
    val heldBackCount: Int,
)

data class CardFaceUi(
    val category: CardCategory,
    val label: String,
    val code: String,
    val text: String,
    val level: CardLevel?,
    val about: String,
)

data class OrderRowUi(val index: Int, val name: String, val isCurrent: Boolean, val isDone: Boolean, val tag: String)

private fun formatClock(totalSeconds: Int): String {
    val negative = totalSeconds < 0
    val n = abs(totalSeconds)
    val minutes = n / 60
    val seconds = n % 60
    val secondsStr = if (seconds < 10) "0$seconds" else "$seconds"
    return (if (negative) "−" else "") + "$minutes:$secondsStr"
}

private fun QuestionCard.toCardFaceUi(language: Language): CardFaceUi = CardFaceUi(
    category = category,
    label = category.displayName(language),
    code = code,
    text = text(language),
    level = level,
    about = SessionCopy.about(category, language),
)

fun SessionState.toUiState(config: SessionConfig, cardRepository: org.neteinstein.loopgain.data.repository.CardRepository): SessionUiState {
    val language = config.language
    val ready = isReady
    val total = totalSecondsFor(this, config)
    val drawnList = drawnInOrder.map { it.toCardFaceUi(language) }
    val readCards = drawnList
    val readCard = readCards.getOrNull(readIndex.coerceIn(0, (readCards.size - 1).coerceAtLeast(0)))
    val names = namedPeople
    val currentIndex = turnIndex.coerceIn(0, (names.size - 1).coerceAtLeast(0))
    val current = names.getOrNull(currentIndex) ?: SessionCopy.dash
    val next = names.getOrNull(currentIndex + 1)

    return SessionUiState(
        stage = stage,
        language = language,
        people = people.mapIndexed { i, p -> PersonRowUi(p.id, i + 1, p.name) },
        depthPickers = CardCategory.ORDER.map { category ->
            val levels = cardRepository.levelsFor(category)
            CategoryDepthUi(
                category = category,
                label = category.displayName(language),
                hasLevels = levels.isNotEmpty(),
                selectedLevel = levels.let { this.levels[category] },
                levels = levels,
            )
        },
        mathLine = if (ready) SessionCopy.mathLine(config, names.size, language) else SessionCopy.addNames(language),
        totalLabel = if (ready) "${total / 60}:00" else SessionCopy.dash,
        startLabel = if (ready) SessionCopy.startLabel(language) else SessionCopy.addNamesToStart(language),
        canStart = ready,
        piles = CardCategory.ORDER.map { category ->
            val card = drawn[category]
            val pool = cardRepository.pool(category, levels[category], heldBackIds)
            PileUi(
                category = category,
                label = category.displayName(language),
                isDrawn = card != null,
                code = card?.code,
                level = levels[category],
                availableCount = pool.drawable.size,
                heldBackCount = pool.heldBack,
            )
        },
        drawnCount = drawn.size,
        canProceedFromDraw = isAllDrawn,
        drawnCards = drawnList,
        readCard = readCard,
        readFootLabel = SessionCopy.readFoot(readIndex, readCards.size, language),
        readNextLabel = if (readIndex + 1 >= readCards.size) SessionCopy.writeArrow(language) else SessionCopy.nextCard(language),
        writeClock = formatClock(writeSecondsLeft),
        writeUrgent = writeSecondsLeft in 1..30,
        currentPersonName = current,
        nextPersonName = next ?: SessionCopy.closing(language),
        turnPhaseLabel = SessionCopy.turnPhaseLabel(turnPhase, language),
        turnClock = formatClock(turnSecondsLeft),
        turnOver = turnSecondsLeft < 0,
        turnNote = SessionCopy.turnNote(turnSecondsLeft < 0, config.minutesPerPerson, language),
        activeCards = drawnList.filter { card ->
            if (turnPhase == SessionTurnPhase.FEEDBACK) {
                card.category == CardCategory.POSITIVE_REINFORCEMENT || card.category == CardCategory.IMPROVEMENTS
            } else {
                card.category == CardCategory.PERSONAL_QUESTION || card.category == CardCategory.MOTTO
            }
        },
        order = names.mapIndexed { i, name ->
            val done = i < turnIndex
            val cur = i == turnIndex
            OrderRowUi(
                index = i + 1,
                name = name,
                isCurrent = cur,
                isDone = done,
                tag = if (done) SessionCopy.done(language) else if (cur) SessionCopy.now(language) else "",
            )
        },
        orderFootLabel = SessionCopy.orderFoot(turnIndex, names.size.coerceAtLeast(1), language),
        advanceLabel = if (turnPhase == SessionTurnPhase.FEEDBACK) {
            SessionCopy.respondsArrow(current, language)
        } else if (next != null) {
            SessionCopy.nextArrow(next, language)
        } else {
            SessionCopy.toClosingArrow(language)
        },
        isWarning = isWarning,
        reflection = reflection,
        quickReflections = SessionCopy.quickReflections(language),
        doneSummary = SessionCopy.doneSummary(
            names.size,
            formatClock(total - sessionSecondsLeft).removePrefix("−"),
            "${total / 60}:00",
            turnIndex + 1,
            language,
        ),
        heldBackNote = SessionCopy.heldBackNote(language),
        hint = if (stage in dismissedHints) null else SessionCopy.hint(stage, config, language),
        sessionClock = formatClock(sessionSecondsLeft),
        sessionRunning = running,
    )
}

private fun totalSecondsFor(state: SessionState, config: SessionConfig): Int =
    config.suggestedMinutes(state.namedPeople.size.coerceAtLeast(1)) * 60
