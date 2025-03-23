package com.kardio.ui.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kardio.ui.theme.Accent
import com.kardio.ui.theme.BackgroundSecondary
import com.kardio.ui.theme.KardioTheme
import com.kardio.ui.theme.TextPrimary

/**
 * Glassmorphic card component for Kardio.
 * Creates a card with a frosted glass effect.
 *
 * @param modifier Modifier to be applied to the card
 * @param backgroundColor Base background color of the card
 * @param backgroundAlpha Alpha value for the background
 * @param borderColor Color for the border
 * @param borderWidth Width of the border
 * @param borderAlpha Alpha value for the border
 * @param glowColor Color for the glow effect
 * @param glowAlpha Alpha value for the glow
 * @param glowRadius Radius of the glow effect
 * @param glowEnabled Whether to enable the glow effect
 * @param blurRadius Blur radius for the glassmorphic effect
 * @param cornerRadius Corner radius of the card
 * @param shape Custom shape for the card
 * @param onClick Optional callback for when the card is clicked
 * @param content The content of the card
 */
@Composable
fun KardioGlassmorphicCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = BackgroundSecondary.copy(alpha = A_BACKGROUND),
    backgroundAlpha: Float = A_BACKGROUND,
    borderColor: Color = Color.White.copy(alpha = A_BORDER),
    borderWidth: Dp = 0.5.dp,
    borderAlpha: Float = A_BORDER,
    glowColor: Color = Accent.copy(alpha = A_GLOW),
    glowAlpha: Float = A_GLOW,
    glowRadius: Dp = 16.dp,
    glowEnabled: Boolean = true,
    blurRadius: Dp = 16.dp,
    cornerRadius: Dp = 16.dp,
    shape: Shape = RoundedCornerShape(cornerRadius),
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val baseModifier = Modifier
        .clip(shape)
        .blur(blurRadius)
        .background(backgroundColor.copy(alpha = backgroundAlpha))
        .then(
            if (glowEnabled) {
                Modifier.drawBehind {
                    drawGlassmorphicGlow(
                        color = glowColor.copy(alpha = glowAlpha),
                        radius = glowRadius.toPx()
                    )
                }
            } else {
                Modifier
            }
        )

    if (onClick != null) {
        KardioCard(
            modifier = modifier.then(baseModifier),
            backgroundColor = Color.Transparent,
            border = BorderStroke(borderWidth, borderColor.copy(alpha = borderAlpha)),
            shape = shape,
            onClick = onClick
        ) {
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center,
                content = content
            )
        }
    } else {
        Surface(
            modifier = modifier.then(baseModifier),
            shape = shape,
            color = Color.Transparent,
            border = BorderStroke(borderWidth, borderColor.copy(alpha = borderAlpha))
        ) {
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center,
                content = content
            )
        }
    }
}

// Helper function to draw glassmorphic glow effect
private fun DrawScope.drawGlassmorphicGlow(
    color: Color,
    radius: Float
) {
    // Radial gradient for glow effect
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                color,
                color.copy(alpha = 0.5f),
                color.copy(alpha = 0f)
            ),
            center = Offset(size.width / 2, size.height / 2),
            radius = radius * 2
        ),
        radius = size.minDimension / 2,
        center = Offset(size.width / 2, size.height / 2)
    )
}

// Default alpha values for the glassmorphic effect
private const val A_BACKGROUND = 0.5f
private const val A_BORDER = 0.3f
private const val A_GLOW = 0.25f

@Preview(showBackground = true, backgroundColor = 0xFF0F1429)
@Composable
fun KardioGlassmorphicCardPreview() {
    KardioTheme {
        KardioGlassmorphicCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            onClick = {},
            content = {
                Text(
                    text = "Glassmorphic Card",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    modifier = Modifier.padding(24.dp)
                )
            }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1429)
@Composable
fun KardioGlassmorphicCardNoGlowPreview() {
    KardioTheme {
        KardioGlassmorphicCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            glowEnabled = false,
            borderColor = Color.White.copy(alpha = 0.5f),
            borderWidth = 1.dp,
            content = {
                Text(
                    text = "No Glow Card",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    modifier = Modifier.padding(24.dp)
                )
            }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1429)
@Composable
fun KardioGlassmorphicCardColoredGlowPreview() {
    KardioTheme {
        KardioGlassmorphicCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            glowColor = Color.Red.copy(alpha = 0.3f),
            glowRadius = 24.dp,
            content = {
                Text(
                    text = "Red Glow Card",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    modifier = Modifier.padding(24.dp)
                )
            }
        )
    }
}