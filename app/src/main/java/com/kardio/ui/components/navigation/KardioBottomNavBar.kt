package com.kardio.ui.components.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kardio.ui.theme.BackgroundPrimary
import com.kardio.ui.theme.KardioTheme
import com.kardio.ui.theme.NavItemActive
import com.kardio.ui.theme.NavItemInactive

/**
 * Data class representing a bottom navigation item
 */
data class BottomNavItem(
    val icon: ImageVector,
    val label: String,
    val contentDescription: String? = null
)

/**
 * Bottom navigation bar component for Kardio.
 *
 * @param items List of navigation items to display
 * @param selectedItemIndex Index of the currently selected item
 * @param onItemSelected Callback for when an item is selected
 * @param isAnimationEnabled Whether to enable animations on item selection
 * @param modifier Modifier to be applied to the bottom nav bar
 */
@Composable
fun KardioBottomNavBar(
    items: List<BottomNavItem>,
    selectedItemIndex: Int,
    onItemSelected: (Int) -> Unit,
    isAnimationEnabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    // Limit item count
    val displayItems = items.take(5)

    Surface(
        color = BackgroundPrimary,
        tonalElevation = 2.dp,
        shadowElevation = 4.dp,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(BackgroundPrimary),
            verticalAlignment = Alignment.CenterVertically
        ) {
            displayItems.forEachIndexed { index, item ->
                BottomNavItem(
                    item = item,
                    isSelected = index == selectedItemIndex,
                    onSelected = { onItemSelected(index) },
                    animateSelection = isAnimationEnabled
                )
            }
        }
    }
}

/**
 * Individual bottom navigation item
 */
@Composable
private fun RowScope.BottomNavItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onSelected: () -> Unit,
    animateSelection: Boolean
) {
    val hapticFeedback = LocalHapticFeedback.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .weight(1f)
            .clickable(
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                indication = rememberRipple(bounded = false, radius = 24.dp),
                onClick = {
                    if (animateSelection) {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                    onSelected()
                }
            )
            .padding(vertical = 8.dp)
    ) {
        // Icon
        Icon(
            imageVector = item.icon,
            contentDescription = item.contentDescription ?: item.label,
            tint = if (isSelected) NavItemActive else NavItemInactive,
            modifier = Modifier.size(24.dp)
        )

        // Label
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(animationSpec = tween(150)),
            exit = fadeOut(animationSpec = tween(150))
        ) {
            Text(
                text = item.label,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) NavItemActive else NavItemInactive,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KardioBottomNavBarPreview() {
    KardioTheme {
        var selectedIndex by remember { mutableStateOf(0) }

        val items = listOf(
            BottomNavItem(Icons.Default.Home, "Home"),
            BottomNavItem(Icons.Default.Search, "Search"),
            BottomNavItem(Icons.Default.School, "Learn"),
            BottomNavItem(Icons.Default.Person, "Profile")
        )

        Box {
            KardioBottomNavBar(
                items = items,
                selectedItemIndex = selectedIndex,
                onItemSelected = { selectedIndex = it }
            )
        }
    }
}