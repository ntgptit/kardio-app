package com.kardio.ui.components.feedback

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kardio.ui.theme.Accent
import com.kardio.ui.theme.KardioTheme
import com.kardio.ui.theme.TextSecondary

/**
 * Loading indicator component for Kardio.
 * Shows a circular progress indicator with an optional message.
 *
 * @param isLoading Whether to show the loading indicator
 * @param message Optional message to display below the indicator
 * @param modifier Modifier to be applied to the loading indicator
 * @param color Color of the progress indicator
 * @param strokeWidth Width of the progress indicator stroke
 */
@Composable
fun KardioLoadingIndicator(
    isLoading: Boolean,
    message: String? = null,
    modifier: Modifier = Modifier,
    color: Color = Accent,
    strokeWidth: Float = 3f
) {
    AnimatedVisibility(
        visible = isLoading,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(300))
    ) {
        Column(
            modifier = modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = color,
                strokeWidth = strokeWidth.dp,
                strokeCap = StrokeCap.Round
            )

            if (message != null) {
                // Animate the alpha of the message for a subtle pulsing effect
                val infiniteTransition = rememberInfiniteTransition(label = "LoadingPulse")
                val alpha by infiniteTransition.animateFloat(
                    initialValue = 0.6f,
                    targetValue = 1.0f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "LoadingTextAlpha"
                )

                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .alpha(alpha)
                )
            }
        }
    }
}

/**
 * Full screen loading indicator that covers the entire screen with a semi-transparent background.
 *
 * @param isLoading Whether to show the loading indicator
 * @param message Optional message to display below the indicator
 * @param backgroundColor Background color of the full screen overlay
 */
@Composable
fun KardioFullScreenLoading(
    isLoading: Boolean,
    message: String? = null,
    backgroundColor: Color = Color.Black.copy(alpha = 0.7f)
) {
    AnimatedVisibility(
        visible = isLoading,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(300))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            KardioLoadingIndicator(
                isLoading = true,
                message = message,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KardioLoadingIndicatorPreview() {
    KardioTheme {
        Surface {
            KardioLoadingIndicator(
                isLoading = true,
                message = "Loading..."
            )
        }
    }
}

@Preview
@Composable
fun KardioFullScreenLoadingPreview() {
    KardioTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Background Content",
                modifier = Modifier.align(Alignment.Center)
            )

            KardioFullScreenLoading(
                isLoading = true,
                message = "Loading content..."
            )
        }
    }
}