package org.neteinstein.loopgain.domain.model

/**
 * Depth of a question, shown on the printed card as dots (not stars — the spreadsheet's
 * asterisks are its own notation). Motto cards have no level at all, so [QuestionCard.level]
 * is nullable rather than defaulting to [ONE].
 */
enum class CardLevel(val dots: Int) {
    ONE(1),
    TWO(2),
    THREE(3),
}
