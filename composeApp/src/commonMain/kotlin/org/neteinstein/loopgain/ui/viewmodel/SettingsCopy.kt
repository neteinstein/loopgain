package org.neteinstein.loopgain.ui.viewmodel

import org.neteinstein.loopgain.domain.model.AppTheme
import org.neteinstein.loopgain.domain.model.Language

/** UI copy for the Settings screen. Same PT-special/English-fallback rule as [SessionCopy]. */
internal object SettingsCopy {
    fun title(language: Language): String = if (language == Language.PT) "Definições" else "Settings"

    fun appPreferences(language: Language): String =
        if (language == Language.PT) "PREFERÊNCIAS DA APP" else "APP PREFERENCES"

    fun theme(language: Language): String = if (language == Language.PT) "Tema" else "Theme"

    fun themeOption(theme: AppTheme, language: Language): String = when (theme) {
        AppTheme.LIGHT -> if (language == Language.PT) "Claro" else "Light"
        AppTheme.DARK -> if (language == Language.PT) "Escuro" else "Dark"
        AppTheme.SYSTEM -> if (language == Language.PT) "Sistema" else "System"
    }

    fun language(language: Language): String = if (language == Language.PT) "Idioma" else "Language"

    fun chooseLanguage(language: Language): String =
        if (language == Language.PT) "Escolher idioma" else "Choose language"

    fun cardManagement(language: Language): String =
        if (language == Language.PT) "GESTÃO DE CARTAS" else "CARD MANAGEMENT"

    fun cardManagementDescription(language: Language): String = if (language == Language.PT) {
        "Cartas usadas nas últimas doze sessões ficam reservadas automaticamente, para que nada se repita cedo demais."
    } else {
        "Cards drawn in the last twelve sessions are held back automatically so nothing repeats too soon."
    }

    fun heldBackStatus(count: Int, language: Language): String = when {
        language == Language.PT && count == 0 -> "Nenhuma carta está atualmente reservada."
        language == Language.PT -> "$count " + if (count == 1) "carta reservada." else "cartas reservadas."
        count == 0 -> "No cards are currently held back."
        else -> "$count " + if (count == 1) "card held back." else "cards held back."
    }

    fun resetHeldBackCards(language: Language): String =
        if (language == Language.PT) "REPOR CARTAS RESERVADAS" else "RESET HELD-BACK CARDS"

    fun resetConfirmTitle(language: Language): String =
        if (language == Language.PT) "Repor cartas reservadas?" else "Reset held-back cards?"

    fun resetConfirmBody(language: Language): String = if (language == Language.PT) {
        "Isto esquece que cartas foram usadas recentemente, para que todas fiquem disponíveis outra vez. Também apaga o teu registo de sessões."
    } else {
        "This forgets which cards were used recently, so every card becomes available again. It also clears your session log."
    }

    fun reset(language: Language): String = if (language == Language.PT) "REPOR" else "RESET"
    fun cancel(language: Language): String = if (language == Language.PT) "CANCELAR" else "CANCEL"

    fun about(language: Language): String = if (language == Language.PT) "SOBRE" else "ABOUT"

    fun version(versionName: String, language: Language): String =
        if (language == Language.PT) "Versão $versionName" else "Version $versionName"

    fun tagline(language: Language): String = if (language == Language.PT) {
        "Levando a tua equipa da zona de conforto para a zona de confiança!"
    } else {
        "Taking your team from the comfort zone to the trust zone!"
    }

    fun footer(language: Language): String = if (language == Language.PT) {
        "O LoopGain Teams faz parte da caixa de ferramentas LoopGain, por Pedro Vicente"
    } else {
        "LoopGain Teams is part of the toolbox LoopGain, by Pedro Vicente"
    }
}
