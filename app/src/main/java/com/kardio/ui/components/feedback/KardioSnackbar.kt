package com.kardio.ui.components.feedback

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kardio.ui.theme.BackgroundSecondary
import com.kardio.ui.theme.Error
import com.kardio.ui.theme.Success
import com.kardio.ui.theme.TextPrimary
import com.kardio.ui.theme.Warning

/**
 * Kiểu của Snackbar
 */
enum class SnackbarType {
    SUCCESS, ERROR, INFO, WARNING
}

/**
 * Component hiển thị Snackbar - chuyển từ component_snackbar.xml
 *
 * @param message Nội dung thông báo
 * @param visible Trạng thái hiển thị
 * @param type Kiểu snackbar
 * @param duration Thời gian hiển thị (ms)
 * @param onDismiss Callback khi snackbar biến mất
 */
@Composable
fun KardioSnackbar(
    message: String,
    visible: Boolean,
    type: SnackbarType = SnackbarType.INFO,
    duration: Long = 3000,
    onDismiss: () -> Unit
) {
    // Xác định icon và màu theo loại
    val (icon, color) = when (type) {
        SnackbarType.SUCCESS -> Icons.Default.Check to Success
        SnackbarType.ERROR -> Icons.Default.Warning to Error
        SnackbarType.WARNING -> Icons.Default.Warning to Warning
        SnackbarType.INFO -> Icons.Default.Info to MaterialTheme.colorScheme.primary
    }

    // Animation
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(200)) + slideInVertically(tween(300)) { it },
        exit = fadeOut(tween(200)) + slideOutVertically(tween(300)) { it }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(BackgroundSecondary)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )
            }
        }
    }

    // Tự động dismiss
    LaunchedEffect(visible) {
        if (visible) {
            kotlinx.coroutines.delay(duration)
            onDismiss()
        }
    }
}

/**
 * Tiện ích để hiển thị Snackbar từ bất kỳ đâu
 */
object QlzSnackbar {
    fun showSuccess(context: Context, message: String) {
        // Implement với API Toast hoặc cơ chế hiển thị khác
    }

    fun showError(context: Context, message: String) {
        // Implement với API Toast hoặc cơ chế hiển thị khác
    }

    fun showInfo(context: Context, message: String) {
        // Implement với API Toast hoặc cơ chế hiển thị khác
    }

    fun showWarning(context: Context, message: String) {
        // Implement với API Toast hoặc cơ chế hiển thị khác
    }
}

/**
 * SnackbarHost tùy chỉnh để sử dụng trong Scaffold
 */
@Composable
fun KardioSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier.padding(16.dp),
        snackbar = { data ->
            Snackbar(
                modifier = Modifier.padding(0.dp),
                shape = RoundedCornerShape(8.dp),
                containerColor = BackgroundSecondary,
                contentColor = TextPrimary,
                content = {
                    Text(
                        text = data.visuals.message,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            )
        }
    )
}