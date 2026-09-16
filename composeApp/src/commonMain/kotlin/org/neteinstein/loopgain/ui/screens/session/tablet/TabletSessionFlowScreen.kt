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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import org.neteinstein.loopgain.domain.model.CardCategory
import org.neteinstein.loopgain.domain.model.SessionStage
import org.neteinstein.loopgain.ui.components.HintBanner
import org.neteinstein.loopgain.ui.components.LevelDots
import org.neteinstein.loopgain.ui.theme.CardStyles
import org.neteinstein.loopgain.ui.theme.SessionPalette
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
fun TabletSessionFlowScreen(viewModel: SessionViewModel = koinViewModel(), modifier: Modifier = Modifier) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showHistory by remember { mutableStateOf(false) }
    var revealCategory by remember { mutableStateOf<CardCategory?>(null) }

    LaunchedEffect(state.stage) {
        if (state.stage != SessionStage.DRAW) revealCategory = null
    }

    val namedCount = state.people.count { it.name.isNotBlank() }

    Box(modifier = modifier.fillMaxSize().background(SessionPalette.Background)) {
        Column(modifier = Modifier.fillMaxSize()) {
            TabletHeader(
                headerMeta = when {
                    showHistory -> "SESSION LOG · TEAM LEVEL ONLY"
                    state.stage == SessionStage.SETUP -> "SETUP"
                    else -> "SESSION ${viewModel.nextSessionNumber()} · $namedCount PEOPLE"
                },
                clock = state.sessionClock,
                showClock = state.sessionRunning && !showHistory,
                historyLabel = if (showHistory) "CLOSE" else "HISTORY",
                onToggleHistory = { showHistory = !showHistory },
            )

            if (!showHistory) {
                TabletStepTabs(currentStage = state.stage)
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
                        revealCategory = revealCategory,
                        onOpenReveal = { category -> revealCategory = category },
                    )
                }
            }
        }

        if (!showHistory) {
            revealCategory?.let { category ->
                TabletRevealOverlay(
                    category = category,
                    state = state,
                    onDrawAnother = { viewModel.drawOne(category) },
                    onNext = { next -> viewModel.drawOne(next); revealCategory = next },
                    onClose = { revealCategory = null },
                )
            }
        }
    }
}

@Composable
private fun TabletStageContent(
    state: SessionUiState,
    viewModel: SessionViewModel,
    revealCategory: CardCategory?,
    onOpenReveal: (CardCategory) -> Unit,
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
            onTapPile = { category ->
                val alreadyDrawn = state.piles.firstOrNull { it.category == category }?.isDrawn == true
                if (!alreadyDrawn) viewModel.drawOne(category)
                onOpenReveal(category)
            },
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

        SessionStage.DONE -> TabletDoneScreen(state = state, onNewSession = viewModel::resetSession)

        // The tablet never enters READ — startWriteDirectly() skips it — but the `when` must stay
        // exhaustive against SessionStage, which is shared with the phone flow.
        SessionStage.READ -> Unit
    }
}

@Composable
private fun TabletHeader(
    headerMeta: String,
    clock: String,
    showClock: Boolean,
    historyLabel: String,
    onToggleHistory: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = SessionPalette.Border)
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
            Box(modifier = Modifier.width(1.dp).height(16.dp).background(SessionPalette.Border))
            Text(text = headerMeta, color = SessionPalette.MutedLabel, fontSize = 10.sp, letterSpacing = 1.6.sp)
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            if (showClock) {
                Row(
                    modifier = Modifier.border(1.dp, SessionPalette.Accent).padding(horizontal = 13.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(9.dp),
                ) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(SessionPalette.AccentBright))
                    Text(text = clock, color = SessionPalette.Ink, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = "LEFT", color = SessionPalette.MutedLabel, fontSize = 9.sp, letterSpacing = 1.6.sp)
                }
            }
            OutlinedButton(
                onClick = onToggleHistory,
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = SessionPalette.Accent),
            ) {
                Text(text = historyLabel, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.2.sp)
            }
        }
    }
}

/**
 * A read-only progress indicator across the six tablet steps — see the "deliberately does not
 * copy" note on [TabletSessionFlowScreen] for why this does not jump stages on tap.
 */
@Composable
private fun TabletStepTabs(currentStage: SessionStage, modifier: Modifier = Modifier) {
    val currentIndex = tabletStepIndex(currentStage)
    Row(modifier = modifier.fillMaxWidth()) {
        TABLET_STEP_LABELS.forEachIndexed { index, label ->
            val isCurrent = index == currentIndex
            val isDone = index < currentIndex
            Row(
                modifier = Modifier
                    .weight(1f)
                    .border(width = 1.dp, color = SessionPalette.Border)
                    .background(if (isCurrent) SessionPalette.Ink else Color.Transparent)
                    .padding(horizontal = 14.dp, vertical = 13.dp),
                horizontalArrangement = Arrangement.spacedBy(9.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = (index + 1).toString().padStart(2, '0'),
                    color = if (isCurrent) SessionPalette.AccentBright else SessionPalette.MutedSecondary,
                    fontSize = 10.sp,
                )
                Text(
                    text = label,
                    color = when {
                        isCurrent -> SessionPalette.Background
                        isDone -> SessionPalette.Accent
                        else -> SessionPalette.MutedSecondary
                    },
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.4.sp,
                )
            }
        }
    }
}

/**
 * The tablet's read-equivalent: a full-screen reveal for one drawn card, opened by tapping a pile
 * on [TabletDrawScreen]. "DRAW ANOTHER" redraws just this category; the right-hand button either
 * reveals the next undrawn category or, once all four are drawn, closes back to the board.
 */
@Composable
private fun TabletRevealOverlay(
    category: CardCategory,
    state: SessionUiState,
    onDrawAnother: () -> Unit,
    onNext: (CardCategory) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val card = state.drawnCards.firstOrNull { it.category == category } ?: return
    val nextUndrawn = state.piles.firstOrNull { !it.isDrawn }?.category
    val swatch = CardStyles.forCategory(category).containerColor

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xD6071B33))
            .padding(46.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 760.dp)
                .fillMaxWidth()
                .border(1.dp, SessionPalette.AccentBright)
                .background(SessionPalette.Background)
                .padding(horizontal = 36.dp, vertical = 30.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(modifier = Modifier.width(24.dp).height(3.dp).background(swatch))
                    Text(
                        text = card.label.uppercase(),
                        color = swatch,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 2.sp,
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    LevelDots(
                        level = card.level,
                        activeColor = swatch,
                        inactiveColor = SessionPalette.Disabled,
                        dotSize = 8.dp,
                    )
                    Text(
                        text = SessionCopy.levelLabel(card.level, state.language),
                        color = SessionPalette.MutedLabel,
                        fontSize = 10.sp,
                        letterSpacing = 1.4.sp,
                    )
                    Box(modifier = Modifier.width(1.dp).height(14.dp).background(SessionPalette.Border))
                    Text(text = card.code, color = SessionPalette.MutedLabel, fontSize = 11.sp, letterSpacing = 1.2.sp)
                }
            }

            Text(
                text = SessionCopy.readAloud(state.language),
                color = SessionPalette.MutedLabel,
                fontSize = 10.sp,
                letterSpacing = 1.6.sp,
            )

            Text(
                text = card.text,
                color = SessionPalette.Ink,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 40.sp,
            )

            Row(
                modifier = Modifier.fillMaxWidth().border(width = 1.dp, color = SessionPalette.Border).padding(top = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = card.about,
                    color = SessionPalette.MutedLabel,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1f).padding(top = 18.dp),
                )
                Row(modifier = Modifier.padding(top = 18.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = onDrawAnother,
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = SessionPalette.Accent),
                    ) {
                        Text(
                            text = SessionCopy.drawAnother(state.language),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.2.sp,
                        )
                    }
                    Button(
                        onClick = { if (nextUndrawn != null) onNext(nextUndrawn) else onClose() },
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SessionPalette.Accent,
                            contentColor = SessionPalette.OnAccent,
                        ),
                    ) {
                        val label = nextUndrawn?.let {
                            SessionCopy.nextCategoryCard(it.displayName(state.language), state.language)
                        } ?: SessionCopy.backToBoard(state.language)
                        Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.2.sp)
                    }
                }
            }
        }
    }
}
