package org.neteinstein.loopgain.ui.screens.session.tablet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.neteinstein.loopgain.ui.theme.SessionPalette
import org.neteinstein.loopgain.ui.viewmodel.SessionUiState

/** Step 5: the closing reflection, centred, matching the design's single-column layout. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TabletReflectScreen(
    state: SessionUiState,
    onReflectionChange: (String) -> Unit,
    onQuickPick: (String) -> Unit,
    onLog: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.weight(1f).fillMaxWidth().padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = "CLOSING", color = SessionPalette.MutedLabel, fontSize = 10.sp, letterSpacing = 2.sp)
            Text(
                text = "How did that feel?",
                color = SessionPalette.Ink,
                fontSize = 46.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            )
            Text(
                text = "Say it out loud first. Then one line for the record — about the session, not about anyone in it.",
                color = SessionPalette.Accent,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = 520.dp).padding(bottom = 20.dp),
            )
            BasicTextField(
                value = state.reflection,
                onValueChange = onReflectionChange,
                textStyle = TextStyle(color = SessionPalette.Ink, fontSize = 17.sp, textAlign = TextAlign.Center),
                modifier = Modifier
                    .widthIn(max = 620.dp)
                    .fillMaxWidth()
                    .border(1.dp, SessionPalette.Accent)
                    .background(androidx.compose.ui.graphics.Color.White)
                    .padding(18.dp),
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 12.dp),
            ) {
                state.quickReflections.forEach { quick ->
                    Text(
                        text = quick,
                        color = SessionPalette.Accent,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .border(1.dp, SessionPalette.Border)
                            .clickable { onQuickPick(quick) }
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                    )
                }
            }
            Text(
                text = "Saved with the session: four card codes, the date, the length, this line.",
                color = SessionPalette.MutedLabel,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = 480.dp).padding(top = 14.dp),
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SessionPalette.PanelBackground)
                .padding(horizontal = 30.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = state.orderFootLabel, color = SessionPalette.MutedLabel, fontSize = 13.sp)
            Button(
                onClick = onLog,
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SessionPalette.Accent, contentColor = SessionPalette.OnAccent),
            ) {
                Text(text = "LOG SESSION", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.6.sp)
            }
        }
    }
}
