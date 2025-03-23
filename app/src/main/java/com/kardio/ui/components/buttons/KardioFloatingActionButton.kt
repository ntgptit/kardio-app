package com.kardio.ui.components.buttons

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.kardio.ui.theme.KardioTheme
import com.kardio.ui.theme.PrimaryButton
import com.kardio.ui.theme.TextPrimary

/**
 * Floating action button component for Kardio.
 *
 * @param onClick Callback invoked when the button is clicked
 * @param icon Icon to display in the button
 * @param modifier Modifier to be applied to the button
 * @param contentDescription Content description for accessibility
 * @param backgroundColor Background color of the FAB
 * @param contentColor Color of the icon
 * @param label Optional text label to display next to the icon (creates an Extended FAB)
 * @param expanded Whether the extended FAB is expanded to show the label
 * @param enableHaptic Whether to enable haptic feedback on press
 */
@Composable
fun KardioFloatingActionButton(
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    backgroundColor: Color = PrimaryButton,
    contentColor: Color = TextPrimary,
    label: String? = null,
    expanded: Boolean = true,
    enableHaptic: Boolean = true
) {
    val hapticFeedback = LocalHapticFeedback.current

    if (label != null) {
        // Extended FAB with icon and text
        ExtendedFloatingActionButton(
            onClick = {
                if (enableHaptic) {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                }
                onClick()
            },
            modifier = modifier,
            expanded = expanded,
            icon = {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = contentColor
                )
            },
            text = {
                Text(
                    text = label,
                    color = contentColor,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium
                )
            },
            containerColor = backgroundColor,
            contentColor = contentColor,
            shape = MaterialTheme.shapes.large
        )
    } else {
        // Regular FAB with just an icon
        FloatingActionButton(
            onClick = {
                if (enableHaptic) {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                }
                onClick()
            },
            modifier = modifier,
            shape = CircleShape,
            containerColor = backgroundColor,
            contentColor = contentColor
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = contentColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KardioFabPreview() {
    KardioTheme {
        KardioFloatingActionButton(
            onClick = {},
            icon = androidx.compose.material.icons.Icons.Default.Add,
            contentDescription = "Add"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KardioExtendedFabPreview() {
    KardioTheme {
        KardioFloatingActionButton(
            onClick = {},
            icon = androidx.compose.material.icons.Icons.Default.Add,
            contentDescription = "Add",
            label = "Create New"
        )
    }
}