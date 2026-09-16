package org.neteinstein.loopgain.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.neteinstein.loopgain.domain.model.Language
import org.neteinstein.loopgain.ui.theme.CardStyles
import org.neteinstein.loopgain.ui.theme.LocalSessionColors
import org.neteinstein.loopgain.ui.viewmodel.PileUi
import org.neteinstein.loopgain.ui.viewmodel.SessionCopy

private const val FLIP_HALF_MS = 160

/**
 * One of the four draw piles: face-down until tapped, then shows the drawn card's question text.
 * Tapping it again draws a new card for that category, which flips the tile — as if the old card
 * were tucked to the bottom of the pile and the next one turned face-up.
 */
@Composable
fun PileTile(pile: PileUi, language: Language, onTap: () -> Unit, modifier: Modifier = Modifier) {
    var shown by remember { mutableStateOf(pile) }
    val rotationY = remember { Animatable(0f) }

    LaunchedEffect(pile) {
        val cardChanged = shown.isDrawn != pile.isDrawn || shown.code != pile.code
        if (cardChanged) {
            rotationY.animateTo(90f, tween(FLIP_HALF_MS, easing = FastOutSlowInEasing))
            shown = pile
            rotationY.snapTo(-90f)
            rotationY.animateTo(0f, tween(FLIP_HALF_MS, easing = FastOutSlowInEasing))
        } else {
            shown = pile
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer {
                this.rotationY = rotationY.value
                cameraDistance = 12f * density
            }
            .clickable(onClick = onTap),
    ) {
        val face = shown.face
        if (shown.isDrawn && face != null) {
            QuestionCardFace(
                card = face,
                modifier = Modifier.fillMaxSize(),
                showFooter = false,
                compact = true,
            )
        } else {
            FaceDownPileTile(pile = shown, language = language, modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
private fun FaceDownPileTile(pile: PileUi, language: Language, modifier: Modifier = Modifier) {
    val swatch = CardStyles.forCategory(pile.category).containerColor
    Column(
        modifier = modifier
            .border(1.dp, LocalSessionColors.current.Border)
            .background(LocalSessionColors.current.PanelBackground)
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
                text = "TAP",
                color = LocalSessionColors.current.Ink,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = SessionCopy.pileAvailabilityLabel(false, pile.availableCount, pile.heldBackCount, language),
                color = LocalSessionColors.current.MutedSecondary,
                fontSize = 10.sp,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}
