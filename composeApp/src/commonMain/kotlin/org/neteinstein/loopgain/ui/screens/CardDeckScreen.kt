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

data class CardData(
    val title: String,
    val content: String,
    val backgroundColor: Color,
    val rotation: Float,
    val offsetX: Float,
    val offsetY: Float
)

@Composable
fun CardDeckScreen() {
    val cards = listOf(
        CardData(
            title = "MOTTO",
            content = "We are all different,\nwe probably value different\nthings and there is nothing\nwrong about that.",
            backgroundColor = Color(0xFF1E3A5F), // Dark blue
            rotation = -8f,
            offsetX = -20f,
            offsetY = 30f
        ),
        CardData(
            title = "",
            content = "What can we do to\nadmire our differences?",
            backgroundColor = Color(0xFF5A8FBF), // Medium blue
            rotation = -4f,
            offsetX = -10f,
            offsetY = 15f
        ),
        CardData(
            title = "",
            content = "It all begins with\ncommunication",
            backgroundColor = Color(0xFF7FB3D5), // Light blue
            rotation = 0f,
            offsetX = 0f,
            offsetY = 0f
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
                .height(420.dp)
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
            containerColor = card.backgroundColor
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
                if (card.title.isNotEmpty()) {
                    Text(
                        text = card.title,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                }
                
                Text(
                    text = card.content,
                    color = Color.White,
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
