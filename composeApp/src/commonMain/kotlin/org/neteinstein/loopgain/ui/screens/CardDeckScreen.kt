package org.neteinstein.loopgain.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import org.neteinstein.loopgain.ui.theme.CardColors

// Card types based on LoopGain theme specification
enum class CardType(val displayName: String, val color: Color) {
    MOTTO("MOTTO", CardColors.Motto),
    PERSONAL_QUESTION("PERSONAL QUESTION", CardColors.PersonalQuestion),
    IMPROVEMENTS("IMPROVEMENTS", CardColors.Improvements),
    POSITIVE_REINFORCEMENT("POSITIVE REINFORCEMENT", CardColors.PositiveReinforcement)
}

data class CardData(
    val type: CardType,
    val content: String,
    val rotation: Float,
    val offsetX: Float,
    val offsetY: Float
)

@Composable
fun CardDeckScreen() {
    val cards = listOf(
        CardData(
            type = CardType.MOTTO,
            content = "We are all different,\nwe probably value different\nthings and there is nothing\nwrong about that.",
            rotation = -8f,
            offsetX = -20f,
            offsetY = 30f
        ),
        CardData(
            type = CardType.PERSONAL_QUESTION,
            content = "What can we do to\nadmire our differences?",
            rotation = -4f,
            offsetX = -10f,
            offsetY = 15f
        ),
        CardData(
            type = CardType.IMPROVEMENTS,
            content = "It all begins with\ncommunication",
            rotation = 0f,
            offsetX = 0f,
            offsetY = 0f
        ),
        CardData(
            type = CardType.POSITIVE_REINFORCEMENT,
            content = "Together we can\naccomplish great things!",
            rotation = 4f,
            offsetX = 10f,
            offsetY = -15f
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(320.dp)
                .height(500.dp)
        ) {
            cards.reversed().forEachIndexed { index, card ->
                PiledCard(
                    card = card,
                    zIndex = index.toFloat()
                )
            }
        }
    }
}

@Composable
fun PiledCard(
    card: CardData,
    zIndex: Float
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .offset(x = card.offsetX.dp, y = card.offsetY.dp)
            .rotate(card.rotation)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .zIndex(zIndex),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = card.type.color
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Card type label at the top
                Text(
                    text = card.type.displayName,
                    color = CardColors.Typography,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                
                // Card content in the middle
                Text(
                    text = card.content,
                    color = CardColors.Typography,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 28.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.align(Alignment.Start)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
