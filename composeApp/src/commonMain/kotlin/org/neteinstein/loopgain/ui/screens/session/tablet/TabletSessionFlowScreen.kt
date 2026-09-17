package org.neteinstein.loopgain.ui.screens.session.tablet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.neteinstein.loopgain.domain.model.SessionStage
import org.neteinstein.loopgain.ui.components.HintBanner
import org.neteinstein.loopgain.ui.theme.LocalIsDarkTheme
import org.neteinstein.loopgain.ui.theme.LocalSessionColors
import org.neteinstein.loopgain.ui.theme.SessionPalette
import org.neteinstein.loopgain.ui.theme.SessionPaletteDark
import org.neteinstein.loopgain.ui.theme.TitleRed
import org.neteinstein.loopgain.ui.viewmodel.SessionCopy
import org.neteinstein.loopgain.ui.viewmodel.SessionUiState
import org.neteinstein.loopgain.ui.viewmodel.SessionViewModel
import org.neteinstein.loopgain.ui.viewmodel.toHistoryUiState

/**
 * The tablet's "facilitator board" flow (design: `LoopGain Sessions.dc.html`) — one wide screen
 * per [SessionStage] instead of the phone's screen-per-stage stack, sharing the same
 * [SessionViewModel]. Registered for wide windows by
 * [org.neteinstein.loopgain.ui.navigation.AppNavigation]; the phone keeps using
 * [org.neteinstein.loopgain.ui.screens.session.SessionFlowScreen].
 *
 * Two things this deliberately does *not* copy from the design mockup:
 * - The step tab bar is a read-only progress indicator, not a jump-to-any-stage control. The
 *   mock's JS synthesizes missing state (blank names, undrawn cards, a fabricated elapsed time)
 *   when a tab is tapped out of order — a design-preview convenience, not something a real
 *   facilitator should be able to do mid-session. [SessionEngine][org.neteinstein.loopgain.domain.session.SessionEngine]
 *   has no non-destructive way to jump backward either (re-running `startSession` would redraw
 *   cards that were already read aloud), so rather than build that, every step is shown but only
 *   the current one is interactive.
 * - The History screen's "what keeps coming back" panel does not invent theme labels like
 *   "Ownership & handover" — see [org.neteinstein.loopgain.ui.viewmodel.toHistoryUiState].
 */
@Composable
fun TabletSessionFlowScreen(
    viewModel: SessionViewModel = koinViewModel(),
    onSettingsClick: () -> Unit = {},
    onFinish: () -> Unit = viewModel::resetSession,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showHistory by remember { mutableStateOf(false) }

    val namedCount = state.people.count { it.name.isNotBlank() }
    val colors = if (LocalIsDarkTheme.current) SessionPaletteDark else SessionPalette

    CompositionLocalProvider(LocalSessionColors provides colors) {
        Column(modifier = modifier.fillMaxSize().background(LocalSessionColors.current.Background)) {
            TabletHeader(
                headerMeta = when {
                    showHistory -> SessionCopy.sessionLogHeaderMeta(state.language)
                    state.stage == SessionStage.SETUP -> SessionCopy.stageLabel(SessionStage.SETUP, state.language)
                    else -> SessionCopy.sessionNumberHeaderMeta(viewModel.nextSessionNumber(), namedCount, state.language)
                },
                clock = state.sessionClock,
                clockSuffix = SessionCopy.leftLabel(state.language),
                showClock = state.sessionRunning && !showHistory,
                historyLabel = SessionCopy.historyToggleLabel(showHistory, state.language),
                onToggleHistory = { showHistory = !showHistory },
                onSettingsClick = onSettingsClick,
            )

            if (!showHistory) {
                TabletStepTabs(currentStage = state.stage, language = state.language)
                state.hint?.let { hint ->
                    HintBanner(text = hint, onDismiss = viewModel::dismissHint)
                }
            }

            Box(modifier = Modifier.weight(1f).fillMaxWidth().windowInsetsPadding(WindowInsets.navigationBars)) {
                if (showHistory) {
                    TabletHistoryScreen(
                        historyState = viewModel.historyEntries().toHistoryUiState(state.language),
                        language = state.language,
                    )
                } else {
                    TabletStageContent(
                        state = state,
                        viewModel = viewModel,
                        onFinish = onFinish,
                    )
                }
            }
        }
    }
}

@Composable
private fun TabletStageContent(
    state: SessionUiState,
    viewModel: SessionViewModel,
    onFinish: () -> Unit,
) {
    when (state.stage) {
        SessionStage.SETUP -> TabletSetupScreen(
            state = state,
            onAddPerson = viewModel::addPerson,
            onRemovePerson = viewModel::removePerson,
            onRenamePerson = viewModel::renamePerson,
            onPickLevel = viewModel::setLevel,
            onStart = viewModel::startSession,
        )

        SessionStage.DRAW -> TabletDrawScreen(
            state = state,
            onTapPile = viewModel::drawOne,
            onRedraw = viewModel::redrawAll,
            onStartWrite = viewModel::startWriteDirectly,
        )

        SessionStage.WRITE -> TabletWriteScreen(state = state, onDone = viewModel::startRounds)

        SessionStage.ROUNDS -> TabletRoundsScreen(
            state = state,
            onSkip = viewModel::skipToReflect,
            onAdvance = viewModel::advanceRounds,
        )

        SessionStage.REFLECT -> TabletReflectScreen(
            state = state,
            onReflectionChange = viewModel::setReflection,
            onQuickPick = viewModel::setReflection,
            onLog = viewModel::logSession,
        )

        SessionStage.DONE -> TabletDoneScreen(state = state, onFinish = onFinish)

        // The tablet never enters READ — startWriteDirectly() skips it — but the `when` must stay
        // exhaustive against SessionStage, which is shared with the phone flow.
        SessionStage.READ -> Unit
    }
}

@Composable
private fun TabletHeader(
    headerMeta: String,
    clock: String,
    clockSuffix: String,
    showClock: Boolean,
    historyLabel: String,
    onToggleHistory: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = LocalSessionColors.current.Border)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = 26.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Text(
                text = "LoopGain",
                color = TitleRed,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.5.sp,
            )
            Box(modifier = Modifier.width(1.dp).height(16.dp).background(LocalSessionColors.current.Border))
            Text(text = headerMeta, color = LocalSessionColors.current.MutedLabel, fontSize = 10.sp, letterSpacing = 1.6.sp)
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            if (showClock) {
                Row(
                    modifier = Modifier.background(LocalSessionColors.current.Accent).padding(horizontal = 13.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(9.dp),
                ) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(LocalSessionColors.current.AccentBright))
                    Text(text = clock, color = LocalSessionColors.current.OnAccent, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = clockSuffix, color = LocalSessionColors.current.OnAccent.copy(alpha = 0.75f), fontSize = 9.sp, letterSpacing = 1.6.sp)
                }
            }
            OutlinedButton(
                onClick = onToggleHistory,
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = LocalSessionColors.current.Accent),
            ) {
                Text(text = historyLabel, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.2.sp)
            }
            androidx.compose.material3.IconButton(onClick = onSettingsClick) {
                Text(text = "⚙", color = LocalSessionColors.current.MutedLabel, fontSize = 22.sp)
            }
        }
    }
}

/**
 * A read-only progress indicator across the six tablet steps — see the "deliberately does not
 * copy" note on [TabletSessionFlowScreen] for why this does not jump stages on tap.
 */
@Composable
private fun TabletStepTabs(currentStage: SessionStage, language: org.neteinstein.loopgain.domain.model.Language, modifier: Modifier = Modifier) {
    val currentIndex = tabletStepIndex(currentStage)
    Row(modifier = modifier.fillMaxWidth()) {
        tabletStepLabels(language).forEachIndexed { index, label ->
            val isCurrent = index == currentIndex
            val isDone = index < currentIndex
            Row(
                modifier = Modifier
                    .weight(1f)
                    .border(width = 1.dp, color = LocalSessionColors.current.Border)
                    .background(
                        when {
                            isCurrent -> LocalSessionColors.current.Ink
                            isDone -> LocalSessionColors.current.Accent.copy(alpha = 0.1f)
                            else -> Color.Transparent
                        }
                    )
                    .padding(horizontal = 14.dp, vertical = 13.dp),
                horizontalArrangement = Arrangement.spacedBy(9.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = if (isDone) "✓" else (index + 1).toString().padStart(2, '0'),
                    color = if (isCurrent) LocalSessionColors.current.AccentBright else if (isDone) LocalSessionColors.current.Accent else LocalSessionColors.current.MutedSecondary,
                    fontSize = 10.sp,
                )
                Text(
                    text = label,
                    color = when {
                        isCurrent -> LocalSessionColors.current.Background
                        isDone -> LocalSessionColors.current.Accent
                        else -> LocalSessionColors.current.MutedSecondary
                    },
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.4.sp,
                )
            }
        }
    }
}
