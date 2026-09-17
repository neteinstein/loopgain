package org.neteinstein.loopgain.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.neteinstein.loopgain.domain.model.CardLevel
import org.neteinstein.loopgain.ui.theme.CardStyles
import org.neteinstein.loopgain.ui.theme.LocalSessionColors
import org.neteinstein.loopgain.ui.viewmodel.CategoryDepthUi

/**
 * One tile of the setup screen's "depth, per category" grid. Motto has no levels — [depth] with
 * an empty `levels` list renders a static swatch with no picker rather than defaulting to one.
 */
@Composable
fun DepthPickerTile(
    depth: CategoryDepthUi,
    onPick: (CardLevel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val swatch = CardStyles.forCategory(depth.category).containerColor
    Column(
        modifier = modifier
            .border(1.dp, swatch.copy(alpha = 0.35f))
            .background(swatch.copy(alpha = 0.1f))
            .padding(8.dp),
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(swatch),
        )
        Text(
            text = depth.label.uppercase(),
            color = LocalSessionColors.current.Ink,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.6.sp,
            modifier = Modifier.padding(top = 8.dp),
        )
        if (depth.hasLevels) {
            Column(
                modifier = Modifier.padding(top = 6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                depth.levels.forEach { level ->
                    val selected = depth.selectedLevel == level
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, if (selected) swatch else LocalSessionColors.current.Border)
                            .background(if (selected) LocalSessionColors.current.PanelBackground else Color.Transparent)
                            .clickable { onPick(level) }
                            .padding(vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        LevelDots(level = level, activeColor = swatch, inactiveColor = LocalSessionColors.current.Disabled)
                    }
                }
            }
        } else {
            Text(
                text = "—",
                color = LocalSessionColors.current.MutedSecondary,
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 12.dp),
            )
        }
    }
}
