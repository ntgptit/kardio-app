package com.kardio.ui.components.quiz

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kardio.ui.theme.FlashcardBackBg
import com.kardio.ui.theme.FlashcardFrontBg
import com.kardio.ui.theme.KardioTheme
import com.kardio.ui.theme.TextPrimary
import com.kardio.ui.theme.TextSecondary
import com.kardio.ui.theme.TextTertiary
import com.kardio.ui.theme.Warning

/**
 * Flashcard component for Kardio.
 * Displays a two-sided flashcard with front and back content, supports flipping animation.
 *
 * @param term Content for the front of the card (term/question)
 * @param definition Content for the back of the card (definition/answer)
 * @param pronunciation Optional pronunciation guide for the term
 * @param example Optional example for the definition
 * @param isDifficult Whether the card is marked as difficult
 * @param initialSide Initial side of the card to display (front or back)
 * @param onFlip Callback when the card is flipped
 * @param onPlayAudio Callback when the audio button is pressed
 * @param onMarkDifficult Callback when the difficult status is toggled
 * @param modifier Modifier to be applied to the flashcard
 */
@Composable
fun KardioFlashcard(
    term: String,
    definition: String,
    pronunciation: String? = null,
    example: String? = null,
    isDifficult: Boolean = false,
    initialSide: FlashcardSide = FlashcardSide.FRONT,
    onFlip: (FlashcardSide) -> Unit = {},
    onPlayAudio: () -> Unit = {},
    onMarkDifficult: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val hapticFeedback = LocalHapticFeedback.current

    // State for tracking the current side of the flashcard
    var side by remember { mutableStateOf(initialSide) }

    // Rotation animation for the flip effect
    val rotation by animateFloatAsState(
        targetValue = if (side == FlashcardSide.FRONT) 0f else 180f,
        animationSpec = tween(durationMillis = 400),
        label = "CardFlipRotation"
    )

    // Determine if front is visible based on rotation
    val isFrontVisible = rotation < 90f

    // Main card container
    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    side =
                        if (side == FlashcardSide.FRONT) FlashcardSide.BACK else FlashcardSide.FRONT
                    onFlip(side)
                }
            )
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isFrontVisible) FlashcardFrontBg else FlashcardBackBg
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Front side of the card
            if (isFrontVisible) {
                FlashcardFrontContent(
                    term = term,
                    pronunciation = pronunciation,
                    isDifficult = isDifficult,
                    onPlayAudio = onPlayAudio,
                    onMarkDifficult = { onMarkDifficult(!isDifficult) },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // Back side needs to be counter-rotated to be readable
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { rotationY = 180f }
                ) {
                    FlashcardBackContent(
                        definition = definition,
                        example = example,
                        isDifficult = isDifficult,
                        onMarkDifficult = { onMarkDifficult(!isDifficult) },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

/**
 * Content for the front side of the flashcard
 */
@Composable
private fun FlashcardFrontContent(
    term: String,
    pronunciation: String?,
    isDifficult: Boolean,
    onPlayAudio: () -> Unit,
    onMarkDifficult: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            // Center content
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
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )

                    // Pronunciation if available
                    if (!pronunciation.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = pronunciation,
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Bottom controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mark as difficult button
                IconButton(
                    onClick = onMarkDifficult
                ) {
                    Icon(
                        imageVector = if (isDifficult) Icons.Default.Star else Icons.Default.StarOutline,
                        contentDescription = if (isDifficult) "Unmark as difficult" else "Mark as difficult",
                        tint = Warning,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Flip hint text
                Text(
                    text = "Tap to flip",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextTertiary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )

                // Audio button
                IconButton(
                    onClick = onPlayAudio
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Play audio",
                        tint = TextSecondary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

/**
 * Content for the back side of the flashcard
 */
@Composable
private fun FlashcardBackContent(
    definition: String,
    example: String?,
    isDifficult: Boolean,
    onMarkDifficult: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Definition section
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Definition:",
                    style = MaterialTheme.typography.labelMedium,
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
                        text = "Example:",
                        style = MaterialTheme.typography.labelMedium,
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mark as difficult button
                IconButton(
                    onClick = onMarkDifficult
                ) {
                    Icon(
                        imageVector = if (isDifficult) Icons.Default.Star else Icons.Default.StarOutline,
                        contentDescription = if (isDifficult) "Unmark as difficult" else "Mark as difficult",
                        tint = Warning,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Flip hint text
                Text(
                    text = "Tap to flip",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextTertiary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * Sides of the flashcard
 */
enum class FlashcardSide {
    FRONT, BACK
}

@Preview(showBackground = true)
@Composable
fun KardioFlashcardFrontPreview() {
    KardioTheme {
        Box(
            modifier = Modifier
                .size(350.dp, 200.dp)
                .background(Color.DarkGray)
        ) {
            KardioFlashcard(
                term = "Ephemeral",
                definition = "Lasting for a very short time.",
                pronunciation = "/əˈfem(ə)rəl/",
                example = "Ephemeral pleasures."
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KardioFlashcardBackPreview() {
    KardioTheme {
        Box(
            modifier = Modifier
                .size(350.dp, 200.dp)
                .background(Color.DarkGray)
        ) {
            KardioFlashcard(
                term = "Serendipity",
                definition = "The occurrence and development of events by chance in a happy or beneficial way.",
                example = "A fortunate stroke of serendipity.",
                initialSide = FlashcardSide.BACK,
                isDifficult = true
            )
        }
    }
}