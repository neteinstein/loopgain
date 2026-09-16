package org.neteinstein.loopgain.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.neteinstein.loopgain.ui.theme.LoopGainMark
import org.neteinstein.loopgain.ui.theme.LoopGainWordmark
import org.neteinstein.loopgain.ui.theme.TitleRed

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E3A5F), // Dark blue
                        Color(0xFF4A90E2)  // Medium blue
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // A glint travels continuously around the mark and the wordmark's "oo" — energy
            // flowing through the loop, rather than the old fade-in/fade-out blink.
            LoopGainMark(
                modifier = Modifier.size(width = 96.dp, height = 48.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LoopGainWordmark(
                color = TitleRed,
                fontSize = 48.sp,
                letterSpacing = 2.sp,
                animated = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Taking your team from the comfort zone to the trust zone!",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }
}
