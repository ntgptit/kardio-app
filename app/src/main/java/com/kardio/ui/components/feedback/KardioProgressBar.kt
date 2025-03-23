package com.kardio.ui.components.feedback

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kardio.ui.theme.Accent
import com.kardio.ui.theme.BackgroundSecondary
import com.kardio.ui.theme.KardioTheme

/**
 * Progress bar component for Kardio.
 * Displays a horizontal progress indicator with customizable appearance.
 *
 * @param progress Current progress value (between 0 and maxProgress)
 * @param maxProgress Maximum progress value
 * @param modifier Modifier to be applied to the progress bar
 * @param height Height of the progress bar
 * @param cornerRadius Corner radius of the progress bar
 * @param progressColor Color of the progress indicator
 * @param backgroundColor Color of the background
 * @param animationDurationMs Duration of the progress animation in milliseconds
 */
@Composable
fun KardioProgressBar(
    progress: Float,
    maxProgress: Float = 100f,
    modifier: Modifier = Modifier,
    height: Dp = 8.dp,
    cornerRadius: Dp = 4.dp,
    progressColor: Color = Accent,
    backgroundColor: Color = BackgroundSecondary,
    animationDurationMs: Int = 300
) {
    // Clamp progress between 0 and maxProgress
    val clampedProgress = progress.coerceIn(0f, maxProgress)

    // Calculate progress percentage
    val progressPercent = clampedProgress / maxProgress

    // Animate progress changes
    val animatedProgress by animateFloatAsState(
        targetValue = progressPercent,
        animationSpec = tween(durationMillis = animationDurationMs),
        label = "ProgressAnimation"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor)
    ) {
        // Progress indicator
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProgress)
                .height(height)
                .background(progressColor)
        )
    }
}

/**
 * Progress bar component with additional text indicators and padding
 */
@Composable
fun KardioProgressBarWithPadding(
    progress: Float,
    maxProgress: Float = 100f,
    modifier: Modifier = Modifier,
    progressColor: Color = Accent,
    backgroundColor: Color = BackgroundSecondary
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        KardioProgressBar(
            progress = progress,
            maxProgress = maxProgress,
            progressColor = progressColor,
            backgroundColor = backgroundColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KardioProgressBarPreview() {
    KardioTheme {
        KardioProgressBar(
            progress = 75f,
            maxProgress = 100f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KardioProgressBarHalfPreview() {
    KardioTheme {
        KardioProgressBar(
            progress = 50f,
            maxProgress = 100f,
            height = 12.dp,
            cornerRadius = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KardioProgressBarEmptyPreview() {
    KardioTheme {
        KardioProgressBar(
            progress = 0f,
            maxProgress = 100f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KardioProgressBarCustomColorsPreview() {
    KardioTheme {
        KardioProgressBar(
            progress = 60f,
            maxProgress = 100f,
            progressColor = MaterialTheme.colorScheme.tertiary,
            backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}