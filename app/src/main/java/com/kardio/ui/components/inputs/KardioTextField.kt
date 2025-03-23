package com.kardio.ui.components.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kardio.ui.theme.BackgroundInput
import com.kardio.ui.theme.Error
import com.kardio.ui.theme.KardioTheme
import com.kardio.ui.theme.TextPrimary
import com.kardio.ui.theme.TextSecondary
import com.kardio.ui.theme.TextTertiary

/**
 * Text field component for Kardio.
 * Customizable text input with support for labels, icons, error states, and password input.
 *
 * @param value Current text value
 * @param onValueChange Callback for when the text value changes
 * @param modifier Modifier to be applied to the text field
 * @param label Optional label text to display above the text field
 * @param placeholder Optional placeholder text when the field is empty
 * @param leadingIcon Optional icon to display at the start of the text field
 * @param error Optional error message to display below the text field
 * @param isPassword Whether this is a password field (with toggleable visibility)
 * @param isRequired Whether this field is required (shows asterisk next to label)
 * @param enabled Whether the text field is enabled
 * @param readOnly Whether the text field is read-only
 * @param keyboardOptions Keyboard options for this text field
 * @param keyboardActions Keyboard actions for this text field
 * @param maxLines Maximum number of lines for this text field
 * @param singleLine Whether this text field is single line
 */
@Composable
fun KardioTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    helperText: String? = null,
    leadingIcon: ImageVector? = null,
    error: String? = null,
    isPassword: Boolean = false,
    isRequired: Boolean = false,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    maxLines: Int = Int.MAX_VALUE,
    singleLine: Boolean = false
) {
    // State for password visibility toggle
    var passwordVisible by remember { mutableStateOf(false) }

    /**
     * Text field component variant that uses TextFieldValue instead of String.
     * This allows more control over cursor position and selection.
     */
    @Composable
    fun KardioTextField(
        value: TextFieldValue,
        onValueChange: (TextFieldValue) -> Unit,
        modifier: Modifier = Modifier,
        label: String? = null,
        placeholder: String? = null,
        helperText: String? = null,
        leadingIcon: ImageVector? = null,
        error: String? = null,
        isPassword: Boolean = false,
        isRequired: Boolean = false,
        enabled: Boolean = true,
        readOnly: Boolean = false,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
        maxLines: Int = Int.MAX_VALUE,
        singleLine: Boolean = false
    ) {
        // State for password visibility toggle
        var passwordVisible by remember { mutableStateOf(false) }

        // Focus state
        val focusRequester = remember { FocusRequester() }
        var isFocused by remember { mutableStateOf(false) }

        // Determine visual transformation based on password field and visibility
        val visualTransformation = when {
            isPassword && !passwordVisible -> PasswordVisualTransformation()
            else -> VisualTransformation.None
        }

        // Keyboard options for password field
        val passwordKeyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = keyboardOptions.imeAction
        )

        Column(modifier = modifier) {
            // Label and Required indicator
            if (label != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary
                    )

                    if (isRequired) {
                        Text(
                            text = " *",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Error
                        )
                    }
                }
            }

            // Text field container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(BackgroundInput)
                    .border(
                        width = 1.dp,
                        color = when {
                            error != null -> Error
                            isFocused -> TextPrimary
                            else -> BackgroundInput
                        },
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                // Leading icon if provided
                if (leadingIcon != null) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterStart),
                        tint = if (error != null) Error else TextSecondary
                    )
                }

                // Text field
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = if (leadingIcon != null) 32.dp else 0.dp,
                            end = if (isPassword) 40.dp else 0.dp
                        )
                        .focusRequester(focusRequester)
                        .onFocusChanged { isFocused = it.isFocused },
                    enabled = enabled && !readOnly,
                    readOnly = readOnly,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = TextPrimary
                    ),
                    keyboardOptions = if (isPassword) passwordKeyboardOptions else keyboardOptions,
                    keyboardActions = keyboardActions,
                    singleLine = singleLine,
                    maxLines = maxLines,
                    visualTransformation = visualTransformation,
                    interactionSource = remember { MutableInteractionSource() },
                    cursorBrush = SolidColor(TextPrimary),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            // Placeholder text
                            if (value.text.isEmpty() && placeholder != null) {
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

                // Password visibility toggle
                if (isPassword) {
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = TextSecondary
                        )
                    }
                }
            }

            // Error or helper text
            when {
                error != null -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = null,
                            tint = Error,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodySmall,
                            color = Error,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }

                helperText != null -> {
                    Text(
                        text = helperText,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun KardioTextFieldPreview() {
        KardioTheme {
            KardioTextField(
                value = "",
                onValueChange = {},
                label = "Username",
                placeholder = "Enter your username",
                isRequired = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun KardioTextFieldWithErrorPreview() {
        KardioTheme {
            KardioTextField(
                value = "invalid-email",
                onValueChange = {},
                label = "Email",
                leadingIcon = Icons.Default.Error,
                error = "Invalid email format",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun KardioPasswordFieldPreview() {
        KardioTheme {
            KardioTextField(
                value = "password123",
                onValueChange = {},
                label = "Password",
                isPassword = true,
                isRequired = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }

    // Focus state
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    // Determine visual transformation based on password field and visibility
    val visualTransformation = when {
        isPassword && !passwordVisible -> PasswordVisualTransformation()
        else -> VisualTransformation.None
    }

    // Keyboard options for password field
    val passwordKeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Password,
        imeAction = keyboardOptions.imeAction
    )

    Column(modifier = modifier) {
        // Label and Required indicator
        if (label != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )

                if (isRequired) {
                    Text(
                        text = " *",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Error
                    )
                }
            }
        }

        // Text field container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(BackgroundInput)
                .border(
                    width = 1.dp,
                    color = when {
                        error != null -> Error
                        isFocused -> TextPrimary
                        else -> BackgroundInput
                    },
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            // Leading icon if provided
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterStart),
                    tint = if (error != null) Error else TextSecondary
                )
            }

            // Text field
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = if (leadingIcon != null) 32.dp else 0.dp,
                        end = if (isPassword) 40.dp else 0.dp
                    )
                    .focusRequester(focusRequester)
                    .onFocusChanged { isFocused = it.isFocused },
                enabled = enabled && !readOnly,
                readOnly = readOnly,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = TextPrimary
                ),
                keyboardOptions = if (isPassword) passwordKeyboardOptions else keyboardOptions,
                keyboardActions = keyboardActions,
                singleLine = singleLine,
                maxLines = maxLines,
                visualTransformation = visualTransformation,
                interactionSource = remember { MutableInteractionSource() },
                cursorBrush = SolidColor(TextPrimary),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        // Placeholder text
                        if (value.isEmpty() && placeholder != null) {
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

            // Password visibility toggle
            if (isPassword) {
                IconButton(
                    onClick = { passwordVisible = !passwordVisible },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        tint = TextSecondary
                    )
                }
            }
        }

        // Error or helper text
        when {
            error != null -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = null,
                        tint = Error,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = error,
                        style = MaterialTheme.typography.bodySmall,
                        color = Error,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            helperText != null -> {
                Text(
                    text = helperText,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}