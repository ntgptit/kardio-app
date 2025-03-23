package com.kardio.ui.components.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kardio.ui.theme.BackgroundPrimary
import com.kardio.ui.theme.KardioTheme
import com.kardio.ui.theme.TabIndicator
import com.kardio.ui.theme.TabSelected
import com.kardio.ui.theme.TabUnselected

/**
 * Tab bar component for Kardio.
 * Displays a horizontal scrollable tab layout with indicator.
 *
 * @param tabs List of tab titles
 * @param selectedTabIndex Index of the currently selected tab
 * @param onTabSelected Callback for when a tab is selected
 * @param modifier Modifier to be applied to the tab bar
 * @param backgroundColor Background color of the tab bar
 * @param selectedTabColor Color of the selected tab text
 * @param unselectedTabColor Color of the unselected tab text
 * @param indicatorColor Color of the selection indicator
 */
@Composable
fun KardioTabBar(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = BackgroundPrimary,
    selectedTabColor: Color = TabSelected,
    unselectedTabColor: Color = TabUnselected,
    indicatorColor: Color = TabIndicator
) {
    if (tabs.isEmpty()) return

    val density = LocalDensity.current
    val lazyListState = rememberLazyListState()

    // Store tab widths for precise indicator positioning
    val tabWidths = remember { mutableListOf<Int>().apply { addAll(List(tabs.size) { 0 }) } }

    // Scroll to selected tab
    LaunchedEffect(selectedTabIndex) {
        lazyListState.animateScrollToItem(
            index = selectedTabIndex.coerceIn(0, tabs.size - 1)
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
    ) {
        // Tab bar
        LazyRow(
            state = lazyListState,
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(tabs) { index, tab ->
                val isSelected = index == selectedTabIndex
                val textColor by animateColorAsState(
                    targetValue = if (isSelected) selectedTabColor else unselectedTabColor,
                    label = "TabTextColor"
                )

                // Tab item
                Box(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 12.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = false),
                            onClick = { onTabSelected(index) }
                        )
                        .wrapContentSize(Alignment.Center)
                        // Measure tab width for indicator positioning
                        .onGloballyPositioned { coordinates ->
                            if (tabWidths.size > index) {
                                tabWidths[index] = coordinates.size.width
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tab,
                        color = textColor,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Indicator
        if (tabWidths.isNotEmpty() && selectedTabIndex < tabWidths.size) {
            // Calculate indicator position
            var indicatorOffset by remember { mutableStateOf(0.dp) }
            var indicatorWidth by remember { mutableStateOf(0.dp) }

            LaunchedEffect(selectedTabIndex, tabWidths) {
                var offset = 12.dp // Initial padding
                for (i in 0 until selectedTabIndex) {
                    offset += with(density) { tabWidths[i].toDp() }
                    offset += 24.dp // Padding between tabs
                }

                indicatorOffset = offset
                indicatorWidth = with(density) { tabWidths[selectedTabIndex].toDp() }
            }

            // Animate indicator position and width
            val animatedOffset by animateDpAsState(
                targetValue = indicatorOffset,
                label = "IndicatorOffset"
            )

            val animatedWidth by animateDpAsState(
                targetValue = indicatorWidth,
                label = "IndicatorWidth"
            )

            // Draw the indicator
            Box(
                modifier = Modifier
                    .height(2.dp)
                    .width(animatedWidth)
                    .align(Alignment.BottomStart)
                    .offset(x = animatedOffset)
                    .background(indicatorColor)
            )
        }
    }
}

// Extension function for tab ripple effect
private fun Modifier.tabIndicatorOffset(
    currentTabPosition: Int,
    tabPositions: List<Int>,
    density: androidx.compose.ui.unit.Density
): Modifier = composed {
    val currentTabWidth = tabPositions.getOrNull(currentTabPosition) ?: 0
    val indicatorOffset = tabPositions
        .take(currentTabPosition)
        .sumOf { it }

    val offsetDp = with(density) { indicatorOffset.toDp() }
    val widthDp = with(density) { currentTabWidth.toDp() }

    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = offsetDp)
        .width(widthDp)
}

@Preview(showBackground = true)
@Composable
fun KardioTabBarPreview() {
    KardioTheme {
        var selectedTabIndex by remember { mutableStateOf(0) }

        val tabs = listOf("Featured", "Recent", "Popular", "Trending", "Favorites")

        KardioTabBar(
            tabs = tabs,
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KardioTabBarCustomColorsPreview() {
    KardioTheme {
        var selectedTabIndex by remember { mutableStateOf(1) }

        val tabs = listOf("Day", "Week", "Month", "Year")

        KardioTabBar(
            tabs = tabs,
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it },
            backgroundColor = MaterialTheme.colorScheme.surface,
            selectedTabColor = MaterialTheme.colorScheme.primary,
            unselectedTabColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            indicatorColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth()
        )
    }
}