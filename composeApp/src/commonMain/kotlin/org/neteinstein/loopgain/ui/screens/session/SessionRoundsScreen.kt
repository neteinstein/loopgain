package org.neteinstein.loopgain.ui.screens.session

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import org.neteinstein.loopgain.ui.theme.LocalSessionColors
import org.neteinstein.loopgain.ui.viewmodel.SessionCopy
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
            Text(text = SessionCopy.receivingFeedback(state.language), color = LocalSessionColors.current.MutedLabel, fontSize = 10.sp, letterSpacing = 1.8.sp)
            Text(
                text = state.currentPersonName.uppercase(),
                color = LocalSessionColors.current.Ink,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp, bottom = 10.dp),
            )
            Text(
                text = state.turnPhaseLabel,
                color = LocalSessionColors.current.OnAccent,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.2.sp,
                modifier = Modifier
                    .background(LocalSessionColors.current.Accent)
                    .padding(horizontal = 10.dp, vertical = 7.dp),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LocalSessionColors.current.Accent)
                    .padding(12.dp)
                    .padding(top = 14.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = state.turnClock,
                    color = if (state.turnOver) LocalSessionColors.current.AccentBright else LocalSessionColors.current.Ink,
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(text = state.turnNote, color = LocalSessionColors.current.MutedLabel, fontSize = 11.sp, modifier = Modifier.weight(1f))
            }

            Column(
                modifier = Modifier.padding(top = 14.dp),
                verticalArrangement = Arrangement.spacedBy(7.dp),
            ) {
                state.activeCards.forEach { card ->
                    val swatch = CardStyles.forCategory(card.category).containerColor
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, swatch.copy(alpha = 0.4f))
                            .background(swatch.copy(alpha = 0.14f))
                            .padding(12.dp),
                    ) {
                        Text(
                            text = card.label.uppercase(),
                            color = swatch,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.2.sp,
                        )
                        Text(
                            text = card.text,
                            color = LocalSessionColors.current.Ink,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 7.dp),
                        )
                    }
                }
            }

            Text(
                text = SessionCopy.orderLabel(state.language),
                color = LocalSessionColors.current.MutedLabel,
                fontSize = 10.sp,
                letterSpacing = 1.6.sp,
                modifier = Modifier.padding(top = 14.dp, bottom = 8.dp),
            )
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                state.order.forEach { row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (row.isCurrent) LocalSessionColors.current.PanelBackground else androidx.compose.ui.graphics.Color.Transparent)
                            .padding(horizontal = 11.dp, vertical = 9.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        if (row.isCurrent) {
                            androidx.compose.foundation.layout.Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(androidx.compose.foundation.shape.CircleShape)
                                    .background(LocalSessionColors.current.Accent),
                            )
                        }
                        Text(text = "${row.index}", color = LocalSessionColors.current.MutedSecondary, fontSize = 10.sp)
                        Text(
                            text = row.name,
                            color = if (row.isDone) LocalSessionColors.current.MutedSecondary else LocalSessionColors.current.Ink,
                            fontSize = 14.sp,
                            modifier = Modifier.weight(1f),
                        )
                        Text(text = row.tag, color = LocalSessionColors.current.Accent, fontSize = 9.sp, letterSpacing = 1.sp)
                    }
                }
            }
        }

        if (state.isWarning) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(org.neteinstein.loopgain.ui.theme.TitleRed)
                    .padding(horizontal = 16.dp, vertical = 11.dp),
            ) {
                Text(
                    text = SessionCopy.fiveMinutesLeft(state.language),
                    color = androidx.compose.ui.graphics.Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.6.sp,
                )
                Text(
                    text = SessionCopy.finishTurnNote(state.language),
                    color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.88f),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 5.dp),
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(LocalSessionColors.current.PanelBackground)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(9.dp),
        ) {
            OutlinedButton(
                onClick = onSkip,
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = LocalSessionColors.current.Accent),
            ) {
                Text(text = SessionCopy.closeLabel(state.language), fontSize = 11.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp)
            }
            Button(
                onClick = onAdvance,
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LocalSessionColors.current.Accent, contentColor = LocalSessionColors.current.OnAccent),
                modifier = Modifier.weight(1f),
            ) {
                Text(text = state.advanceLabel, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp)
            }
        }
    }
}
