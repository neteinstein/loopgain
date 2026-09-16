package org.neteinstein.loopgain.ui.viewmodel

import org.neteinstein.loopgain.data.source.bundledDeck
import org.neteinstein.loopgain.domain.model.CardCategory
import org.neteinstein.loopgain.domain.model.Language
import org.neteinstein.loopgain.domain.model.QuestionCard
import org.neteinstein.loopgain.domain.model.SessionHistoryEntry

/**
 * Tablet-only "History" screen state: a facilitator-facing log of past sessions plus an honest,
 * per-[CardCategory] draw-frequency count across them.
 *
 * There is no themed clustering here (no "Ownership & handover"-style labels) — that reading of
 * the original design mockup invented categories with no data behind them. This reports only
 * what [SessionHistoryEntry.drawnCardIds] actually says, one count per printed-deck category.
 */
data class HistoryCardUi(val category: CardCategory, val code: String, val text: String)

data class HistoryEntryUi(
    val sessionNumber: Int,
    val meta: String,
    val cards: List<HistoryCardUi>,
    val reflectionNote: String,
)

data class CategoryFrequencyUi(
    val category: CardCategory,
    val label: String,
    val count: Int,
    /** Denominator for rendering a bar of dots — the highest count among all categories shown. */
    val maxCount: Int,
)

data class SessionHistoryUiState(
    val entries: List<HistoryEntryUi>,
    val categoryFrequency: List<CategoryFrequencyUi>,
)

private val cardsById: Map<String, QuestionCard> = bundledDeck.associateBy { it.id }

/** Formats logged [SessionHistoryEntry] rows (most-recent-first, as [org.neteinstein.loopgain.data.repository.SessionHistoryRepository.all] returns them) for the tablet's History screen. */
fun List<SessionHistoryEntry>.toHistoryUiState(language: Language): SessionHistoryUiState {
    val entries = map { entry ->
        val cards = entry.drawnCardIds.mapNotNull { id -> cardsById[id] }
        HistoryEntryUi(
            sessionNumber = entry.sessionNumber,
            meta = SessionCopy.historyMeta(entry.participantCount, entry.usedSeconds / 60, language),
            cards = cards.map { HistoryCardUi(category = it.category, code = it.code, text = it.text(language)) },
            reflectionNote = entry.reflectionNote.ifBlank { SessionCopy.noReflectionNote(language) },
        )
    }

    val counts = CardCategory.ORDER.associateWith { category ->
        sumOf { entry -> entry.drawnCardIds.count { id -> cardsById[id]?.category == category } }
    }
    val maxCount = (counts.values.maxOrNull() ?: 0).coerceAtLeast(1)
    val frequency = CardCategory.ORDER.map { category ->
        CategoryFrequencyUi(
            category = category,
            label = category.displayName(language),
            count = counts[category] ?: 0,
            maxCount = maxCount,
        )
    }

    return SessionHistoryUiState(entries = entries, categoryFrequency = frequency)
}
