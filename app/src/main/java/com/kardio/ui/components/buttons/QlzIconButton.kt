// File: app/src/main/java/com/kardio/ui/components/buttons/QlzIconButton.kt
package com.kardio.ui.components.buttons

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import com.kardio.R
import com.kardio.utils.helpers.AnimationUtils

/**
 * QlzIconButton - Button chỉ có icon, không có text
 * Sử dụng màu icon_primary (#FFFFFF) cho icon tint
 */
class QlzIconButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0  // Sửa thành 0 thay vì R.attr.imageButtonStyle
) : AppCompatImageButton(context, attrs, defStyleAttr) {

    private var isAnimationEnabled = true
    private var isGlowEnabled = true
    private var isHapticEnabled = true

    init {
        applyStyle()
        setupClickAnimation()

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.QlzIconButton)
        try {
            isAnimationEnabled = typedArray.getBoolean(
                R.styleable.QlzIconButton_qlzAnimationEnabled,
                true
            )
            isGlowEnabled = typedArray.getBoolean(
                R.styleable.QlzIconButton_qlzGlowEnabled,
                true
            )
            isHapticEnabled = typedArray.getBoolean(
                R.styleable.QlzIconButton_qlzHapticFeedback,
                true
            )
        } finally {
            typedArray.recycle()
        }
    }

    private fun applyStyle() {
        // Thiết lập style mặc định từ theme
        background = ContextCompat.getDrawable(context, R.drawable.bg_button_icon)

        // Kích thước tối thiểu cho accessibility
        minimumWidth = resources.getDimensionPixelSize(R.dimen.min_touch_target_size)
        minimumHeight = resources.getDimensionPixelSize(R.dimen.min_touch_target_size)

        // Đảm bảo tint icon đúng màu
        imageTintList = ContextCompat.getColorStateList(context, R.color.icon_primary)

        // Không có elevation cho icon button
        elevation = 0f
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupClickAnimation() {
        setOnTouchListener { _, event ->
            if (isAnimationEnabled) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        AnimationUtils.applyButtonClickAnimation(this, context)
                    }
                }
            }
            false
        }
    }

    fun setAnimationEnabled(enabled: Boolean) {
        isAnimationEnabled = enabled
    }

    fun setGlowEnabled(enabled: Boolean) {
        isGlowEnabled = enabled
        // TODO: Implement glow effect
    }

    override fun setHapticFeedbackEnabled(enabled: Boolean) {
        isHapticEnabled = enabled
    }
}