package org.neteinstein.loopgain.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// LoopGain brand colors - based on the official theme specification
// Primary, Secondary, and Background colors
private val NavyBlue = Color(0xFF0D2254) // Primary color - headers, primary buttons, heavy text
private val SteelBlue = Color(0xFF86B3D1) // Secondary color - accents, differentiating categories
private val SkyBlue = Color(0xFFB9D9EB) // Background color - soft backgrounds, cards
private val Cerulean = Color(0xFF4A90E2) // Secondary accent color

// Card type specific colors
object CardColors {
    val Motto = Color(0xFF0D2254) // Deep Navy - MOTTO card
    val PersonalQuestion = Color(0xFFB9D9EB) // Sky Blue - PERSONAL QUESTION card
    val Improvements = Color(0xFF86B3D1) // Steel Blue - IMPROVEMENTS card
    val PositiveReinforcement = Color(0xFF4A90E2) // Cerulean - POSITIVE REINFORCEMENT card
    val Typography = Color(0xFFFFFFFF) // Pure White - typography and card edges
}

private val LightColorScheme = lightColorScheme(
    primary = NavyBlue,
    secondary = Cerulean,
    tertiary = SteelBlue,
    background = SkyBlue,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = NavyBlue,
    onSurface = NavyBlue,
)

private val DarkColorScheme = darkColorScheme(
    primary = SkyBlue,
    secondary = Cerulean,
    tertiary = NavyBlue,
    background = Color(0xFF1C1B1F),
    surface = Color(0xFF1C1B1F),
    onPrimary = NavyBlue,
    onSecondary = Color.White,
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
