// File: app/src/main/java/com/kardio/ui/components/buttons/QlzFloatingActionButton.kt
package com.kardio.ui.components.buttons

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.core.content.ContextCompat
import com.kardio.R
import com.kardio.utils.helpers.AnimationUtils

/**
 * QlzFloatingActionButton - FAB tối ưu cho Dark Mode
 * Sử dụng màu primary_button (#4255FF) và icon_primary (#FFFFFF)
 */
class QlzFloatingActionButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.floatingActionButtonStyle
) : FloatingActionButton(context, attrs, defStyleAttr) {

    private var isAnimationEnabled = true
    private var isGlowEnabled = true
    private var isHapticEnabled = true

    init {
        applyStyle()
        setupClickAnimation()

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.QlzFloatingActionButton)
        try {
            isAnimationEnabled = typedArray.getBoolean(
                R.styleable.QlzFloatingActionButton_qlzAnimationEnabled,
                true
            )
            isGlowEnabled = typedArray.getBoolean(
                R.styleable.QlzFloatingActionButton_qlzGlowEnabled,
                true
            )
            isHapticEnabled = typedArray.getBoolean(
                R.styleable.QlzFloatingActionButton_qlzHapticFeedback,
                true
            )
        } finally {
            typedArray.recycle()
        }
    }

    private fun applyStyle() {
        // Thiết lập style mặc định
        backgroundTintList = ContextCompat.getColorStateList(context, R.color.primary_button)
        imageTintList = ContextCompat.getColorStateList(context, R.color.icon_primary)

        // Custom elevation cho dark mode (giảm độ nổi bật)
        elevation = resources.getDimension(R.dimen.elevation_sm)

        // Đảm bảo size phù hợp với guidelines
        customSize = resources.getDimensionPixelSize(R.dimen.fab_size_normal)
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