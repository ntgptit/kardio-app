package com.kardio.ui.components.feedback

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kardio.ui.theme.Accent
import com.kardio.ui.theme.TextSecondary

/**
 * Component loading indicator - chuyển từ component_loading_indicator.xml
 *
 * @param isLoading Trạng thái loading
 * @param message Thông báo hiển thị khi loading
 * @param modifier Modifier cho container
 * @param color Màu của CircularProgressIndicator
 */
@Composable
fun KardioLoadingIndicator(
    isLoading: Boolean,
    message: String? = null,
    modifier: Modifier = Modifier,
    color: Color = Accent
) {
    if (isLoading) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = color,
                    strokeWidth = 4.dp
                )

                if (!message.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

/**
 * Dialog loading indicator cho các tác vụ toàn màn hình
 */
@Composable
fun KardioLoadingDialog(
    isLoading: Boolean,
    message: String? = null,
    onDismiss: () -> Unit = {}
) {
    if (isLoading) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
            ) {
                KardioLoadingIndicator(
                    isLoading = true,
                    message = message,
                    modifier = Modifier.padding(24.dp)
                )
            }
        }
    }
}

/**
 * Hiển thị loading trên toàn màn hình
 */
@Composable
fun KardioFullScreenLoading(
    isLoading: Boolean,
    message: String? = null
) {
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            KardioLoadingIndicator(
                isLoading = true,
                message = message
            )
        }
    }
}