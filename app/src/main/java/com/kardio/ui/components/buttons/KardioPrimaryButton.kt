package com.kardio.ui.components.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kardio.ui.theme.KardioTheme
import com.kardio.ui.theme.PrimaryButton
import com.kardio.ui.theme.TextPrimary

/**
 * Primary button component for Kardio.
 *
 * @param text The text to display on the button
 * @param onClick Callback invoked when the button is clicked
 * @param modifier Modifier to be applied to the button
 * @param enabled Whether the button is enabled
 * @param isLoading Whether to show a loading indicator
 * @param icon Optional icon to display before the text
 * @param backgroundColor Custom background color (defaults to primary button color)
 * @param textColor Custom text color (defaults to text primary)
 */
@Composable
fun KardioPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    icon: ImageVector? = null,
    backgroundColor: Color = PrimaryButton,
    textColor: Color = TextPrimary
) {
    Button(
        onClick = { if (!isLoading) onClick() },
        modifier = modifier,
        enabled = enabled && !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor,
            disabledContainerColor = backgroundColor.copy(alpha = 0.6f),
            disabledContentColor = textColor.copy(alpha = 0.6f)
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = textColor,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
            } else if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KardioPrimaryButtonPreview() {
    KardioTheme {
        KardioPrimaryButton(
            text = "Button Text",
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KardioPrimaryButtonLoadingPreview() {
    KardioTheme {
        KardioPrimaryButton(
            text = "Loading...",
            onClick = {},
            isLoading = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}