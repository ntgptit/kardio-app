package com.kardio.ui.components.cards

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kardio.ui.theme.FlashcardBackBg
import com.kardio.ui.theme.FlashcardFrontBg
import com.kardio.ui.theme.TextPrimary
import com.kardio.ui.theme.TextSecondary
import com.kardio.ui.theme.TextTertiary
import com.kardio.ui.theme.Warning

/**
 * Trạng thái của flashcard
 */
enum class FlashcardSide {
    FRONT, BACK
}

/**
 * Component Flashcard - chuyển từ component_flashcard.xml
 *
 * @param term Từ hiển thị mặt trước
 * @param definition Định nghĩa hiển thị mặt sau
 * @param pronunciation Phát âm (nếu có)
 * @param example Ví dụ (nếu có)
 * @param onFlip Callback khi lật card
 * @param onPlayAudio Callback khi nhấn nút phát âm
 * @param onMarkDifficult Callback khi đánh dấu khó
 * @param isDifficult Trạng thái đánh dấu khó hiện tại
 * @param initialSide Side ban đầu
 * @param animationSpec Animation spec cho hiệu ứng lật
 */
@Composable
fun KardioFlashcard(
    term: String,
    definition: String,
    pronunciation: String? = null,
    example: String? = null,
    onFlip: (FlashcardSide) -> Unit = {},
    onPlayAudio: () -> Unit = {},
    onMarkDifficult: (Boolean) -> Unit = {},
    isDifficult: Boolean = false,
    initialSide: FlashcardSide = FlashcardSide.FRONT,
    animationSpec: AnimationSpec<Float> = tween(durationMillis = 400)
) {
    // State
    var side by remember { mutableStateOf(initialSide) }
    var rotation by remember { mutableFloatStateOf(0f) }

    // Xác định side dựa trên rotation
    val isFrontVisible = if (rotation < 90f) true else false

    LaunchedEffect(side) {
        val targetRotation = if (side == FlashcardSide.FRONT) 0f else 180f

        animate(
            initialValue = rotation,
            targetValue = targetRotation,
            animationSpec = animationSpec
        ) { value, _ ->
            rotation = value
        }
    }

    // Cấu hình graphics layer cho animation
    val graphicsLayerConfig: GraphicsLayerScope.() -> Unit = {
        rotationY = rotation
        cameraDistance = 12f * density
    }

    // Flip action
    val flipCard = {
        side = if (side == FlashcardSide.FRONT) FlashcardSide.BACK else FlashcardSide.FRONT
        onFlip(side)
    }

    // Main container
    KardioGlassmorphicCard(
        modifier = Modifier.fillMaxSize(),
        glowEnabled = true,
        backgroundColor = if (isFrontVisible) FlashcardFrontBg else FlashcardBackBg
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { flipCard() }
                .graphicsLayer(graphicsLayerConfig)
        ) {
            // Front side
            if (isFrontVisible) {
                FlashcardFrontSide(
                    term = term,
                    pronunciation = pronunciation,
                    onPlayAudio = onPlayAudio,
                    onMarkDifficult = { onMarkDifficult(!isDifficult) },
                    isDifficult = isDifficult
                )
            }
            // Back side - we need to flip it 180 degrees to make it readable
            else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { rotationY = 180f }  // Counter-rotate so text is readable
                ) {
                    FlashcardBackSide(
                        definition = definition,
                        example = example,
                        onMarkDifficult = { onMarkDifficult(!isDifficult) },
                        isDifficult = isDifficult
                    )
                }
            }
        }
    }
}

@Composable
private fun FlashcardFrontSide(
    term: String,
    pronunciation: String?,
    onPlayAudio: () -> Unit,
    onMarkDifficult: () -> Unit,
    isDifficult: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Main content (centered)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Term
                Text(
                    text = term,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    textAlign = TextAlign.Center,
                    color = TextPrimary
                )

                // Pronunciation
                if (!pronunciation.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = pronunciation,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = TextSecondary
                    )
                }
            }
        }

        // Bottom controls
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Mark difficult button
            IconButton(
                onClick = onMarkDifficult,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = if (isDifficult) Icons.Default.Star else Icons.Default.StarOutline,
                    contentDescription = "Mark as difficult",
                    tint = Warning
                )
            }

            // Tap to flip text
            Text(
                text = "Chạm để lật thẻ",
                style = MaterialTheme.typography.bodySmall,
                color = TextTertiary,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.BottomCenter)
            )

            // Audio button
            IconButton(
                onClick = onPlayAudio,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Play audio",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun FlashcardBackSide(
    definition: String,
    example: String?,
    onMarkDifficult: () -> Unit,
    isDifficult: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Definition section
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Định nghĩa:",
                style = MaterialTheme.typography.bodySmall,
                color = TextTertiary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = definition,
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary
            )

            // Example section if available
            if (!example.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Ví dụ:",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextTertiary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = example,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontStyle = FontStyle.Italic
                    ),
                    color = TextPrimary
                )
            }
        }

        // Bottom controls
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Mark difficult button
            IconButton(
                onClick = onMarkDifficult,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = if (isDifficult) Icons.Default.Star else Icons.Default.StarOutline,
                    contentDescription = "Mark as difficult",
                    tint = Warning
                )
            }

            // Tap to flip text
            Text(
                text = "Chạm để lật thẻ",
                style = MaterialTheme.typography.bodySmall,
                color = TextTertiary,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}