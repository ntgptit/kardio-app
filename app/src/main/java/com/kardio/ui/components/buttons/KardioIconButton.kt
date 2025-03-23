package com.kardio.ui.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kardio.ui.theme.BackgroundSecondary
import com.kardio.ui.theme.IconPrimary
import com.kardio.ui.theme.KardioTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Icon button component for Kardio.
 * Supports haptic feedback and glow effect on press.
 *
 * @param icon The icon to display
 * @param onClick Callback invoked when the button is clicked
 * @param modifier Modifier to be applied to the button
 * @param enabled Whether the button is enabled
 * @param contentDescription Content description for accessibility
 * @param tint Color to tint the icon
 * @param size Size of the button
 * @param backgroundColor Background color of the button
 * @param enableHaptic Whether to enable haptic feedback on press
 * @param enableGlow Whether to enable glow effect on press
 */
@Composable
fun KardioIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String? = null,
    tint: Color = IconPrimary,
    size: Dp = 48.dp,
    backgroundColor: Color = BackgroundSecondary,
    enableHaptic: Boolean = true,
    enableGlow: Boolean = false
) {
    val hapticFeedback = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    var isPressed by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = {
                if (enableHaptic) {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                }

                if (enableGlow) {
                    isPressed = true
                    scope.launch {
                        delay(200)
                        isPressed = false
                    }
                }

                onClick()
            },
            enabled = enabled,
            modifier = Modifier.size(size)
        ) {
            // We can add a glow effect when pressed if enableGlow is true
            if (enableGlow && isPressed) {
                Box(
                    modifier = Modifier
                        .size(size)
                        .clip(CircleShape)
                        .background(tint.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = contentDescription,
                        tint = tint,
                        modifier = Modifier.size(size / 2)
                    )
                }
            } else {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = tint,
                    modifier = Modifier.size(size / 2)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KardioIconButtonPreview() {
    KardioTheme {
        KardioIconButton(
            icon = androidx.compose.material.icons.Icons.Default.Add,
            onClick = {},
            contentDescription = "Add"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KardioIconButtonWithGlowPreview() {
    KardioTheme {
        KardioIconButton(
            icon = androidx.compose.material.icons.Icons.Default.Favorite,
            onClick = {},
            contentDescription = "Favorite",
            tint = Color.Red,
            enableGlow = true,
            size = 56.dp
        )
    }
}