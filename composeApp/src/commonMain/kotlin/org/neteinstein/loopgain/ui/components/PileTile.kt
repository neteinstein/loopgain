package org.neteinstein.loopgain.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.neteinstein.loopgain.domain.model.Language
import org.neteinstein.loopgain.ui.theme.CardStyles
import org.neteinstein.loopgain.ui.theme.LocalSessionColors
import org.neteinstein.loopgain.ui.viewmodel.PileUi
import org.neteinstein.loopgain.ui.viewmodel.SessionCopy

/** One of the four draw piles: face-down until tapped, then shows its drawn code. */
@Composable
fun PileTile(pile: PileUi, language: Language, onTap: () -> Unit, modifier: Modifier = Modifier) {
    val swatch = CardStyles.forCategory(pile.category).containerColor
    Column(
        modifier = modifier
            .fillMaxSize()
            .border(1.dp, swatch.copy(alpha = if (pile.isDrawn) 0.55f else 0.3f))
            .background(swatch.copy(alpha = if (pile.isDrawn) 0.2f else 0.1f))
            .clickable(onClick = onTap)
            .padding(11.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            Text(
                text = pile.label.uppercase(),
                color = swatch,
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp,
            )
            LevelDots(
                level = pile.level,
                activeColor = swatch,
                inactiveColor = LocalSessionColors.current.Disabled,
                modifier = Modifier.padding(top = 7.dp),
            )
        }
        Column {
            Text(
                text = pile.code ?: "TAP",
                color = LocalSessionColors.current.Ink,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = SessionCopy.pileAvailabilityLabel(pile.isDrawn, pile.availableCount, pile.heldBackCount, language),
                color = LocalSessionColors.current.MutedSecondary,
                fontSize = 10.sp,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}
