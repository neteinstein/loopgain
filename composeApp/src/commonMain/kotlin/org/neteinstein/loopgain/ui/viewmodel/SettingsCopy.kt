package org.neteinstein.loopgain.ui.viewmodel

import org.neteinstein.loopgain.domain.model.AppTheme
import org.neteinstein.loopgain.domain.model.Language

/** UI copy for the Settings screen, authored in English, Portuguese, Spanish and French. */
internal object SettingsCopy {
    fun title(language: Language): String = when (language) {
        Language.EN -> "Settings"
        Language.PT -> "Definições"
        Language.ES -> "Ajustes"
        Language.FR -> "Paramètres"
    }

    fun appPreferences(language: Language): String = when (language) {
        Language.EN -> "APP PREFERENCES"
        Language.PT -> "PREFERÊNCIAS DA APP"
        Language.ES -> "PREFERENCIAS DE LA APP"
        Language.FR -> "PRÉFÉRENCES DE L'APPLICATION"
    }

    fun theme(language: Language): String = when (language) {
        Language.EN -> "Theme"
        Language.PT -> "Tema"
        Language.ES -> "Tema"
        Language.FR -> "Thème"
    }

    fun themeOption(theme: AppTheme, language: Language): String = when (theme) {
        AppTheme.LIGHT -> when (language) {
            Language.EN -> "Light"
            Language.PT -> "Claro"
            Language.ES -> "Claro"
            Language.FR -> "Clair"
        }
        AppTheme.DARK -> when (language) {
            Language.EN -> "Dark"
            Language.PT -> "Escuro"
            Language.ES -> "Oscuro"
            Language.FR -> "Sombre"
        }
        AppTheme.SYSTEM -> when (language) {
            Language.EN -> "System"
            Language.PT -> "Sistema"
            Language.ES -> "Sistema"
            Language.FR -> "Système"
        }
    }

    fun language(language: Language): String = when (language) {
        Language.EN -> "Language"
        Language.PT -> "Idioma"
        Language.ES -> "Idioma"
        Language.FR -> "Langue"
    }

    fun chooseLanguage(language: Language): String = when (language) {
        Language.EN -> "Choose language"
        Language.PT -> "Escolher idioma"
        Language.ES -> "Elegir idioma"
        Language.FR -> "Choisir la langue"
    }

    fun cardManagement(language: Language): String = when (language) {
        Language.EN -> "CARD MANAGEMENT"
        Language.PT -> "GESTÃO DE CARTAS"
        Language.ES -> "GESTIÓN DE CARTAS"
        Language.FR -> "GESTION DES CARTES"
    }

    fun cardManagementDescription(language: Language): String = when (language) {
        Language.EN -> "Cards drawn in the last twelve sessions are held back automatically so nothing repeats too soon."
        Language.PT -> "Cartas usadas nas últimas doze sessões ficam reservadas automaticamente, para que nada se repita cedo demais."
        Language.ES -> "Las cartas sacadas en las últimas doce sesiones se reservan automáticamente para que nada se repita demasiado pronto."
        Language.FR -> "Les cartes tirées lors des douze dernières sessions sont automatiquement mises de côté pour que rien ne se répète trop tôt."
    }

    fun heldBackStatus(count: Int, language: Language): String = when (language) {
        Language.EN -> if (count == 0) {
            "No cards are currently held back."
        } else {
            "$count " + if (count == 1) "card held back." else "cards held back."
        }
        Language.PT -> if (count == 0) {
            "Nenhuma carta está atualmente reservada."
        } else {
            "$count " + if (count == 1) "carta reservada." else "cartas reservadas."
        }
        Language.ES -> if (count == 0) {
            "Actualmente no hay ninguna carta reservada."
        } else {
            "$count " + if (count == 1) "carta reservada." else "cartas reservadas."
        }
        Language.FR -> if (count == 0) {
            "Aucune carte n'est actuellement mise de côté."
        } else {
            "$count " + if (count == 1) "carte mise de côté." else "cartes mises de côté."
        }
    }

    fun resetHeldBackCards(language: Language): String = when (language) {
        Language.EN -> "RESET HELD-BACK CARDS"
        Language.PT -> "REPOR CARTAS RESERVADAS"
        Language.ES -> "RESTABLECER CARTAS RESERVADAS"
        Language.FR -> "RÉINITIALISER LES CARTES MISES DE CÔTÉ"
    }

    fun resetConfirmTitle(language: Language): String = when (language) {
        Language.EN -> "Reset held-back cards?"
        Language.PT -> "Repor cartas reservadas?"
        Language.ES -> "¿Restablecer cartas reservadas?"
        Language.FR -> "Réinitialiser les cartes mises de côté ?"
    }

    fun resetConfirmBody(language: Language): String = when (language) {
        Language.EN -> "This forgets which cards were used recently, so every card becomes available again. It also clears your session log."
        Language.PT -> "Isto esquece que cartas foram usadas recentemente, para que todas fiquem disponíveis outra vez. Também apaga o teu registo de sessões."
        Language.ES -> "Esto olvida qué cartas se usaron recientemente, para que todas vuelvan a estar disponibles. También borra tu registro de sesiones."
        Language.FR -> "Ceci efface la mémoire des cartes utilisées récemment, afin que toutes redeviennent disponibles. Cela efface aussi ton journal des sessions."
    }

    fun reset(language: Language): String = when (language) {
        Language.EN -> "RESET"
        Language.PT -> "REPOR"
        Language.ES -> "RESTABLECER"
        Language.FR -> "RÉINITIALISER"
    }

    fun cancel(language: Language): String = when (language) {
        Language.EN -> "CANCEL"
        Language.PT -> "CANCELAR"
        Language.ES -> "CANCELAR"
        Language.FR -> "ANNULER"
    }

    fun about(language: Language): String = when (language) {
        Language.EN -> "ABOUT"
        Language.PT -> "SOBRE"
        Language.ES -> "ACERCA DE"
        Language.FR -> "À PROPOS"
    }

    fun version(versionName: String, language: Language): String = when (language) {
        Language.EN -> "Version $versionName"
        Language.PT -> "Versão $versionName"
        Language.ES -> "Versión $versionName"
        Language.FR -> "Version $versionName"
    }

    fun tagline(language: Language): String = when (language) {
        Language.EN -> "Taking your team from the comfort zone to the trust zone!"
        Language.PT -> "Levando a tua equipa da zona de conforto para a zona de confiança!"
        Language.ES -> "¡Llevando a tu equipo de la zona de confort a la zona de confianza!"
        Language.FR -> "Faire passer ton équipe de la zone de confort à la zone de confiance !"
    }

    fun footer(language: Language): String = when (language) {
        Language.EN -> "LoopGain Teams is part of the toolbox LoopGain, by Pedro Vicente"
        Language.PT -> "O LoopGain Teams faz parte da caixa de ferramentas LoopGain, por Pedro Vicente"
        Language.ES -> "LoopGain Teams forma parte de la caja de herramientas LoopGain, de Pedro Vicente"
        Language.FR -> "LoopGain Teams fait partie de la boîte à outils LoopGain, par Pedro Vicente"
    }
}
