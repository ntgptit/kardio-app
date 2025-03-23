package com.kardio.ui.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kardio.ui.theme.Accent
import com.kardio.ui.theme.BackgroundSecondary
import com.kardio.ui.theme.KardioTheme
import com.kardio.ui.theme.TextPrimary

/**
 * Secondary button component for Kardio.
 * Has an outlined appearance and can be customized with icons and colors.
 *
 * @param text The text to display on the button
 * @param onClick Callback invoked when the button is clicked
 * @param modifier Modifier to be applied to the button
 * @param enabled Whether the button is enabled
 * @param icon Optional icon to display before the text
 * @param backgroundColor Background color of the button
 * @param textColor Color of the text and icon
 * @param borderColor Color of the button's border
 */
@Composable
fun KardioSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    backgroundColor: Color = BackgroundSecondary,
    textColor: Color = TextPrimary,
    borderColor: Color = Accent
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        border = BorderStroke(1.dp, borderColor),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = textColor
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = textColor,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KardioSecondaryButtonPreview() {
    KardioTheme {
        KardioSecondaryButton(
            text = "Secondary Button",
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KardioSecondaryButtonWithPaddingPreview() {
    KardioTheme {
        KardioSecondaryButton(
            text = "With Padding",
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}