package org.neteinstein.loopgain.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.neteinstein.loopgain.data.local.KeyValueStore
import org.neteinstein.loopgain.data.local.platformSystemLanguage
import org.neteinstein.loopgain.domain.model.AppTheme
import org.neteinstein.loopgain.domain.model.Language

/** The user's app-wide preferences — colour theme and display language — persisted across launches. */
interface SettingsRepository {
    val theme: StateFlow<AppTheme>
    val language: StateFlow<Language>
    fun setTheme(theme: AppTheme)
    fun setLanguage(language: Language)
}

/**
 * [systemLanguage] seeds the very first launch's default when nothing is saved yet — the device's
 * system language, mapped to a supported [Language] by [platformSystemLanguage]. It is a
 * constructor parameter (rather than called inline) so tests can pin it instead of depending on
 * the host's locale.
 */
class DefaultSettingsRepository(
    private val store: KeyValueStore,
    systemLanguage: Language? = platformSystemLanguage(),
) : SettingsRepository {
    private val _theme = MutableStateFlow(
        store.getString(KEY_THEME)?.let { saved -> runCatching { AppTheme.valueOf(saved) }.getOrNull() } ?: AppTheme.SYSTEM,
    )
    override val theme: StateFlow<AppTheme> = _theme.asStateFlow()

    private val _language = MutableStateFlow(
        store.getString(KEY_LANGUAGE)?.let { saved -> runCatching { Language.valueOf(saved) }.getOrNull() }
            ?: systemLanguage
            ?: Language.EN,
    )
    override val language: StateFlow<Language> = _language.asStateFlow()

    override fun setTheme(theme: AppTheme) {
        store.putString(KEY_THEME, theme.name)
        _theme.value = theme
    }

    override fun setLanguage(language: Language) {
        store.putString(KEY_LANGUAGE, language.name)
        _language.value = language
    }

    private companion object {
        const val KEY_THEME = "app_theme"
        const val KEY_LANGUAGE = "app_language"
    }
}
