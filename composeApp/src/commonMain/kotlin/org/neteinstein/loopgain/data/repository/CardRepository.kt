package org.neteinstein.loopgain.data.repository

import kotlin.random.Random
import org.neteinstein.loopgain.data.source.bundledDeck
import org.neteinstein.loopgain.domain.model.CardCategory
import org.neteinstein.loopgain.domain.model.CardLevel
import org.neteinstein.loopgain.domain.model.QuestionCard

/**
 * A drawable pool for one category: cards still fresh, and how many were held back because they
 * were used in a recent session.
 */
data class CardPool(
    val drawable: List<QuestionCard>,
    val heldBack: Int,
)

interface CardRepository {
    fun byCategory(category: CardCategory): List<QuestionCard>
    fun byCategoryAndLevel(category: CardCategory, level: CardLevel?): List<QuestionCard>

    /** Levels that actually exist for [category] — empty for [CardCategory.MOTTO]. */
    fun levelsFor(category: CardCategory): List<CardLevel>

    /**
     * The cards [category] can draw from right now: cards at [level] (or all of them for a
     * category with no levels) with [excludingIds] removed. Falls back one step at a time — to
     * the unfiltered category, then to the full category ignoring the exclusion — so a pool is
     * never empty just because recent history or a level filter is strict.
     */
    fun pool(category: CardCategory, level: CardLevel?, excludingIds: Set<String>): CardPool

    fun random(category: CardCategory, level: CardLevel?, excludingIds: Set<String>, random: Random = Random.Default): QuestionCard
}

class DefaultCardRepository(
    private val deck: List<QuestionCard> = bundledDeck,
) : CardRepository {

    private val byCategoryCache: Map<CardCategory, List<QuestionCard>> = deck.groupBy { it.category }

    override fun byCategory(category: CardCategory): List<QuestionCard> =
        byCategoryCache[category].orEmpty()

    override fun byCategoryAndLevel(category: CardCategory, level: CardLevel?): List<QuestionCard> =
        byCategory(category).filter { it.level == level }

    override fun levelsFor(category: CardCategory): List<CardLevel> =
        byCategory(category).mapNotNull { it.level }.distinct().sorted()

    override fun pool(category: CardCategory, level: CardLevel?, excludingIds: Set<String>): CardPool {
        val all = byCategory(category)
        val fresh = all.filterNot { it.id in excludingIds }
        val basis = fresh.ifEmpty { all }
        val levelled = if (level != null) basis.filter { it.level == level } else basis
        val drawable = levelled.ifEmpty { basis }
        return CardPool(drawable = drawable, heldBack = all.size - fresh.size)
    }

    override fun random(
        category: CardCategory,
        level: CardLevel?,
        excludingIds: Set<String>,
        random: Random,
    ): QuestionCard {
        val drawable = pool(category, level, excludingIds).drawable
        return drawable[random.nextInt(drawable.size)]
    }
}
