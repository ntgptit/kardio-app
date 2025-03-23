package com.kardio.ui.components.header

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kardio.R
import com.kardio.ui.theme.KardioTheme
import com.kardio.ui.theme.StreakFlameOrange
import com.kardio.ui.theme.TextPrimary

/**
 * Profile header component for Kardio.
 * Displays user profile information including avatar, username, and streak count.
 *
 * @param username The username to display
 * @param streakCount The user's current streak count
 * @param avatarResId Resource ID for the avatar image
 * @param onAvatarClick Callback for when the avatar is clicked
 * @param modifier Modifier to be applied to the header
 */
@Composable
fun KardioProfileHeader(
    username: String,
    streakCount: Int = 0,
    avatarResId: Int = R.drawable.ic_profile_placeholder,
    onAvatarClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Image(
            painter = painterResource(id = avatarResId),
            contentDescription = "Profile Avatar",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .clickable { onAvatarClick() },
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        // User info
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // Username
            Text(
                text = username,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
        }

        // Streak info (if applicable)
        if (streakCount > 0) {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = "Streak",
                    tint = StreakFlameOrange,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = streakCount.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KardioProfileHeaderPreview() {
    KardioTheme {
        KardioProfileHeader(
            username = "John Doe",
            streakCount = 7
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KardioProfileHeaderNoStreakPreview() {
    KardioTheme {
        KardioProfileHeader(
            username = "New User",
            streakCount = 0
        )
    }
}