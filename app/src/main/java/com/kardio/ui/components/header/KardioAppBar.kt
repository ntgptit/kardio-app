package com.kardio.ui.components.header

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kardio.ui.theme.BackgroundPrimary
import com.kardio.ui.theme.KardioTheme
import com.kardio.ui.theme.TextPrimary

/**
 * App bar component for Kardio.
 * Provides a customizable top app bar with optional navigation icon and actions.
 *
 * @param title The title text to display in the app bar
 * @param onNavigationClick Callback for when the navigation icon is clicked
 * @param navigationIcon The navigation icon to display (null to hide)
 * @param actions Composable content for the actions area
 * @param backgroundColor Background color of the app bar
 * @param contentColor Color of the content (title and icons)
 * @param centerTitle Whether to center the title
 * @param modifier Modifier to be applied to the app bar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KardioAppBar(
    title: String,
    onNavigationClick: (() -> Unit)? = null,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit) = {},
    backgroundColor: Color = BackgroundPrimary,
    contentColor: Color = TextPrimary,
    centerTitle: Boolean = false,
    modifier: Modifier = Modifier
) {
    val navigationIconComposable: (@Composable () -> Unit)? = when {
        navigationIcon != null -> navigationIcon
        onNavigationClick != null -> {
            {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Navigate Back",
                        tint = contentColor
                    )
                }
            }
        }

        else -> null
    }

    if (centerTitle) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = navigationIconComposable ?: {},
            actions = actions,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = backgroundColor,
                titleContentColor = contentColor,
                navigationIconContentColor = contentColor,
                actionIconContentColor = contentColor
            ),
            modifier = modifier
        )
    } else {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = navigationIconComposable ?: {},
            actions = actions,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = backgroundColor,
                titleContentColor = contentColor,
                navigationIconContentColor = contentColor,
                actionIconContentColor = contentColor
            ),
            modifier = modifier
        )
    }
}

/**
 * Simple app bar with only text for screens that don't need navigation
 */
@Composable
fun KardioSimpleAppBar(
    title: String,
    backgroundColor: Color = BackgroundPrimary,
    contentColor: Color = TextPrimary,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(backgroundColor)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium,
            color = contentColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun KardioAppBarPreview() {
    KardioTheme {
        KardioAppBar(
            title = "Kardio App",
            onNavigationClick = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun KardioAppBarWithActionsPreview() {
    KardioTheme {
        KardioAppBar(
            title = "Home",
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu"
                    )
                }
            },
            actions = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KardioAppBarCenteredTitlePreview() {
    KardioTheme {
        KardioAppBar(
            title = "Profile",
            centerTitle = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KardioSimpleAppBarPreview() {
    KardioTheme {
        KardioSimpleAppBar(
            title = "Settings"
        )
    }
}