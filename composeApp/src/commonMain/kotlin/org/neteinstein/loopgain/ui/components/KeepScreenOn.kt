package org.neteinstein.loopgain.ui.components

import androidx.compose.runtime.Composable

/**
 * Disables the device's screen-timeout while [enabled] is true. Driven by
 * [org.neteinstein.loopgain.ui.viewmodel.SessionUiState.sessionRunning], which is true from the
 * moment the session counter starts ([org.neteinstein.loopgain.domain.session.SessionEngine.startSession])
 * until the session finishes (rounds complete, are skipped, or the session is logged/reset).
 */
@Composable
expect fun KeepScreenOn(enabled: Boolean)
