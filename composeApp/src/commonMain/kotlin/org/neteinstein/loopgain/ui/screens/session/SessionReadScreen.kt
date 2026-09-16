package org.neteinstein.loopgain.ui.screens.session

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import org.neteinstein.loopgain.ui.components.QuestionCardFace
import org.neteinstein.loopgain.ui.theme.SessionPalette
import org.neteinstein.loopgain.ui.viewmodel.SessionUiState

@Composable
fun SessionReadScreen(
    state: SessionUiState,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize().background(SessionPalette.PanelBackground)) {
        val card = state.readCard
        if (card != null) {
            QuestionCardFace(
                card = card,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp),
            )
        } else {
            androidx.compose.foundation.layout.Spacer(Modifier.weight(1f))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SessionPalette.Background)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(9.dp),
        ) {
            Text(
                text = state.readFootLabel,
                color = SessionPalette.MutedLabel,
                fontSize = 11.sp,
                modifier = Modifier.weight(1f),
            )
            OutlinedButton(
                onClick = onPrevious,
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = SessionPalette.Accent),
            ) {
                Text(text = "←", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
            Button(
                onClick = onNext,
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SessionPalette.Accent,
                    contentColor = SessionPalette.OnAccent,
                ),
            ) {
                Text(text = state.readNextLabel, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp)
            }
        }
    }
}
