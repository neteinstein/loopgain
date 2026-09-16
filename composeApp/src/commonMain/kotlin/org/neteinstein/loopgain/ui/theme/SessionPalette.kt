package org.neteinstein.loopgain.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Chrome colours for the session flow screens (header, panels, buttons, borders) — distinct from
 * [CardStyles], which governs the printed-deck look of a card face and never changes with the
 * app theme (a physical card doesn't get a dark mode). Session chrome does: [SessionPalette] is
 * the light variant from the "Faithful deck" design, [SessionPaletteDark] is the dark variant the
 * same design used for its table-companion/game-mode flows — reused here rather than inventing
 * new colours, so light/dark is a faithful part of the same design system, not a bolt-on.
 */
interface SessionColors {
    val Background: Color
    val PanelBackground: Color
    val Ink: Color
    val Border: Color
    val Accent: Color
    val AccentBright: Color
    val MutedLabel: Color
    val MutedSecondary: Color
    val Disabled: Color
    val DisabledText: Color
    val OnAccent: Color
}

object SessionPalette : SessionColors {
    override val Background = Color(0xFFF4F6F8)
    override val PanelBackground = Color(0xFFE3ECF5)
    override val Ink = Color(0xFF0B2B5C)
    override val Border = Color(0x290B2B5C)
    override val Accent = Color(0xFF1E5FA8)
    override val AccentBright = Color(0xFF2FA8E8)
    override val MutedLabel = Color(0xFF4E7CA8)
    override val MutedSecondary = Color(0xFF94A7BC)
    override val Disabled = Color(0xFFD3DCE6)
    override val DisabledText = Color(0xFF8DA0B5)
    override val OnAccent = Color(0xFFF4F6F8)
}

/** From the design's dark table-companion/game-mode flows (1B/1C) — same roles, dark ground. */
object SessionPaletteDark : SessionColors {
    override val Background = Color(0xFF0B2B5C)
    override val PanelBackground = Color(0xFF0F3466)
    override val Ink = Color(0xFFF4F6F8)
    override val Border = Color(0xFF1E5FA8)
    override val Accent = Color(0xFF7FB9E6)
    override val AccentBright = Color(0xFF2FA8E8)
    override val MutedLabel = Color(0xFF4E7CA8)
    override val MutedSecondary = Color(0xFF8FB0CE)
    override val Disabled = Color(0xFF2A4763)
    override val DisabledText = Color(0xFF6E8CA8)
    override val OnAccent = Color(0xFF071B33)
}

/** Provided by [org.neteinstein.loopgain.ui.screens.session.SessionFlowScreen] and its tablet
 * counterpart, resolved from [LocalIsDarkTheme] — defaults to light for previews/tests that don't
 * provide it. */
val LocalSessionColors = staticCompositionLocalOf<SessionColors> { SessionPalette }
