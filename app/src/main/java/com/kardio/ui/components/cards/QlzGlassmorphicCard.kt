// File: app/src/main/java/com/kardio/ui/components/cards/QlzGlassmorphicCard.kt
package com.kardio.ui.components.cards

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.kardio.R
import androidx.core.graphics.toColorInt

/**
 * QlzGlassmorphicCard - Card với hiệu ứng glassmorphism cho Dark Mode.
 * Tạo hiệu ứng mờ, trong suốt và viền sáng theo style Modern Dark Mode.
 */
class QlzGlassmorphicCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    private val rectF = RectF()
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }
    private val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        maskFilter = BlurMaskFilter(15f, BlurMaskFilter.Blur.NORMAL)
    }

    private var borderColor = Color.WHITE
    private var borderWidth = 0.5f
    private var borderAlpha = 0.2f
    private var cornerRadius = 0f
    private var glowColor = Color.WHITE
    private var glowAlpha = 0.15f
    private var glowEnabled = true

    init {
        // Default setup
        setDefaultValues()
        setupFromAttributes(attrs)

        // Card setup for glassmorphic effect
        cardElevation = 0f // No elevation for glassmorphic cards
        setCardBackgroundColor("#20FFFFFF".toColorInt()) // Semi-transparent white
    }

    private fun setDefaultValues() {
        // Set default values from dimens and colors
        cornerRadius = resources.getDimension(R.dimen.radius_md)
        borderWidth = resources.getDimension(R.dimen.glassmorphism_border_width)
        borderColor = ContextCompat.getColor(context, R.color.text_primary)
        glowColor = ContextCompat.getColor(context, R.color.text_primary)

        // Update paints
        updatePaints()
    }

    private fun setupFromAttributes(attrs: AttributeSet?) {
        if (attrs == null) return

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.QlzGlassmorphicCard)
        try {
            borderColor = typedArray.getColor(
                R.styleable.QlzGlassmorphicCard_qlzBorderColor,
                borderColor
            )
            borderWidth = typedArray.getDimension(
                R.styleable.QlzGlassmorphicCard_qlzBorderWidth,
                borderWidth
            )
            borderAlpha = typedArray.getFloat(
                R.styleable.QlzGlassmorphicCard_qlzBorderAlpha,
                borderAlpha
            )

            glowColor = typedArray.getColor(
                R.styleable.QlzGlassmorphicCard_qlzGlowColor,
                glowColor
            )
            glowAlpha = typedArray.getFloat(
                R.styleable.QlzGlassmorphicCard_qlzGlowAlpha,
                glowAlpha
            )
            glowEnabled = typedArray.getBoolean(
                R.styleable.QlzGlassmorphicCard_qlzGlowEnabled,
                glowEnabled
            )

            val backgroundColor = typedArray.getColor(
                R.styleable.QlzGlassmorphicCard_qlzBackgroundColor,
                "#20FFFFFF".toColorInt()
            )
            setCardBackgroundColor(backgroundColor)

            cornerRadius = typedArray.getDimension(
                R.styleable.QlzGlassmorphicCard_cardCornerRadius,
                cornerRadius
            )
            radius = cornerRadius
        } finally {
            typedArray.recycle()
        }

        // Update paints with new values
        updatePaints()
    }

    private fun updatePaints() {
        // Border paint
        borderPaint.color = borderColor
        borderPaint.strokeWidth = borderWidth
        borderPaint.alpha = (borderAlpha * 255).toInt()

        // Glow paint
        glowPaint.color = glowColor
        glowPaint.strokeWidth = borderWidth * 2
        glowPaint.alpha = (glowAlpha * 255).toInt()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Calculate border bounds
        val halfBorder = borderWidth / 2
        rectF.set(
            halfBorder,
            halfBorder,
            width - halfBorder,
            height - halfBorder
        )

        // Draw border
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, borderPaint)

        // Draw glow if enabled
        if (glowEnabled) {
            canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, glowPaint)
        }
    }

    // Public methods to customize appearance
    fun setBorderColor(color: Int) {
        borderColor = color
        updatePaints()
        invalidate()
    }

    fun setBorderWidth(width: Float) {
        borderWidth = width
        updatePaints()
        invalidate()
    }

    fun setBorderAlpha(alpha: Float) {
        borderAlpha = alpha.coerceIn(0f, 1f)
        updatePaints()
        invalidate()
    }

    fun setGlowColor(color: Int) {
        glowColor = color
        updatePaints()
        invalidate()
    }

    fun setGlowAlpha(alpha: Float) {
        glowAlpha = alpha.coerceIn(0f, 1f)
        updatePaints()
        invalidate()
    }

    fun setGlowEnabled(enabled: Boolean) {
        glowEnabled = enabled
        invalidate()
    }
}
