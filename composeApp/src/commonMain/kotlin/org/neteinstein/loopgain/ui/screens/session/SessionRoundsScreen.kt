package org.neteinstein.loopgain.ui.screens.session

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.neteinstein.loopgain.ui.theme.CardStyles
import org.neteinstein.loopgain.ui.theme.SessionPalette
import org.neteinstein.loopgain.ui.viewmodel.SessionUiState

@Composable
fun SessionRoundsScreen(
    state: SessionUiState,
    onSkip: () -> Unit,
    onAdvance: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            Text(text = "RECEIVING FEEDBACK", color = SessionPalette.MutedLabel, fontSize = 10.sp, letterSpacing = 1.8.sp)
            Text(
                text = state.currentPersonName.uppercase(),
                color = SessionPalette.Ink,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp, bottom = 10.dp),
            )
            Text(
                text = state.turnPhaseLabel,
                color = SessionPalette.Accent,
                fontSize = 10.sp,
                letterSpacing = 1.2.sp,
                modifier = Modifier
                    .background(SessionPalette.PanelBackground)
                    .padding(horizontal = 10.dp, vertical = 7.dp),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SessionPalette.Accent)
                    .padding(12.dp)
                    .padding(top = 14.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = state.turnClock,
                    color = if (state.turnOver) SessionPalette.AccentBright else SessionPalette.Ink,
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(text = state.turnNote, color = SessionPalette.MutedLabel, fontSize = 11.sp, modifier = Modifier.weight(1f))
            }

            Column(
                modifier = Modifier.padding(top = 14.dp),
                verticalArrangement = Arrangement.spacedBy(7.dp),
            ) {
                state.activeCards.forEach { card ->
                    val labelColor = CardStyles.forCategory(card.category).containerColor
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SessionPalette.Border)
                            .padding(12.dp),
                    ) {
                        Text(
                            text = card.label.uppercase(),
                            color = labelColor,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.2.sp,
                        )
                        Text(
                            text = card.text,
                            color = SessionPalette.Ink,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 7.dp),
                        )
                    }
                }
            }

            Text(
                text = "ORDER",
                color = SessionPalette.MutedLabel,
                fontSize = 10.sp,
                letterSpacing = 1.6.sp,
                modifier = Modifier.padding(top = 14.dp, bottom = 8.dp),
            )
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                state.order.forEach { row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (row.isCurrent) SessionPalette.PanelBackground else androidx.compose.ui.graphics.Color.Transparent)
                            .border(1.dp, if (row.isCurrent) SessionPalette.Accent else androidx.compose.ui.graphics.Color.Transparent)
                            .padding(horizontal = 11.dp, vertical = 9.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Text(text = "${row.index}", color = SessionPalette.MutedSecondary, fontSize = 10.sp)
                        Text(
                            text = row.name,
                            color = if (row.isDone) SessionPalette.MutedSecondary else SessionPalette.Ink,
                            fontSize = 14.sp,
                            modifier = Modifier.weight(1f),
                        )
                        Text(text = row.tag, color = SessionPalette.Accent, fontSize = 9.sp, letterSpacing = 1.sp)
                    }
                }
            }
        }

        if (state.isWarning) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SessionPalette.Ink)
                    .padding(horizontal = 16.dp, vertical = 11.dp),
            ) {
                Text(text = "FIVE MINUTES LEFT", color = SessionPalette.Background, fontSize = 10.sp, letterSpacing = 1.6.sp)
                Text(
                    text = "Finish this turn, then decide together: close, or carry the rest to next session.",
                    color = SessionPalette.PanelBackground,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 5.dp),
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SessionPalette.PanelBackground)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(9.dp),
        ) {
            OutlinedButton(
                onClick = onSkip,
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = SessionPalette.Accent),
            ) {
                Text(text = "CLOSE", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp)
            }
            Button(
                onClick = onAdvance,
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SessionPalette.Accent, contentColor = SessionPalette.OnAccent),
                modifier = Modifier.weight(1f),
            ) {
                Text(text = state.advanceLabel, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp)
            }
        }
    }
}
