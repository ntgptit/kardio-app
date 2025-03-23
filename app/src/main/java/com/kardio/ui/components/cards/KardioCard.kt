package com.kardio.ui.components.cards

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kardio.ui.theme.BackgroundSecondary
import com.kardio.ui.theme.Divider
import com.kardio.ui.theme.KardioTheme

/**
 * Base card component for Kardio.
 * Provides common card appearance and behavior.
 *
 * @param modifier Modifier to be applied to the card
 * @param onClick Optional callback for when the card is clicked
 * @param backgroundColor Background color of the card
 * @param contentColor Content color of the card
 * @param border Optional border for the card
 * @param elevation Elevation of the card
 * @param shape Shape of the card
 * @param animatePress Whether to animate the card when pressed
 * @param content The content of the card
 */
@Composable
fun KardioCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    backgroundColor: Color = BackgroundSecondary,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,
    shape: Shape = MaterialTheme.shapes.medium,
    animatePress: Boolean = true,
    content: @Composable () -> Unit
) {
    // If onClick is provided, we want to handle press animations
    if (onClick != null) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()
        val scale by animateFloatAsState(
            targetValue = if (isPressed && animatePress) 0.98f else 1f,
            label = "CardPressAnimation"
        )

        Card(
            modifier = modifier
                .scale(scale)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null, // We're handling visual feedback with scale
                    onClick = onClick
                ),
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor,
                contentColor = contentColor
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = elevation,
                pressedElevation = elevation + 1.dp
            ),
            border = border,
            shape = shape
        ) {
            content()
        }
    } else {
        // No click handler, no animation needed
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor,
                contentColor = contentColor
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = elevation
            ),
            border = border,
            shape = shape
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KardioCardPreview() {
    KardioTheme {
        KardioCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = 2.dp,
            border = BorderStroke(1.dp, Divider),
            onClick = {}
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Sample Card Content",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KardioCardNoClickPreview() {
    KardioTheme {
        KardioCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = 4.dp
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Non-Clickable Card",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}