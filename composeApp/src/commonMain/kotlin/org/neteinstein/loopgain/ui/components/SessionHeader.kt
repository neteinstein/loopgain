package org.neteinstein.loopgain.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.neteinstein.loopgain.ui.theme.SessionPalette
import org.neteinstein.loopgain.ui.theme.TitleRed

/** Top bar shared by every session screen: wordmark, stage label, and the running session clock. */
@Composable
fun SessionHeader(
    stageLabel: String,
    clock: String,
    running: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(SessionPalette.Background)
            .windowInsetsPadding(WindowInsets.statusBars)
            .border(width = 0.dp, color = SessionPalette.Border)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "LoopGain",
                color = TitleRed,
                fontSize = 19.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp,
            )
            Text(
                text = stageLabel,
                color = SessionPalette.MutedLabel,
                fontSize = 9.sp,
                letterSpacing = 1.4.sp,
            )
        }
        if (running) {
            Row(
                modifier = Modifier.border(1.dp, SessionPalette.Accent).padding(horizontal = 9.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(7.dp),
            ) {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier.size(5.dp).clip(CircleShape).background(SessionPalette.AccentBright),
                )
                Text(text = clock, color = SessionPalette.Ink, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
