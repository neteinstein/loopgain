package org.neteinstein.loopgain.domain.model

/**
 * One of the 48 cards on the deck. [level] is null exactly for [CardCategory.MOTTO] — that
 * category has no depth and the UI must not default it to a level.
 *
 * `_` inside [en] / [pt] is a placeholder for the teammate being discussed and must be rendered
 * as an underscored blank, not substituted or stripped.
 */
data class QuestionCard(
    val id: String,
    val category: CardCategory,
    val level: CardLevel?,
    val en: String,
    val pt: String,
    val es: String,
    val fr: String,
) {
    fun text(language: Language): String = when (language) {
        Language.EN -> en
        Language.PT -> pt
        Language.ES -> es
        Language.FR -> fr
    }

    /** Short code matching the printed deck's own notation, e.g. "P-03". */
    val code: String
        get() = "${category.codePrefix}-${id.substringAfterLast('_')}"
}
