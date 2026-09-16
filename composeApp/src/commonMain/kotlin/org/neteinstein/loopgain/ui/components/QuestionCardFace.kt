package org.neteinstein.loopgain.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.neteinstein.loopgain.ui.theme.CardStyles
import org.neteinstein.loopgain.ui.viewmodel.CardFaceUi

/**
 * The printed card's face: category top-left, level dots top-right, question centred. One
 * component for the read screen, the rounds screen and (reused by other layouts) any carousel or
 * grid — see .claude/skills/card-face. [compact] scales it down for small tiles like a draw pile,
 * where the question still has to fit but the footer and full type scale don't.
 */
@Composable
fun QuestionCardFace(
    card: CardFaceUi,
    modifier: Modifier = Modifier,
    showFooter: Boolean = true,
    compact: Boolean = false,
) {
    val style = CardStyles.forCategory(card.category)
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(if (compact) 10.dp else 16.dp))
            .background(style.containerColor)
            .padding(if (compact) 11.dp else 20.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = card.label.uppercase(),
                color = style.labelColor,
                fontSize = if (compact) 9.sp else 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = if (compact) 1.sp else 1.5.sp,
            )
            LevelDots(
                level = card.level,
                activeColor = style.labelColor,
                inactiveColor = style.contentColor.copy(alpha = 0.25f),
                dotSize = if (compact) 5.dp else 6.dp,
                spacing = if (compact) 3.dp else 4.dp,
            )
        }

        Text(
            text = card.text,
            color = style.contentColor,
            fontSize = if (compact) 13.sp else 24.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = if (compact) 16.sp else 30.sp,
            maxLines = if (compact) 6 else Int.MAX_VALUE,
            overflow = if (compact) TextOverflow.Ellipsis else TextOverflow.Clip,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = if (compact) 6.dp else 12.dp),
        )

        if (showFooter) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = card.about, color = style.contentColor.copy(alpha = 0.7f), fontSize = 12.sp)
                Text(text = card.code, color = style.contentColor.copy(alpha = 0.7f), fontSize = 12.sp)
            }
        }
    }
}

/**
 * `_` in a question is a placeholder for the teammate being discussed. Render it as a visibly
 * blank run, not a single easy-to-miss underscore character and never a substituted name.
 */
internal fun String.asBlankedQuestion(): String = replace("_", "_____")
