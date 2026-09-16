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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.neteinstein.loopgain.ui.theme.CardStyles
import org.neteinstein.loopgain.ui.theme.SessionPalette
import org.neteinstein.loopgain.ui.viewmodel.PileUi

/** One of the four draw piles: face-down until tapped, then shows its drawn code. */
@Composable
fun PileTile(pile: PileUi, onTap: () -> Unit, modifier: Modifier = Modifier) {
    val swatch = CardStyles.forCategory(pile.category).containerColor
    Column(
        modifier = modifier
            .fillMaxSize()
            .border(1.dp, if (pile.isDrawn) swatch else SessionPalette.Border)
            .background(if (pile.isDrawn) Color.White else SessionPalette.PanelBackground)
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
                inactiveColor = SessionPalette.Disabled,
                modifier = Modifier.padding(top = 7.dp),
            )
        }
        Column {
            Text(
                text = pile.code ?: "TAP",
                color = SessionPalette.Ink,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = if (pile.isDrawn) {
                    "drawn"
                } else if (pile.heldBackCount > 0) {
                    "${pile.availableCount} ready · ${pile.heldBackCount} held back"
                } else {
                    "${pile.availableCount} ready"
                },
                color = SessionPalette.MutedSecondary,
                fontSize = 10.sp,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}
