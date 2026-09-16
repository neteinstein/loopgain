package org.neteinstein.loopgain.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import org.neteinstein.loopgain.ui.viewmodel.PileUi

private const val PILE_FLIP_HALF_MS = 160

/**
 * Wraps a pile tile's [content] so that whenever the drawn card for [pile] changes — a fresh draw
 * or a redraw — the tile flips, as if the old card were tucked to the bottom of the pile and the
 * next one turned face-up. Shared by the phone's [PileTile] and the tablet's draw grid so both
 * animate the same way.
 *
 * [content] always receives the last-settled [PileUi], never the live one mid-transition, so it
 * never has to render a card half-way through a flip.
 */
@Composable
fun PileFlip(
    pile: PileUi,
    onTap: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (PileUi) -> Unit,
) {
    var shown by remember { mutableStateOf(pile) }
    val rotationY = remember { Animatable(0f) }

    LaunchedEffect(pile) {
        val cardChanged = shown.isDrawn != pile.isDrawn || shown.code != pile.code
        if (cardChanged) {
            rotationY.animateTo(90f, tween(PILE_FLIP_HALF_MS, easing = FastOutSlowInEasing))
            shown = pile
            rotationY.snapTo(-90f)
            rotationY.animateTo(0f, tween(PILE_FLIP_HALF_MS, easing = FastOutSlowInEasing))
        } else {
            shown = pile
        }
    }

    Box(
        modifier = modifier
            .graphicsLayer {
                this.rotationY = rotationY.value
                cameraDistance = 12f * density
            }
            .clickable(onClick = onTap),
    ) {
        content(shown)
    }
}
