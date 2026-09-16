package org.neteinstein.loopgain.ui.screens.session.tablet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.neteinstein.loopgain.domain.model.CardCategory
import org.neteinstein.loopgain.ui.components.LevelDots
import org.neteinstein.loopgain.ui.theme.CardStyles
import org.neteinstein.loopgain.ui.theme.LocalSessionColors
import org.neteinstein.loopgain.ui.viewmodel.PileUi
import org.neteinstein.loopgain.ui.viewmodel.SessionCopy
import org.neteinstein.loopgain.ui.viewmodel.SessionUiState

/**
 * Step 2: a four-column grid of piles. Tapping a face-down pile draws it and opens the reveal
 * overlay ([org.neteinstein.loopgain.ui.screens.session.tablet.TabletSessionFlowScreen]); tapping
 * an already-drawn pile reopens that overlay without redrawing.
 */
@Composable
fun TabletDrawScreen(
    state: SessionUiState,
    onTapPile: (CardCategory) -> Unit,
    onRedraw: () -> Unit,
    onStartWrite: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(30.dp, 22.dp, 30.dp, 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            Column {
                Text(text = SessionCopy.stepTwoDraw(state.language), color = LocalSessionColors.current.MutedLabel, fontSize = 10.sp, letterSpacing = 2.sp)
                Text(
                    text = SessionCopy.tapEachPileReadToRoom(state.language),
                    color = LocalSessionColors.current.Ink,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 9.dp),
                )
            }
            Text(text = SessionCopy.drawnOfFourLabel(state.drawnCount, state.language), color = LocalSessionColors.current.MutedLabel, fontSize = 13.sp)
        }

        Row(
            modifier = Modifier.weight(1f).fillMaxWidth().padding(12.dp, 12.dp, 12.dp, 18.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            state.piles.forEach { pile ->
                TabletPileTile(
                    pile = pile,
                    language = state.language,
                    onTap = { onTapPile(pile.category) },
                    modifier = Modifier.weight(1f).fillMaxSize(),
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(LocalSessionColors.current.PanelBackground)
                .padding(horizontal = 30.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            OutlinedButton(
                onClick = onRedraw,
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = LocalSessionColors.current.Accent),
            ) {
                Text(text = SessionCopy.redrawAll(state.language), fontSize = 12.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.2.sp)
            }
            Button(
                onClick = onStartWrite,
                enabled = state.canProceedFromDraw,
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LocalSessionColors.current.Accent,
                    contentColor = LocalSessionColors.current.OnAccent,
                    disabledContainerColor = LocalSessionColors.current.Disabled,
                    disabledContentColor = LocalSessionColors.current.DisabledText,
                ),
            ) {
                Text(
                    text = SessionCopy.readAloudThenWriteLabel(state.canProceedFromDraw, state.language),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.4.sp,
                )
            }
        }
    }
}

@Composable
private fun TabletPileTile(pile: PileUi, language: org.neteinstein.loopgain.domain.model.Language, onTap: () -> Unit, modifier: Modifier = Modifier) {
    val swatch = CardStyles.forCategory(pile.category).containerColor
    Column(
        modifier = modifier
            .aspectRatio(0.78f)
            .border(1.dp, if (pile.isDrawn) LocalSessionColors.current.Accent else LocalSessionColors.current.Border)
            .background(if (pile.isDrawn) LocalSessionColors.current.Background else LocalSessionColors.current.PanelBackground)
            .clickable(onClick = onTap)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        if (pile.isDrawn) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = pile.label.uppercase(),
                    color = swatch,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.2.sp,
                )
                Text(text = pile.code.orEmpty(), color = LocalSessionColors.current.MutedLabel, fontSize = 10.sp)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = pile.code ?: "",
                    color = LocalSessionColors.current.Ink,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = SessionCopy.drawnTapToReadAgain(language),
                    color = LocalSessionColors.current.MutedSecondary,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(top = 6.dp),
                )
            }
            LevelDots(level = pile.level, activeColor = swatch, inactiveColor = LocalSessionColors.current.Disabled)
        } else {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier.size(58.dp).border(1.dp, LocalSessionColors.current.Accent, CircleShape).clip(CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "∞", color = LocalSessionColors.current.Accent, fontSize = 26.sp)
                }
                Text(
                    text = pile.label.uppercase(),
                    color = swatch,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.2.sp,
                    modifier = Modifier.padding(top = 14.dp),
                )
                Text(
                    text = SessionCopy.pileAvailabilityLabel(false, pile.availableCount, pile.heldBackCount, language),
                    color = LocalSessionColors.current.MutedLabel,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(top = 4.dp),
                )
                Text(
                    text = SessionCopy.tapToDraw(language),
                    color = LocalSessionColors.current.Accent,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.6.sp,
                    modifier = Modifier.padding(top = 10.dp),
                )
            }
        }
    }
}
