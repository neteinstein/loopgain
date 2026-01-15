package org.neteinstein.loopgain.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// LoopGain brand colors - based on the card deck image
private val LoopGainBlue = Color(0xFF1E3A5F) // Dark blue from card
private val LoopGainLightBlue = Color(0xFF7FB3D5) // Light blue from card
private val LoopGainAccent = Color(0xFF4A90E2) // Medium blue accent

private val LightColorScheme = lightColorScheme(
    primary = LoopGainBlue,
    secondary = LoopGainAccent,
    tertiary = LoopGainLightBlue,
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
)

private val DarkColorScheme = darkColorScheme(
    primary = LoopGainLightBlue,
    secondary = LoopGainAccent,
    tertiary = LoopGainBlue,
    background = Color(0xFF1C1B1F),
    surface = Color(0xFF1C1B1F),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.White,
    onBackground = Color(0xFFE6E1E5),
    onSurface = Color(0xFFE6E1E5),
)

@Composable
fun LoopGainTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
