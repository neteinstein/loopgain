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
import org.neteinstein.loopgain.ui.theme.LocalSessionColors
import org.neteinstein.loopgain.ui.viewmodel.SessionCopy
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
            Text(text = SessionCopy.drawTitle(state.language), color = LocalSessionColors.current.Ink, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(
                text = SessionCopy.drawnCountLabel(state.drawnCount, state.language),
                color = LocalSessionColors.current.MutedLabel,
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
                        language = state.language,
                        onTap = { onTapPile(pile.category) },
                        modifier = Modifier.aspectRatio(0.85f),
                    )
                }
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
                onClick = onRedraw,
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = LocalSessionColors.current.Accent),
            ) {
                Text(text = SessionCopy.redraw(state.language), fontSize = 11.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp)
            }
            Button(
                onClick = onRead,
                enabled = state.canProceedFromDraw,
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LocalSessionColors.current.Accent,
                    contentColor = LocalSessionColors.current.OnAccent,
                    disabledContainerColor = LocalSessionColors.current.Disabled,
                    disabledContentColor = LocalSessionColors.current.DisabledText,
                ),
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = SessionCopy.readOrDrawAllLabel(state.canProceedFromDraw, state.language),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.2.sp,
                )
            }
        }
    }
}
