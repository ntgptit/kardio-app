// File: app/src/main/java/com/kardio/ui/components/header/QlzAppBar.kt
package com.kardio.ui.components.header

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.kardio.R

class QlzAppBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : Toolbar(context, attrs, defStyleAttr) {

    init {
        setupAppBar()
    }

    private fun setupAppBar() {
        // Sử dụng màu nền đơn giản, giống với bottom bar
        setBackgroundColor(ContextCompat.getColor(context, R.color.background_primary))

        // Elevation bằng 0 để tránh hiệu ứng bóng đổ
        elevation = 0f

        // Loại bỏ các content insets để ConstraintLayout có thể mở rộng toàn màn hình
        setContentInsetsAbsolute(0, 0)
        setContentInsetsRelative(0, 0)

        // Chỉ giữ padding dọc, bỏ padding ngang
        val verticalPadding = resources.getDimensionPixelSize(R.dimen.spacing_sm)
        setPadding(0, verticalPadding, 0, verticalPadding)

        // Thiết lập chiều cao mặc định
        minimumHeight = resources.getDimensionPixelSize(R.dimen.toolbar_height)
    }
}