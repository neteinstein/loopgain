package org.neteinstein.loopgain.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Chrome colours for the session flow screens (header, panels, buttons, borders) — distinct from
 * [CardStyles], which governs the printed-deck look of a card face. These come from the
 * "Faithful deck" design (light theme; the tablet/table-companion layouts use a dark variant of
 * the same roles).
 */
object SessionPalette {
    val Background = Color(0xFFF4F6F8)
    val PanelBackground = Color(0xFFE3ECF5)
    val Ink = Color(0xFF0B2B5C)
    val Border = Color(0x290B2B5C)
    val Accent = Color(0xFF1E5FA8)
    val AccentBright = Color(0xFF2FA8E8)
    val MutedLabel = Color(0xFF4E7CA8)
    val MutedSecondary = Color(0xFF94A7BC)
    val Disabled = Color(0xFFD3DCE6)
    val DisabledText = Color(0xFF8DA0B5)
    val OnAccent = Color(0xFFF4F6F8)
}
