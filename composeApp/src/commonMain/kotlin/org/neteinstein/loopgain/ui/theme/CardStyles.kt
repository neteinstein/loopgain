package org.neteinstein.loopgain.ui.theme

import androidx.compose.ui.graphics.Color
import org.neteinstein.loopgain.domain.model.CardCategory

/** Category → colour, for the one card face composable shared by every screen that shows a card. */
data class CardStyle(
    val containerColor: Color,
    val contentColor: Color,
    /** Tone-on-tone label colour on light cards; white on the navy Motto card. */
    val labelColor: Color,
)

object CardStyles {
    fun forCategory(category: CardCategory): CardStyle = when (category) {
        CardCategory.MOTTO -> CardStyle(
            containerColor = CardColors.Motto,
            contentColor = CardColors.Typography,
            labelColor = CardColors.Typography,
        )
        CardCategory.POSITIVE_REINFORCEMENT -> CardStyle(
            containerColor = CardColors.PositiveReinforcement,
            contentColor = CardColors.TypographyOnLight,
            labelColor = Color(0xFF1268A0),
        )
        CardCategory.IMPROVEMENTS -> CardStyle(
            containerColor = CardColors.Improvements,
            contentColor = CardColors.TypographyOnLight,
            labelColor = Color(0xFF1E5FA8),
        )
        CardCategory.PERSONAL_QUESTION -> CardStyle(
            containerColor = CardColors.PersonalQuestion,
            contentColor = CardColors.TypographyOnLight,
            labelColor = Color(0xFF3A6C9B),
        )
    }
}
