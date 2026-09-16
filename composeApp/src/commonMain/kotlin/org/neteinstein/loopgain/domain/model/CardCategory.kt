package org.neteinstein.loopgain.domain.model

/**
 * The four categories on the physical LoopGain deck, 12 cards each. Colour-free by design —
 * palettes live in `ui/theme`, never on the domain model.
 */
enum class CardCategory(
    val displayNameEn: String,
    val displayNamePt: String,
    /** Prefix used in a card's short code (e.g. "P-03"), matching the printed deck. */
    val codePrefix: String,
) {
    MOTTO("Motto", "Mote", "M"),
    POSITIVE_REINFORCEMENT("Positive Reinforcement", "Reforço Positivo", "P"),
    IMPROVEMENTS("Improvements", "Melhorias", "I"),
    PERSONAL_QUESTION("Personal Question", "Pergunta Pessoal", "Q");

    fun displayName(language: Language): String = when (language) {
        Language.EN -> displayNameEn
        Language.PT -> displayNamePt
    }

    companion object {
        /** Deck order: the order cards are drawn and read in every session. */
        val ORDER = listOf(MOTTO, POSITIVE_REINFORCEMENT, IMPROVEMENTS, PERSONAL_QUESTION)
    }
}
