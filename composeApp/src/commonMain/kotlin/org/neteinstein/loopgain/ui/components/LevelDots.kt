package org.neteinstein.loopgain.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.neteinstein.loopgain.domain.model.CardLevel

/**
 * Depth indicator matching the printed deck: filled dots up to [level], out of three. Renders
 * nothing for a null level — Motto cards have no depth and must show no dots at all, not a
 * default of one.
 */
@Composable
fun LevelDots(
    level: CardLevel?,
    activeColor: Color,
    inactiveColor: Color,
    modifier: Modifier = Modifier,
    dotSize: Dp = 6.dp,
    spacing: Dp = 4.dp,
) {
    if (level == null) return
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(spacing)) {
        repeat(3) { index ->
            val filled = index < level.dots
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .size(dotSize)
                    .clip(CircleShape)
                    .background(if (filled) activeColor else inactiveColor),
            )
        }
    }
}
