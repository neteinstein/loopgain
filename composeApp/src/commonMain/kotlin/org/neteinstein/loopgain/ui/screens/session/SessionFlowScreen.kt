package org.neteinstein.loopgain.ui.screens.session

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.neteinstein.loopgain.domain.model.SessionStage
import org.neteinstein.loopgain.ui.components.HintBanner
import org.neteinstein.loopgain.ui.components.SessionHeader
import org.neteinstein.loopgain.ui.theme.SessionPalette
import org.neteinstein.loopgain.ui.viewmodel.SessionViewModel

private fun stageLabel(stage: SessionStage): String = when (stage) {
    SessionStage.SETUP -> "SETUP"
    SessionStage.DRAW -> "DRAW"
    SessionStage.READ -> "READ ALOUD"
    SessionStage.WRITE -> "WRITE"
    SessionStage.ROUNDS -> "ROUNDS"
    SessionStage.REFLECT -> "CLOSING"
    SessionStage.DONE -> "LOGGED"
}

/**
 * The phone's "Faithful deck" flow (design 1A): one screen per [SessionStage], sharing a single
 * [SessionViewModel] with header and hint banner. This is the screen registered for the phone
 * layout in [org.neteinstein.loopgain.ui.navigation.AppNavigation] — a tablet layout would drive
 * the same view model with its own composables instead of this dispatcher.
 */
@Composable
fun SessionFlowScreen(viewModel: SessionViewModel = koinViewModel(), modifier: Modifier = Modifier) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize().background(SessionPalette.Background)) {
        SessionHeader(stageLabel = stageLabel(state.stage), clock = state.sessionClock, running = state.sessionRunning)
        state.hint?.let { hint ->
            HintBanner(text = hint, onDismiss = viewModel::dismissHint)
        }
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
                onNewSession = viewModel::resetSession,
            )
        }
    }
}
