package org.neteinstein.loopgain.ui.screens.session

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.neteinstein.loopgain.domain.model.CardCategory
import org.neteinstein.loopgain.ui.components.PileTile
import org.neteinstein.loopgain.ui.theme.SessionPalette
import org.neteinstein.loopgain.ui.viewmodel.SessionUiState

@Composable
fun SessionDrawScreen(
    state: SessionUiState,
    onTapPile: (CardCategory) -> Unit,
    onRedraw: () -> Unit,
    onRead: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f).padding(16.dp)) {
            Text(text = "Draw the four", color = SessionPalette.Ink, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(
                text = "${state.drawnCount} of 4 drawn · tap a pile to draw or redraw it",
                color = SessionPalette.MutedLabel,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(9.dp),
                verticalArrangement = Arrangement.spacedBy(9.dp),
                modifier = Modifier.weight(1f),
            ) {
                items(state.piles) { pile ->
                    PileTile(
                        pile = pile,
                        onTap = { onTapPile(pile.category) },
                        modifier = Modifier.aspectRatio(1.05f),
                    )
                }
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
                onClick = onRedraw,
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = SessionPalette.Accent),
            ) {
                Text(text = "REDRAW", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp)
            }
            Button(
                onClick = onRead,
                enabled = state.canProceedFromDraw,
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SessionPalette.Accent,
                    contentColor = SessionPalette.OnAccent,
                    disabledContainerColor = SessionPalette.Disabled,
                    disabledContentColor = SessionPalette.DisabledText,
                ),
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = if (state.canProceedFromDraw) "READ THEM OUT →" else "DRAW ALL FOUR",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.2.sp,
                )
            }
        }
    }
}
