// File: app/src/main/java/com/kardio/ui/components/buttons/QlzPrimaryButton.kt
package com.kardio.ui.components.buttons

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.kardio.R
import com.kardio.utils.helpers.AnimationUtils

/**
 * QlzPrimaryButton - Primary button component theo thiết kế Dark Mode
 * Sử dụng màu primary_button (#4255FF) từ theme
 */
class QlzPrimaryButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    private var isAnimationEnabled = true
    private var isHapticEnabled = true

    init {
        // Setup mặc định
        applyStyle()
        setupClickAnimation()

        // Parse custom attributes nếu có
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.QlzPrimaryButton)
        try {
            isAnimationEnabled = typedArray.getBoolean(
                R.styleable.QlzPrimaryButton_qlzAnimationEnabled,
                true
            )
            isHapticEnabled = typedArray.getBoolean(
                R.styleable.QlzPrimaryButton_qlzHapticFeedback,
                true
            )
        } finally {
            typedArray.recycle()
        }
    }

    private fun applyStyle() {
        // Thiết lập style mặc định từ theme
        background = ContextCompat.getDrawable(context, R.drawable.bg_button_primary)
        setTextColor(ContextCompat.getColor(context, R.color.text_primary))
        isAllCaps = false

        // Padding và kích thước
        val horizontalPadding = resources.getDimensionPixelSize(R.dimen.button_padding_horizontal)
        val verticalPadding = resources.getDimensionPixelSize(R.dimen.button_padding_vertical)
        setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)

        // Typography
        setTextAppearance(context, R.style.TextAppearance_Kardio_Button)

        // Elevation nhẹ cho dark mode
        elevation = resources.getDimension(R.dimen.elevation_xs)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupClickAnimation() {
        // Sử dụng press state change để trigger animation
        setOnTouchListener { _, event ->
            if (isAnimationEnabled) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        AnimationUtils.applyButtonClickAnimation(this, context)
                    }
                }
            }
            // Không consume event để onClick vẫn hoạt động
            false
        }
    }

    // Phương thức cho phép bật/tắt animation
    fun setAnimationEnabled(enabled: Boolean) {
        isAnimationEnabled = enabled
    }

    // Phương thức cho phép bật/tắt haptic feedback
    override fun setHapticFeedbackEnabled(enabled: Boolean) {
        isHapticEnabled = enabled
    }
}