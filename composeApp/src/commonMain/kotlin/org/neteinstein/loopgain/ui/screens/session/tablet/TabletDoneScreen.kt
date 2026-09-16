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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.neteinstein.loopgain.ui.components.asBlankedQuestion
import org.neteinstein.loopgain.ui.theme.CardStyles
import org.neteinstein.loopgain.ui.theme.SessionPalette
import org.neteinstein.loopgain.ui.viewmodel.SessionUiState

/**
 * Step 6: what was logged. Reuses [SessionUiState.doneSummary] as one summary line rather than
 * the mock's four separate stat rows (people/planned/used/volunteers), since those numbers are
 * not individually exposed on [SessionUiState] and it wasn't worth adding fields nobody else
 * needs just to split a string the phone flow already renders as one line.
 */
@Composable
fun TabletDoneScreen(state: SessionUiState, onNewSession: () -> Unit, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f).fillMaxHeight().padding(28.dp)) {
            Text(text = "LOGGED", color = SessionPalette.MutedLabel, fontSize = 10.sp, letterSpacing = 2.sp)
            Text(
                text = "Session logged",
                color = SessionPalette.Ink,
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp, bottom = 18.dp),
            )
            Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                state.drawnCards.chunked(2).forEach { pair ->
                    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 11.dp), horizontalArrangement = Arrangement.spacedBy(11.dp)) {
                        pair.forEach { card ->
                            val swatch = CardStyles.forCategory(card.category).containerColor
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .border(1.dp, SessionPalette.Border)
                                    .padding(15.dp),
                            ) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(
                                        text = card.label.uppercase(),
                                        color = swatch,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        letterSpacing = 1.2.sp,
                                    )
                                    Text(text = card.code, color = SessionPalette.MutedLabel, fontSize = 10.sp)
                                }
                                Text(
                                    text = card.text.asBlankedQuestion(),
                                    color = SessionPalette.Ink,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp,
                                    modifier = Modifier.padding(top = 9.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .width(360.dp)
                .fillMaxHeight()
                .background(SessionPalette.PanelBackground)
                .padding(28.dp),
        ) {
            Text(text = "SESSION SUMMARY", color = SessionPalette.MutedLabel, fontSize = 10.sp, letterSpacing = 2.sp)
            Text(
                text = state.doneSummary,
                color = SessionPalette.Accent,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                modifier = Modifier.padding(top = 12.dp),
            )
            androidx.compose.foundation.layout.Box(
                modifier = Modifier.fillMaxWidth().padding(vertical = 18.dp).height(1.dp).background(SessionPalette.Border),
            )
            Text(text = "REFLECTION", color = SessionPalette.MutedLabel, fontSize = 10.sp, letterSpacing = 2.sp)
            Text(
                text = state.reflection.ifBlank { "No note added." },
                color = SessionPalette.Ink,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 10.dp),
            )
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Bottom) {
                Text(
                    text = "Next session these four codes join the held-back pile. Nothing anyone said in the room was recorded.",
                    color = SessionPalette.MutedLabel,
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                )
                Button(
                    onClick = onNewSession,
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SessionPalette.Accent, contentColor = SessionPalette.OnAccent),
                    modifier = Modifier.fillMaxWidth().padding(top = 9.dp),
                ) {
                    Text(text = "NEW SESSION", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.6.sp)
                }
            }
        }
    }
}
