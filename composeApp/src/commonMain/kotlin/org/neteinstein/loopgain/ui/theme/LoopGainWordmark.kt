package org.neteinstein.loopgain.ui.theme

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.takeOrElse
import kotlin.math.PI

private const val INFINITY_GLYPH_ID = "loopgain_oo"

/**
 * "LoopGain" set the way the deck spells it: the "oo" of "Loop" as one continuous infinity glyph
 * instead of two letters. This is the name's one correct rendering — every screen that shows the
 * brand name in running text should use this, not a plain `Text("LoopGain")`.
 *
 * @param animated when true, a bright glint travels continuously around the "oo" glyph (matches
 * [LoopGainMark]'s flow) instead of it sitting static — use on a hero/splash treatment, not in a
 * dense list of running text.
 */
@Composable
fun LoopGainWordmark(
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = FontWeight.Bold,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    animated: Boolean = false,
) {
    val resolvedSize = fontSize.takeOrElse { LocalTextStyle.current.fontSize.takeOrElse { 24.sp } }
    val resolvedColor = color.takeOrElse { LocalTextStyle.current.color.takeOrElse { Color.Black } }

    val flowTarget: Float? = if (animated) {
        val infiniteTransition = rememberInfiniteTransition()
        val progress by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(2400, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
        )
        -(PI.toFloat() / 2f) + (2f * PI.toFloat()) * progress
    } else {
        null
    }

    val text = buildAnnotatedString {
        append("L")
        appendInlineContent(INFINITY_GLYPH_ID, "oo")
        append("pGain")
    }
    // The mark's own proportions are 63.1 wide by 30.9 tall (see LoopGainInfinity) — match that
    // aspect ratio here so the glyph fills its placeholder edge to edge instead of leaving
    // letterboxed gaps, sized to roughly the font's x-height, like a real lowercase "oo".
    val glyphHeight = resolvedSize * 0.6f
    val glyphWidth = glyphHeight * (63.1f / 30.9f)
    val inlineContent = mapOf(
        INFINITY_GLYPH_ID to InlineTextContent(
            placeholder = Placeholder(
                width = glyphWidth,
                height = glyphHeight,
                placeholderVerticalAlign = PlaceholderVerticalAlign.AboveBaseline,
            ),
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawInfinityGlyph(color = resolvedColor, flowTarget = flowTarget)
            }
        },
    )

    Text(
        text = text,
        modifier = modifier,
        color = resolvedColor,
        fontSize = resolvedSize,
        fontWeight = fontWeight,
        letterSpacing = letterSpacing,
        inlineContent = inlineContent,
    )
}
