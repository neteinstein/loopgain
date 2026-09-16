package org.neteinstein.loopgain.ui.theme

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.lerp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private const val HALF_PI = (PI / 2).toFloat()
private const val TWO_PI = (2 * PI).toFloat()

/**
 * The "oo" of "Loop" as an actual infinity glyph — a lemniscate of Bernoulli, the curve the ∞
 * symbol is drawn from — rather than two separate linked rings. Sampled finely and drawn as many
 * short segments (not one flat-shaded stroke) so a highlight can travel continuously around it.
 */
private class LoopGainInfinity(size: Size, val pointsPerHalf: Int = 90) {
    val cx: Float
    val cy: Float
    val amplitude: Float
    val strokeWidth: Float
    val leftBounds: Rect
    val rightBounds: Rect

    init {
        // Traced centerline bounding box for amplitude 25 / stroke 13 (this curve's fixed
        // proportions) is ~63.1 x 30.9 units; fit that box into whatever size we're given.
        val unit = minOf(size.width / 63.1f, size.height / 30.9f)
        amplitude = 25f * unit
        strokeWidth = 13f * unit
        cx = size.width / 2f
        cy = size.height / 2f
        rightBounds = bounds(-HALF_PI, HALF_PI)
        leftBounds = bounds(HALF_PI, 3f * HALF_PI)
    }

    /** u runs continuously across the whole figure: right loop is (-π/2, π/2), left is (π/2, 3π/2). */
    fun pointAt(u: Float): Offset {
        val s = sin(u)
        val c = cos(u)
        val denom = 1f + s * s
        return Offset(cx + amplitude * c / denom, cy + amplitude * s * c / denom)
    }

    private fun bounds(u0: Float, u1: Float): Rect {
        val eps = 0.01f
        var left = Float.MAX_VALUE
        var top = Float.MAX_VALUE
        var right = -Float.MAX_VALUE
        var bottom = -Float.MAX_VALUE
        for (i in 0..pointsPerHalf) {
            val u = (u0 + (u1 - u0) * i / pointsPerHalf).coerceIn(u0 + eps, u1 - eps)
            val p = pointAt(u)
            left = minOf(left, p.x); right = maxOf(right, p.x)
            top = minOf(top, p.y); bottom = maxOf(bottom, p.y)
        }
        return Rect(left, top, right, bottom)
    }
}

/** Fraction along the highlight-gradient's own axis (matches the old static glossy look). */
private fun gradientFraction(point: Offset, bounds: Rect): Float {
    val start = Offset(bounds.left + bounds.width * 0.15f, bounds.top + bounds.height * 0.1f)
    val end = Offset(bounds.right - bounds.width * 0.1f, bounds.bottom - bounds.height * 0.1f)
    val axis = end - start
    val axisLengthSquared = axis.x * axis.x + axis.y * axis.y
    if (axisLengthSquared == 0f) return 0f
    val toPoint = point - start
    return ((toPoint.x * axis.x + toPoint.y * axis.y) / axisLengthSquared).coerceIn(0f, 1f)
}

/** Signed shortest distance from `u` to `target` on the 2π-periodic curve, in (-π, π]. */
private fun angularDelta(u: Float, target: Float): Float {
    var d = (u - target) % TWO_PI
    if (d > PI) d -= TWO_PI
    if (d < -PI) d += TWO_PI
    return d
}

/**
 * A comet's brightness at `u`: a sharp head at `target` with a short glow just ahead of it and a
 * longer trail fading out behind — reads as something moving forward, not a static soft glow.
 */
private fun flowIntensity(u: Float, target: Float, trailLength: Float, leadLength: Float = 0.12f): Float {
    val d = angularDelta(u, target) // u relative to the head: negative is behind, positive is ahead
    return when {
        d in -trailLength..0f -> {
            val x = 1f + d / trailLength // 1 at the head, 0 at the tail's end
            x * x
        }
        d in 0f..leadLength -> 1f - d / leadLength
        else -> 0f
    }
}

private fun DrawScope.drawFlowingInfinity(
    infinity: LoopGainInfinity,
    flowTarget: Float?,
    flowColor: Color,
    rightHighlight: Color,
    rightShadow: Color,
    leftHighlight: Color,
    leftShadow: Color,
) {
    val trailLength = 1.1f
    val totalPoints = infinity.pointsPerHalf * 2
    var prev = infinity.pointAt(-HALF_PI + 0.001f)
    for (i in 1..totalPoints) {
        val u = -HALF_PI + TWO_PI * i / totalPoints
        val curr = infinity.pointAt(u)
        val onRight = u < HALF_PI
        val bounds = if (onRight) infinity.rightBounds else infinity.leftBounds
        val highlight = if (onRight) rightHighlight else leftHighlight
        val shadow = if (onRight) rightShadow else leftShadow
        val fraction = gradientFraction(curr, bounds)
        var color = lerp(highlight, shadow, fraction)
        if (flowTarget != null) {
            val glint = flowIntensity(u, flowTarget, trailLength)
            if (glint > 0f) color = lerp(color, flowColor, glint)
        }
        drawLine(color = color, start = prev, end = curr, strokeWidth = infinity.strokeWidth, cap = StrokeCap.Round)
        prev = curr
    }
}

/**
 * The full-color mark: a light loop (white to Personal-Question sky-blue) woven with a deep loop
 * (Positive-Reinforcement cyan to navy), with a bright glint continuously travelling once around
 * the whole figure — energy flowing through the loop, rather than the mark fading in and out.
 */
@Composable
fun LoopGainMark(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
    )
    Canvas(modifier = modifier) {
        val infinity = LoopGainInfinity(size)
        drawFlowingInfinity(
            infinity = infinity,
            flowTarget = -HALF_PI + TWO_PI * progress,
            flowColor = Color.White,
            rightHighlight = Color(0xFF3D7AC2),
            rightShadow = Color(0xFF122C56),
            leftHighlight = Color(0xFFAFCBE8),
            leftShadow = Color(0xFF6E97C4),
        )
    }
}

/**
 * The "oo" of "Loop" as a single-color inline glyph, for dropping into running text — e.g.
 * [LoopGainWordmark]. `flowTarget` is null for a plain static glyph, or the current animated
 * position (see [LoopGainMark]) to carry the same travelling glint into running text.
 */
internal fun DrawScope.drawInfinityGlyph(color: Color, flowTarget: Float? = null) {
    val infinity = LoopGainInfinity(size)
    // A fixed accent rather than a tint of `color`, so the glint shows up whether the surrounding
    // text is light or dark instead of disappearing into it (e.g. white glint on white text).
    val flowColor = Color(0xFF5BB8E8)
    drawFlowingInfinity(
        infinity = infinity,
        flowTarget = flowTarget,
        flowColor = flowColor,
        rightHighlight = color,
        rightShadow = color,
        leftHighlight = color,
        leftShadow = color,
    )
}
