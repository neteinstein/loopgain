package org.neteinstein.loopgain.ui.screens.session

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
fun SessionDoneScreen(
    state: SessionUiState,
    onNewSession: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(18.dp),
        ) {
            Text(text = "Session logged", color = SessionPalette.Ink, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text(
                text = state.doneSummary,
                color = SessionPalette.MutedLabel,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 15.dp),
            )
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                state.drawnCards.forEach { card ->
                    val swatch = CardStyles.forCategory(card.category).containerColor
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SessionPalette.Border)
                            .padding(horizontal = 11.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Box(modifier = Modifier.width(3.dp).height(24.dp).background(swatch))
                        Text(text = card.code, color = SessionPalette.MutedLabel, fontSize = 10.sp, modifier = Modifier.width(38.dp))
                        Text(text = card.text, color = SessionPalette.Ink, fontSize = 12.sp, modifier = Modifier.weight(1f))
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .border(1.dp, SessionPalette.Border)
                    .padding(13.dp),
            ) {
                Text(text = "HELD BACK NEXT TIME", color = SessionPalette.MutedLabel, fontSize = 10.sp, letterSpacing = 1.6.sp)
                Text(
                    text = state.heldBackNote,
                    color = SessionPalette.Accent,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }
        Button(
            onClick = onNewSession,
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SessionPalette.Accent, contentColor = SessionPalette.OnAccent),
            modifier = Modifier
                .fillMaxWidth()
                .background(SessionPalette.PanelBackground)
                .padding(16.dp),
        ) {
            Text(text = "NEW SESSION", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.4.sp)
        }
    }
}
