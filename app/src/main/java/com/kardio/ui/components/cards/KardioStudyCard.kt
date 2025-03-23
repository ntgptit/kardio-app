package com.kardio.ui.components.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kardio.R
import com.kardio.ui.theme.Accent
import com.kardio.ui.theme.KardioTheme
import com.kardio.ui.theme.TextPrimary
import com.kardio.ui.theme.TextSecondary

/**
 * Study card component for Kardio.
 * Displays study material information with icon, title, subtitle and count.
 *
 * @param title The title of the study card
 * @param subtitle Optional subtitle for the study card
 * @param count Optional count indicator (e.g., number of terms)
 * @param icon Icon to display on the card
 * @param iconTint Tint color for the icon
 * @param onClick Callback invoked when the card is clicked
 * @param modifier Modifier to be applied to the card
 */
@Composable
fun KardioStudyCard(
    title: String,
    subtitle: String? = null,
    count: String? = null,
    icon: Painter,
    iconTint: Color = Accent,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    KardioCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Icon(
                painter = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Title and subtitle
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }