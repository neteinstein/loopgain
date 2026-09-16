package org.neteinstein.loopgain.data.local

import java.util.Locale
import org.neteinstein.loopgain.domain.model.Language

actual fun platformSystemLanguage(): Language? = when (Locale.getDefault().language) {
    "en" -> Language.EN
    "pt" -> Language.PT
    "es" -> Language.ES
    "fr" -> Language.FR
    else -> null
}
