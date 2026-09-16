package org.neteinstein.loopgain.ui.screens.session.tablet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.neteinstein.loopgain.domain.model.CardCategory
import org.neteinstein.loopgain.ui.components.LevelDots
import org.neteinstein.loopgain.ui.theme.CardStyles
import org.neteinstein.loopgain.ui.theme.SessionPalette
import org.neteinstein.loopgain.ui.viewmodel.CardFaceUi
import org.neteinstein.loopgain.ui.viewmodel.OrderRowUi
import org.neteinstein.loopgain.ui.viewmodel.SessionUiState

/** Step 4: clockwise order on the left, the current volunteer's cards in the middle, turn clock on the right. */
@Composable
fun TabletRoundsScreen(
    state: SessionUiState,
    onSkip: () -> Unit,
    onAdvance: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isFeedbackPhase = state.activeCards.any {
        it.category == CardCategory.POSITIVE_REINFORCEMENT || it.category == CardCategory.IMPROVEMENTS
    }

    Column(modifier = modifier.fillMaxSize()) {
        Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .width(280.dp)
                    .fillMaxHeight()
                    .border(1.dp, SessionPalette.Border)
                    .padding(22.dp),
            ) {
                Text(text = "CLOCKWISE ORDER", color = SessionPalette.MutedLabel, fontSize = 10.sp, letterSpacing = 2.sp)
                Column(
                    modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(top = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    state.order.forEach { row -> TabletOrderRow(row) }
                }
                Text(
                    text = "Everyone shares about the volunteer, one at a time — not a conversation. The volunteer answers last.",
                    color = SessionPalette.MutedLabel,
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    modifier = Modifier.padding(top = 14.dp),
                )
            }

            Column(modifier = Modifier.weight(1f).fillMaxHeight().padding(26.dp)) {
                Text(text = "RECEIVING FEEDBACK", color = SessionPalette.MutedLabel, fontSize = 10.sp, letterSpacing = 2.sp)
                Text(
                    text = state.currentPersonName.uppercase(),
                    color = SessionPalette.Ink,
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp, bottom = 10.dp),
                )
                Text(
                    text = state.turnPhaseLabel,
                    color = if (isFeedbackPhase) SessionPalette.Accent else SessionPalette.Background,
                    fontSize = 10.sp,
                    letterSpacing = 1.4.sp,
                    modifier = Modifier
                        .background(if (isFeedbackPhase) SessionPalette.PanelBackground else SessionPalette.Ink)
                        .padding(horizontal = 13.dp, vertical = 9.dp),
                )
                Column(
                    modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(top = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(11.dp),
                ) {
                    state.activeCards.forEach { card -> TabletActiveCardRow(card) }
                }
            }

            Column(
                modifier = Modifier
                    .width(300.dp)
                    .fillMaxHeight()
                    .background(SessionPalette.PanelBackground)
                    .padding(22.dp),
            ) {
                Text(text = "THIS TURN", color = SessionPalette.MutedLabel, fontSize = 10.sp, letterSpacing = 2.sp)
                Text(
                    text = state.turnClock,
                    color = if (state.turnOver) SessionPalette.AccentBright else SessionPalette.Ink,
                    fontSize = 52.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 10.dp),
                )
                Text(
                    text = state.turnNote,
                    color = SessionPalette.MutedLabel,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 6.dp),
                )
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 18.dp).height(1.dp).background(SessionPalette.Border),
                )
                Text(text = "NEXT UP", color = SessionPalette.MutedLabel, fontSize = 10.sp, letterSpacing = 2.sp)
                Text(
                    text = state.nextPersonName.uppercase(),
                    color = SessionPalette.Ink,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp),
                )
                if (state.isWarning) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .background(SessionPalette.Ink)
                            .padding(16.dp),
                    ) {
                        Text(text = "FIVE MINUTES LEFT", color = SessionPalette.Background, fontSize = 10.sp, letterSpacing = 1.6.sp)
                        Text(
                            text = "Finish this turn, then decide together: close, or carry the rest to next session.",
                            color = SessionPalette.PanelBackground,
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            modifier = Modifier.padding(top = 7.dp),
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SessionPalette.PanelBackground)
                .padding(horizontal = 30.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            OutlinedButton(
                onClick = onSkip,
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = SessionPalette.Accent),
            ) {
                Text(text = "SKIP TO CLOSING", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.2.sp)
            }
            Button(
                onClick = onAdvance,
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SessionPalette.Accent, contentColor = SessionPalette.OnAccent),
            ) {
                Text(text = state.advanceLabel, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.2.sp)
            }
        }
    }
}

@Composable
private fun TabletOrderRow(row: OrderRowUi, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(if (row.isCurrent) SessionPalette.PanelBackground else Color.Transparent)
            .border(1.dp, if (row.isCurrent) SessionPalette.Accent else Color.Transparent)
            .padding(horizontal = 13.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(text = "${row.index}", color = SessionPalette.MutedSecondary, fontSize = 11.sp, modifier = Modifier.width(16.dp))
        Text(
            text = row.name,
            color = if (row.isDone) SessionPalette.MutedSecondary else SessionPalette.Ink,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = row.tag,
            color = if (row.isCurrent) SessionPalette.Accent else SessionPalette.MutedSecondary,
            fontSize = 9.sp,
            letterSpacing = 1.4.sp,
        )
    }
}

@Composable
private fun TabletActiveCardRow(card: CardFaceUi, modifier: Modifier = Modifier) {
    val style = CardStyles.forCategory(card.category)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, SessionPalette.Border)
            .background(Color.White)
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Column(modifier = Modifier.width(120.dp)) {
            Text(
                text = card.label.uppercase(),
                color = style.labelColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.4.sp,
            )
            LevelDots(
                level = card.level,
                activeColor = style.labelColor,
                inactiveColor = SessionPalette.Disabled,
                modifier = Modifier.padding(top = 9.dp),
            )
        }
        Text(
            text = card.text,
            color = SessionPalette.Ink,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 28.sp,
            modifier = Modifier.weight(1f),
        )
    }
}
