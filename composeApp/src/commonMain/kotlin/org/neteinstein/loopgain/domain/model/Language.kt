package org.neteinstein.loopgain.domain.model

/**
 * The app's display language. The deck itself (docs/LoopGain - Questions.xlsx) and the session
 * flow copy are only authored in [EN] and [PT] — [ES] and [FR] are UI-selectable but fall back to
 * [EN] wherever content has no translation yet. [QuestionCard.text] and every `SessionCopy`
 * function apply that fallback, so nothing here needs to special-case [ES]/[FR] itself.
 */
enum class Language(val nativeName: String) {
    EN("English"),
    PT("Português"),
    ES("Español"),
    FR("Français"),
}
