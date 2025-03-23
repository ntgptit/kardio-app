package com.kardio.ui.components.inputs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kardio.ui.theme.BackgroundInput
import com.kardio.ui.theme.KardioTheme
import com.kardio.ui.theme.TextPrimary
import com.kardio.ui.theme.TextSecondary
import com.kardio.ui.theme.TextTertiary

/**
 * Search field component for Kardio.
 * A specialized text field for search functionality.
 *
 * @param value Current search text value
 * @param onValueChange Callback for when the search text changes
 * @param onSearch Callback for when the search action is triggered
 * @param placeholder Placeholder text to show when the field is empty
 * @param modifier Modifier to be applied to the search field
 * @param backgroundColor Background color of the search field
 * @param enabled Whether the search field is enabled
 */
@Composable
fun KardioSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: (String) -> Unit = {},
    placeholder: String = "Search...",
    modifier: Modifier = Modifier,
    backgroundColor: Color = BackgroundInput,
    enabled: Boolean = true
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        // Search icon
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            modifier = Modifier.size(20.dp),
            tint = TextSecondary
        )

        // Search text field
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 28.dp, end = if (value.isNotEmpty()) 28.dp else 0.dp)
                .focusRequester(focusRequester)
                .onFocusChanged { isFocused = it.isFocused },
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = TextPrimary
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch(value)
                    focusManager.clearFocus()
                }
            ),
            singleLine = true,
            enabled = enabled,
            interactionSource = remember { MutableInteractionSource() },
            cursorBrush = SolidColor(TextPrimary),
            decorationBox = { innerTextField ->
                Box(
                    contentAlignment = Alignment.CenterStart
                ) {
                    // Placeholder
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextTertiary
                        )
                    }

                    // Actual text field content
                    innerTextField()
                }
            }
        )

        // Clear button
        AnimatedVisibility(
            visible = value.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            IconButton(
                onClick = { onValueChange("") },
                modifier = Modifier.size(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear search",
                    tint = TextSecondary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KardioSearchFieldEmptyPreview() {
    KardioTheme {
        KardioSearchField(
            value = "",
            onValueChange = {},
            placeholder = "Search study materials...",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KardioSearchFieldWithTextPreview() {
    KardioTheme {
        KardioSearchField(
            value = "flashcards",
            onValueChange = {},
            placeholder = "Search study materials...",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}