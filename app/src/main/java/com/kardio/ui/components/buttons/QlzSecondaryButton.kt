// File: app/src/main/java/com/kardio/ui/components/buttons/QlzSecondaryButton.kt
package com.kardio.ui.components.buttons

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.kardio.R
import com.kardio.utils.helpers.AnimationUtils

/**
 * QlzSecondaryButton - Secondary button component với border style
 * Sử dụng màu secondary_button_background (#1A1A2E) và secondary_button_stroke (#FFFFFF)
 */
class QlzSecondaryButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    private var isAnimationEnabled = true
    private var isHapticEnabled = true

    init {
        applyStyle()
        setupClickAnimation()

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.QlzSecondaryButton)
        try {
            isAnimationEnabled = typedArray.getBoolean(
                R.styleable.QlzSecondaryButton_qlzAnimationEnabled,
                true
            )
            isHapticEnabled = typedArray.getBoolean(
                R.styleable.QlzSecondaryButton_qlzHapticFeedback,
                true
            )
        } finally {
            typedArray.recycle()
        }
    }

    private fun applyStyle() {
        // Thiết lập style mặc định từ theme
        background = ContextCompat.getDrawable(context, R.drawable.bg_button_secondary)
        setTextColor(ContextCompat.getColor(context, R.color.text_primary))
        isAllCaps = false

        // Padding và kích thước
        val horizontalPadding = resources.getDimensionPixelSize(R.dimen.button_padding_horizontal)
        val verticalPadding = resources.getDimensionPixelSize(R.dimen.button_padding_vertical)
        setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)

        // Typography
        setTextAppearance(context, R.style.TextAppearance_Kardio_Button)

        // Không có elevation cho secondary button
        elevation = 0f
    }

    private fun setupClickAnimation() {
        setOnTouchListener { v, event ->
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

    override fun setHapticFeedbackEnabled(enabled: Boolean) {
        isHapticEnabled = enabled
    }
}
