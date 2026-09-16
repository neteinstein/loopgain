package org.neteinstein.loopgain.data.local

import org.neteinstein.loopgain.domain.model.Language
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

actual fun platformSystemLanguage(): Language? = when (NSLocale.currentLocale.languageCode) {
    "en" -> Language.EN
    "pt" -> Language.PT
    "es" -> Language.ES
    "fr" -> Language.FR
    else -> null
}
