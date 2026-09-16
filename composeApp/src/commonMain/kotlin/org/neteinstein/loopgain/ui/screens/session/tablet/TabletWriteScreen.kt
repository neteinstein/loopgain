package org.neteinstein.loopgain.ui.screens.session.tablet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import org.neteinstein.loopgain.ui.components.QuestionCardFace
import org.neteinstein.loopgain.ui.theme.SessionPalette
import org.neteinstein.loopgain.ui.viewmodel.SessionUiState

/** Step 3: the four drawn cards laid out as a 2x2 grid on the left, the silent countdown on the right. */
@Composable
fun TabletWriteScreen(state: SessionUiState, onDone: () -> Unit, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f).fillMaxHeight().padding(26.dp)) {
            Text(
                text = "STEP 3 — EVERYONE WRITES",
                color = SessionPalette.MutedLabel,
                fontSize = 10.sp,
                letterSpacing = 2.sp,
                modifier = Modifier.padding(bottom = 14.dp),
            )
            state.drawnCards.chunked(2).forEach { pair ->
                Row(
                    modifier = Modifier.weight(1f).fillMaxWidth().padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    pair.forEach { card ->
                        QuestionCardFace(card = card, modifier = Modifier.weight(1f).fillMaxHeight())
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .width(380.dp)
                .fillMaxHeight()
                .background(SessionPalette.PanelBackground)
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = "SILENT WRITING", color = SessionPalette.MutedLabel, fontSize = 10.sp, letterSpacing = 2.sp)
            Text(
                text = state.writeClock,
                color = if (state.writeUrgent) SessionPalette.AccentBright else SessionPalette.Ink,
                fontSize = 96.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 12.dp),
            )
            Text(
                text = "Paper only. Nothing is typed into this device, now or later.",
                color = SessionPalette.Accent,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 18.dp),
            )
            Button(
                onClick = onDone,
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SessionPalette.Accent, contentColor = SessionPalette.OnAccent),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "EVERYONE IS DONE →", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.6.sp)
            }
        }
    }
}
