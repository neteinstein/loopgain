package org.neteinstein.loopgain

import kotlin.test.Test
import kotlin.test.assertEquals
import org.neteinstein.loopgain.data.local.KeyValueStore
import org.neteinstein.loopgain.data.repository.DefaultSettingsRepository
import org.neteinstein.loopgain.domain.model.AppTheme
import org.neteinstein.loopgain.domain.model.Language

private class FakeKeyValueStore : KeyValueStore {
    private val values = mutableMapOf<String, String>()
    override fun getString(key: String): String? = values[key]
    override fun putString(key: String, value: String) {
        values[key] = value
    }
}

class SettingsRepositoryTest {

    @Test
    fun defaultsToSystemThemeAndEnglish() {
        val repo = DefaultSettingsRepository(FakeKeyValueStore())
        assertEquals(AppTheme.SYSTEM, repo.theme.value)
        assertEquals(Language.EN, repo.language.value)
    }

    @Test
    fun setThemeUpdatesStateImmediately() {
        val repo = DefaultSettingsRepository(FakeKeyValueStore())
        repo.setTheme(AppTheme.DARK)
        assertEquals(AppTheme.DARK, repo.theme.value)
    }

    @Test
    fun setLanguageUpdatesStateImmediately() {
        val repo = DefaultSettingsRepository(FakeKeyValueStore())
        repo.setLanguage(Language.FR)
        assertEquals(Language.FR, repo.language.value)
    }

    @Test
    fun preferencesSurviveARepositoryRecreatedOnTheSameStore() {
        val store = FakeKeyValueStore()
        val first = DefaultSettingsRepository(store)
        first.setTheme(AppTheme.LIGHT)
        first.setLanguage(Language.PT)

        val second = DefaultSettingsRepository(store)
        assertEquals(AppTheme.LIGHT, second.theme.value)
        assertEquals(Language.PT, second.language.value)
    }
}
