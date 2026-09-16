package org.neteinstein.loopgain.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import org.neteinstein.loopgain.domain.model.AppTheme

/**
 * Whether the resolved theme is currently dark — read by session-flow screens to pick between
 * [SessionPalette] and [SessionPaletteDark], independent of Material's own colour roles (those
 * screens deliberately don't use `MaterialTheme.colorScheme`; see [SessionPalette]).
 */
val LocalIsDarkTheme = staticCompositionLocalOf { false }

// LoopGain brand colors - based on the official theme specification
// Primary, Secondary, and Background colors
private val NavyBlue = Color(0xFF0D2254) // Primary color - headers, primary buttons, heavy text
private val SteelBlue = Color(0xFF86B3D1) // Tertiary color - accents, differentiating categories
private val SkyBlue = Color(0xFFB9D9EB) // Background color - soft backgrounds, cards
private val Cerulean = Color(0xFF4A90E2) // Secondary accent color
private val Red = Color(0xFFE5342F) // Red accent - matches the neteinstein/CoupleMoments primary red

// Card type specific colors — sampled from the printed deck photographs (docs/…pdf, pages 54 and
// 95); see .claude/skills/card-face. Treat the three blues and the red as "match the printed
// deck" targets rather than exact brand values.
object CardColors {
    val Motto = Color(0xFF0D2254) // Deep Navy - MOTTO card
    val PersonalQuestion = Color(0xFFC8DFEF) // PERSONAL QUESTION card
    val Improvements = Color(0xFFA9D0E8) // IMPROVEMENTS card
    val PositiveReinforcement = Color(0xFF5BB8E8) // POSITIVE REINFORCEMENT card
    val Typography = Color(0xFFFFFFFF) // Pure White - Motto card text only
    /** The three light cards print navy text — white on Sky Blue fails contrast. */
    val TypographyOnLight = Color(0xFF0D2254)
}

/** The brand's title color — matches neteinstein/CoupleMoments' primary red. */
val TitleRed = Red

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
    onSurfaceVariant = Color(0xFF5C7794),
    outline = Color(0xFFB7C6D6),
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
    onSurfaceVariant = Color(0xFFA8B8C9),
    outline = Color(0xFF49525C),
)

/** Resolves [AppTheme.SYSTEM] against the platform's current setting; [LIGHT]/[DARK] are explicit. */
@Composable
fun LoopGainTheme(
    theme: AppTheme = AppTheme.SYSTEM,
    content: @Composable () -> Unit
) {
    val darkTheme = when (theme) {
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
        AppTheme.SYSTEM -> isSystemInDarkTheme()
    }
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    CompositionLocalProvider(LocalIsDarkTheme provides darkTheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}
