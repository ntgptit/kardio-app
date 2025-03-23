package com.kardio.ui.components.inputs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kardio.ui.theme.BackgroundInput
import com.kardio.ui.theme.Error
import com.kardio.ui.theme.TextPrimary
import com.kardio.ui.theme.TextSecondary
import com.kardio.ui.theme.TextTertiary

/**
 * TextField tùy chỉnh - chuyển từ component_text_field.xml
 *
 * @param value Giá trị hiện tại
 * @param onValueChange Callback khi giá trị thay đổi
 * @param modifier Modifier cho OutlinedTextField
 * @param label Nhãn trên ô nhập
 * @param placeholder Gợi ý khi trống
 * @param leadingIcon Icon trước ô nhập
 * @param isError Có hiển thị lỗi không
 * @param errorMessage Nội dung lỗi
 * @param isRequired Field bắt buộc hay không
 * @param isPassword Có phải ô nhập mật khẩu không
 * @param keyboardType Kiểu bàn phím
 * @param imeAction Hành động trên bàn phím
 * @param keyboardActions Các hành động bàn phím
 * @param onFocusChange Callback khi focus thay đổi
 * @param readOnly Field có chỉ đọc không
 * @param maxLines Số dòng tối đa
 * @param singleLine Chỉ một dòng
 */
@Composable
fun KardioTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: ImageVector? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    isRequired: Boolean = false,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onFocusChange: (FocusState) -> Unit = {},
    readOnly: Boolean = false,
    maxLines: Int = 1,
    singleLine: Boolean = true
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Label và dấu * (required)
        if (label != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    color = TextPrimary
                )

                if (isRequired) {
                    Text(
                        text = " *",
                        style = MaterialTheme.typography.labelLarge,
                        color = Error
                    )
                }
            }
        }

        // Input field
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    isFocused = it.isFocused
                    onFocusChange(it)
                },
            placeholder = if (placeholder != null) {
                { Text(text = placeholder, color = TextTertiary) }
            } else null,
            leadingIcon = if (leadingIcon != null) {
                { Icon(imageVector = leadingIcon, contentDescription = null, tint = TextSecondary) }
            } else null,
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = TextSecondary
                        )
                    }
                }
            } else null,
            isError = isError,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            readOnly = readOnly,
            visualTransformation = if (isPassword && !passwordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = BackgroundInput,
                unfocusedContainerColor = BackgroundInput,
                disabledContainerColor = BackgroundInput.copy(alpha = 0.7f),
                errorContainerColor = BackgroundInput,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                disabledTextColor = TextPrimary.copy(alpha = 0.7f),
                cursorColor = TextPrimary,
                focusedIndicatorColor = if (isError) Error else TextSecondary,
                unfocusedIndicatorColor = if (isError) Error.copy(alpha = 0.7f) else TextTertiary
            ),
            shape = RoundedCornerShape(8.dp)
        )

        // Error message
        if (isError && !errorMessage.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = errorMessage,
                color = Error,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}