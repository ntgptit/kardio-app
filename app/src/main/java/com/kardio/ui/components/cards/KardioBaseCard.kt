package com.kardio.ui.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kardio.ui.theme.BackgroundSecondary
import com.kardio.ui.theme.Divider

/**
 * Card cơ bản cho ứng dụng - chuyển từ component QlzBaseCard
 *
 * @param modifier Modifier cho card
 * @param onClick Callback khi card được nhấn
 * @param shape Hình dạng của card
 * @param backgroundColor Màu nền card
 * @param contentPadding Padding cho nội dung bên trong
 * @param elevation Độ nổi của card
 * @param border Viền của card
 * @param content Nội dung bên trong card
 */
@Composable
fun KardioBaseCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(16.dp),
    backgroundColor: Color = BackgroundSecondary,
    contentPadding: Dp = 0.dp,
    elevation: Dp = 0.dp,
    border: BorderStroke? = BorderStroke(1.dp, Divider),
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = rememberRipple(bounded = true),
                        onClick = onClick
                    )
                } else Modifier
            ),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = Color.Unspecified
        ),
        border = border,
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation
        )
    ) {
        Box(
            modifier = Modifier.padding(contentPadding)
        ) {
            content()
        }
    }
}

/**
 * Card kiểu Neumorphic - một biến thể của card cơ bản với hiệu ứng nổi
 */
@Composable
fun KardioNeumorphicCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(16.dp),
    backgroundColor: Color = BackgroundSecondary,
    contentPadding: Dp = 0.dp,
    hasStroke: Boolean = true,
    strokeColor: Color = Divider,
    content: @Composable () -> Unit
) {
    KardioBaseCard(
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        backgroundColor = backgroundColor,
        contentPadding = contentPadding,
        elevation = 4.dp,
        border = if (hasStroke) BorderStroke(1.dp, strokeColor) else null,
        content = content
    )
}

/**
 * Card kiểu Glassmorphic với hiệu ứng như kính mờ
 */
@Composable
fun KardioGlassmorphicCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(16.dp),
    backgroundColor: Color = BackgroundSecondary.copy(alpha = 0.7f),
    contentPadding: Dp = 0.dp,
    glowEnabled: Boolean = false,
    borderColor: Color = Divider.copy(alpha = 0.5f),
    content: @Composable () -> Unit
) {
    KardioBaseCard(
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        backgroundColor = backgroundColor,
        contentPadding = contentPadding,
        elevation = if (glowEnabled) 8.dp else 0.dp,
        border = BorderStroke(1.dp, borderColor),
        content = content
    )
}