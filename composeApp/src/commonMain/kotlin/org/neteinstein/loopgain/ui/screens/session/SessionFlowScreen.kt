package org.neteinstein.loopgain.ui.screens.session

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.neteinstein.loopgain.domain.model.SessionStage
import org.neteinstein.loopgain.ui.components.HintBanner
import org.neteinstein.loopgain.ui.components.SessionHeader
import org.neteinstein.loopgain.ui.theme.LocalIsDarkTheme
import org.neteinstein.loopgain.ui.theme.LocalSessionColors
import org.neteinstein.loopgain.ui.theme.SessionPalette
import org.neteinstein.loopgain.ui.theme.SessionPaletteDark
import org.neteinstein.loopgain.ui.viewmodel.SessionCopy
import org.neteinstein.loopgain.ui.viewmodel.SessionViewModel

/**
 * The phone's "Faithful deck" flow (design 1A): one screen per [SessionStage], sharing a single
 * [SessionViewModel] with header and hint banner. This is the screen registered for the phone
 * layout in [org.neteinstein.loopgain.ui.navigation.AppNavigation] — a tablet layout would drive
 * the same view model with its own composables instead of this dispatcher.
 */
@Composable
fun SessionFlowScreen(
    viewModel: SessionViewModel = koinViewModel(),
    onSettingsClick: () -> Unit = {},
    onFinish: () -> Unit = viewModel::resetSession,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val colors = if (LocalIsDarkTheme.current) SessionPaletteDark else SessionPalette

    CompositionLocalProvider(LocalSessionColors provides colors) {
        Column(modifier = modifier.fillMaxSize().background(LocalSessionColors.current.Background)) {
            SessionHeader(
                stageLabel = SessionCopy.stageLabel(state.stage, state.language),
                clock = state.sessionClock,
                running = state.sessionRunning,
                onSettingsClick = onSettingsClick,
            )
            state.hint?.let { hint ->
                HintBanner(text = hint, onDismiss = viewModel::dismissHint)
            }
            Column(modifier = Modifier.weight(1f).windowInsetsPadding(WindowInsets.navigationBars)) {
                when (state.stage) {
                    SessionStage.SETUP -> SessionSetupScreen(
                        state = state,
                        onAddPerson = viewModel::addPerson,
                        onRemovePerson = viewModel::removePerson,
                        onRenamePerson = viewModel::renamePerson,
                        onPickLevel = viewModel::setLevel,
                        onStart = viewModel::startSession,
                    )

                    SessionStage.DRAW -> SessionDrawScreen(
                        state = state,
                        onTapPile = viewModel::drawOne,
                        onRedraw = viewModel::redrawAll,
                        onRead = viewModel::enterRead,
                    )

                    SessionStage.READ -> SessionReadScreen(
                        state = state,
                        onPrevious = viewModel::previousReadCard,
                        onNext = viewModel::advanceRead,
                    )

                    SessionStage.WRITE -> SessionWriteScreen(
                        state = state,
                        onDone = viewModel::startRounds,
                    )

                    SessionStage.ROUNDS -> SessionRoundsScreen(
                        state = state,
                        onSkip = viewModel::skipToReflect,
                        onAdvance = viewModel::advanceRounds,
                    )

                    SessionStage.REFLECT -> SessionReflectScreen(
                        state = state,
                        onReflectionChange = viewModel::setReflection,
                        onQuickPick = viewModel::setReflection,
                        onLog = viewModel::logSession,
                    )

                    SessionStage.DONE -> SessionDoneScreen(
                        state = state,
                        onFinish = onFinish,
                    )
                }
            }
        }
    }
}
