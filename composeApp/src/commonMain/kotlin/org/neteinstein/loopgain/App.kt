package org.neteinstein.loopgain

import androidx.compose.runtime.Composable
import org.neteinstein.loopgain.ui.navigation.AppNavigation
import org.neteinstein.loopgain.ui.theme.LoopGainTheme

@Composable
fun App() {
    LoopGainTheme {
        AppNavigation()
    }
}
