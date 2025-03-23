package com.kardio.ui.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kardio.ui.theme.BackgroundSecondary
import com.kardio.ui.theme.Divider
import com.kardio.ui.theme.KardioTheme

/**
 * Neumorphic shape type for the card.
 */
enum class NeumorphicShapeType {
    FLAT,    // Flat neumorphic effect
    PRESSED, // Pressed-in effect
    BASIN    // Basin-like effect
}

/**
 * Neumorphic card component for Kardio.
 * Creates a card with a neumorphic design effect (soft shadows and highlights).
 *
 * @param modifier Modifier to be applied to the card
 * @param shapeType Type of neumorphic shape effect to apply
 * @param backgroundColor Background color of the card
 * @param shadowColor Color for the shadow effect
 * @param highlightColor Color for the highlight effect
 * @param shadowDistance Distance of the shadow effect
 * @param shadowRadius Radius of the shadow effect
 * @param cornerRadius Corner radius of the card
 * @param hasStroke Whether to show a stroke around the card
 * @param strokeColor Color of the stroke if enabled
 * @param strokeWidth Width of the stroke if enabled
 * @param shape Custom shape for the card
 * @param onClick Optional callback for when the card is clicked
 * @param content The content of the card
 */
@Composable
fun KardioNeumorphicCard(
    modifier: Modifier = Modifier,
    shapeType: NeumorphicShapeType = NeumorphicShapeType.FLAT,
    backgroundColor: Color = BackgroundSecondary,
    shadowColor: Color = Color.Black.copy(alpha = 0.3f),
    highlightColor: Color = Color.White.copy(alpha = 0.1f),
    shadowDistance: Dp = 6.dp,
    shadowRadius: Dp = 6.dp,
    cornerRadius: Dp = 16.dp,
    hasStroke: Boolean = false,
    strokeColor: Color = Divider,
    strokeWidth: Dp = 1.dp,
    shape: Shape = RoundedCornerShape(cornerRadius),
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val shadowOffset = when (shapeType) {
        NeumorphicShapeType.FLAT -> Offset(shadowDistance.value, shadowDistance.value)
        NeumorphicShapeType.PRESSED -> Offset(-shadowDistance.value, -shadowDistance.value)
        NeumorphicShapeType.BASIN -> Offset.Zero
    }

    val highlightOffset = when (shapeType) {
        NeumorphicShapeType.FLAT -> Offset(-shadowDistance.value, -shadowDistance.value)
        NeumorphicShapeType.PRESSED -> Offset(shadowDistance.value, shadowDistance.value)
        NeumorphicShapeType.BASIN -> Offset.Zero
    }

    // For basin type, we use an inner shadow effect
    val isBasin = shapeType == NeumorphicShapeType.BASIN

    val baseModifier = Modifier
        .clip(shape)
        .drawBehind {
            if (!isBasin) {
                // Draw shadow for flat and pressed types
                drawNeumorphicShadow(
                    color = shadowColor,
                    offset = shadowOffset,
                    radius = shadowRadius.toPx()
                )
                // Draw highlight
                drawNeumorphicShadow(
                    color = highlightColor,
                    offset = highlightOffset,
                    radius = shadowRadius.toPx()
                )
            }
        }
        .background(
            if (isBasin) {
                // For basin, create a gradient background
                Brush.radialGradient(
                    colors = listOf(
                        backgroundColor.copy(alpha = 0.8f),
                        backgroundColor
                    ),
                    center = Offset.Infinite,
                    radius = 1000f,
                    tileMode = TileMode.Clamp
                )
            } else {
                backgroundColor
            }
        )

    val cardModifier = if (onClick != null) {
        if (hasStroke) {
            KardioCard(
                modifier = modifier.then(baseModifier),
                backgroundColor = Color.Transparent,
                elevation = 0.dp,
                border = androidx.compose.foundation.BorderStroke(strokeWidth, strokeColor),
                shape = shape,
                onClick = onClick
            ) {
                Box(
                    modifier = Modifier.background(Color.Transparent),
                    contentAlignment = Alignment.Center,
                    content = content
                )
            }
        } else {
            KardioCard(
                modifier = modifier.then(baseModifier),
                backgroundColor = Color.Transparent,
                elevation = 0.dp,
                shape = shape,
                onClick = onClick
            ) {
                Box(
                    modifier = Modifier.background(Color.Transparent),
                    contentAlignment = Alignment.Center,
                    content = content
                )
            }
        }
    } else {
        // Non-clickable card
        Surface(
            modifier = modifier.then(baseModifier),
            shape = shape,
            color = Color.Transparent,
            shadowElevation = 0.dp,
            border = if (hasStroke) androidx.compose.foundation.BorderStroke(
                strokeWidth,
                strokeColor
            ) else null
        ) {
            Box(
                modifier = Modifier.background(Color.Transparent),
                contentAlignment = Alignment.Center,
                content = content
            )
        }
    }
}

// Helper function to draw neumorphic shadow
private fun DrawScope.drawNeumorphicShadow(
    color: Color,
    offset: Offset,
    radius: Float
) {
    drawCircle(
        color = color,
        radius = size.minDimension + radius,
        center = Offset(
            x = size.width / 2 + offset.x,
            y = size.height / 2 + offset.y
        ),
        alpha = 0.6f
    )
}

@Preview(showBackground = true)
@Composable
fun KardioNeumorphicCardFlatPreview() {
    KardioTheme {
        KardioNeumorphicCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            onClick = {},
            content = {
                Text(
                    text = "Flat Neumorphic Card",
                    modifier = Modifier.padding(24.dp)
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KardioNeumorphicCardPressedPreview() {
    KardioTheme {
        KardioNeumorphicCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            shapeType = NeumorphicShapeType.PRESSED,
            hasStroke = true,
            content = {
                Text(
                    text = "Pressed Neumorphic Card",
                    modifier = Modifier.padding(24.dp)
                )
            }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1E2642)
@Composable
fun KardioNeumorphicCardBasinPreview() {
    KardioTheme {
        KardioNeumorphicCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            shapeType = NeumorphicShapeType.BASIN,
            content = {
                Text(
                    text = "Basin Neumorphic Card",
                    modifier = Modifier.padding(24.dp)
                )
            }
        )
    }
}