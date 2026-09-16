package org.neteinstein.loopgain

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.koinInject
import org.neteinstein.loopgain.data.repository.SettingsRepository
import org.neteinstein.loopgain.ui.navigation.AppNavigation
import org.neteinstein.loopgain.ui.theme.LoopGainTheme

@Composable
fun App(settingsRepository: SettingsRepository = koinInject()) {
    val theme by settingsRepository.theme.collectAsStateWithLifecycle()
    LoopGainTheme(theme = theme) {
        AppNavigation()
    }
}
