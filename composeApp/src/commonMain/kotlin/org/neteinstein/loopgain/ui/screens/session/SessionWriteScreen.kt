package org.neteinstein.loopgain.ui.screens.session

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.neteinstein.loopgain.ui.theme.CardStyles
import org.neteinstein.loopgain.ui.theme.SessionPalette
import org.neteinstein.loopgain.ui.viewmodel.SessionUiState

@Composable
fun SessionWriteScreen(
    state: SessionUiState,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "EVERYONE WRITES — PAPER ONLY",
                color = SessionPalette.MutedLabel,
                fontSize = 10.sp,
                letterSpacing = 1.8.sp,
            )
            Text(
                text = state.writeClock,
                color = if (state.writeUrgent) SessionPalette.AccentBright else SessionPalette.Ink,
                fontSize = 72.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp),
            )
            Text(
                text = "One answer per card about every other person. The personal card is about you.",
                color = SessionPalette.Accent,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp),
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                state.drawnCards.forEach { card ->
                    val swatch = CardStyles.forCategory(card.category).containerColor
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SessionPalette.Border)
                            .padding(horizontal = 11.dp, vertical = 9.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        androidx.compose.foundation.layout.Box(
                            modifier = Modifier
                                .width(3.dp)
                                .height(26.dp)
                                .background(swatch),
                        )
                        Text(text = card.text, color = SessionPalette.Ink, fontSize = 12.sp, modifier = Modifier.weight(1f))
                    }
                }
            }
        }
        Button(
            onClick = onDone,
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SessionPalette.Accent,
                contentColor = SessionPalette.OnAccent,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(SessionPalette.PanelBackground)
                .padding(16.dp),
        ) {
            Text(text = "EVERYONE IS DONE →", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.4.sp)
        }
    }
}
