package com.kardio.ui.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kardio.ui.theme.SecondaryButtonBackground
import com.kardio.ui.theme.SecondaryButtonStroke
import com.kardio.ui.theme.TextPrimary

/**
 * Nút thứ cấp của ứng dụng với viền outline
 *
 * @param text Text hiển thị trên nút
 * @param onClick Callback khi nút được nhấn
 * @param modifier Modifier cho button
 * @param icon Icon hiển thị trước text (tùy chọn)
 * @param borderColor Màu viền nút
 * @param contentColor Màu nội dung nút
 * @param backgroundColor Màu nền nút
 * @param enabled Trạng thái kích hoạt của nút
 * @param isFullWidth Nút có chiếm toàn bộ chiều rộng không
 */
@Composable
fun KardioSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    borderColor: Color = SecondaryButtonStroke,
    contentColor: Color = TextPrimary,
    backgroundColor: Color = SecondaryButtonBackground,
    enabled: Boolean = true,
    isFullWidth: Boolean = true
) {
    val buttonModifier = if (isFullWidth) {
        modifier.fillMaxWidth()
    } else {
        modifier
    }

    OutlinedButton(
        onClick = onClick,
        modifier = buttonModifier.height(48.dp),
        enabled = enabled,
        border = BorderStroke(1.dp, borderColor),
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = contentColor
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                    color = contentColor,
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}