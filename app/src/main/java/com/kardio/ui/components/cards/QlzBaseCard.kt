// File: app/src/main/java/com/kardio/ui/components/cards/QlzBaseCard.kt
package com.kardio.ui.components.cards

import android.content.Context
import android.util.AttributeSet
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.kardio.R

/**
 * QlzBaseCard - Base card component cho Dark Mode.
 * Sử dụng màu background_secondary (#1E2642) từ colors.xml
 */
open class QlzBaseCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    init {
        applyDefaultStyle()

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.QlzBaseCard)
        try {
            // Custom attributes nếu cần
        } finally {
            typedArray.recycle()
        }
    }

    protected fun applyDefaultStyle() {
        // Background color phù hợp với dark mode
        setCardBackgroundColor(ContextCompat.getColor(context, R.color.background_secondary))

        // Border radius theo design system
        radius = resources.getDimension(R.dimen.radius_md)

        // Giảm elevation cho dark mode
        cardElevation = resources.getDimension(R.dimen.elevation_xs)

        // Padding tiêu chuẩn
        val cardPadding = resources.getDimensionPixelSize(R.dimen.card_content_padding)
        setContentPadding(cardPadding, cardPadding, cardPadding, cardPadding)

        // Ripple effect khi click
        isClickable = true
        isFocusable = true
        foreground = ContextCompat.getDrawable(context, R.drawable.ripple_card)
    }
}