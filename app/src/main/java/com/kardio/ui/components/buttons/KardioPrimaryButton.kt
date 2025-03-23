package com.kardio.ui.components.buttons

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kardio.ui.theme.PrimaryButton
import com.kardio.ui.theme.TextPrimary

/**
 * Nút chính của ứng dụng - chuyển từ component_primary_button.xml
 *
 * @param text Text hiển thị trên nút
 * @param onClick Callback khi nút được nhấn
 * @param modifier Modifier cho button
 * @param isLoading Hiển thị trạng thái loading
 * @param icon Icon hiển thị trước text (tùy chọn)
 * @param backgroundColor Màu nền nút (mặc định là PrimaryButton)
 * @param contentColor Màu nội dung nút (mặc định là TextPrimary)
 * @param enabled Trạng thái kích hoạt của nút
 * @param isFullWidth Nút có chiếm toàn bộ chiều rộng không
 */
@Composable
fun KardioPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    icon: ImageVector? = null,
    backgroundColor: Color = PrimaryButton,
    contentColor: Color = TextPrimary,
    enabled: Boolean = true,
    isFullWidth: Boolean = true
) {
    val buttonModifier = if (isFullWidth) {
        modifier.fillMaxWidth()
    } else {
        modifier
    }

    Button(
        onClick = { if (!isLoading) onClick() },
        modifier = buttonModifier.height(48.dp),
        enabled = enabled && !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
            disabledContainerColor = backgroundColor.copy(alpha = 0.6f),
            disabledContentColor = contentColor.copy(alpha = 0.6f)
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = contentColor,
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
                textAlign = TextAlign.Center
            )
        }
    }
}