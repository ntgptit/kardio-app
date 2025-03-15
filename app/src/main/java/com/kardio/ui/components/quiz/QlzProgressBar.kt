// File: app/src/main/java/com/kardio/ui/components/quiz/QlzProgressBar.kt
package com.kardio.ui.components.quiz

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.kardio.R

/**
 * QlzProgressBar - Progress bar hiển thị tiến độ học tập
 * Hỗ trợ hiển thị phần trăm hoàn thành và màu sắc tùy chỉnh
 */
class QlzProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectF = RectF()

    private var progress = 0f
    private var maxProgress = 100f
    private var cornerRadius = 0f
    private var progressColor = 0
    private var backgroundColor = 0

    init {
        setupFromAttributes(attrs)
        initPaints()
    }

    private fun setupFromAttributes(attrs: AttributeSet?) {
        if (attrs == null) return

        val typedArray = context.obtainStyledAttributes(
            attrs, R.styleable.QlzProgressBar
        )

        try {
            progress = typedArray.getFloat(
                R.styleable.QlzProgressBar_qlzProgress, 0f
            )
            maxProgress = typedArray.getFloat(
                R.styleable.QlzProgressBar_qlzMaxProgress, 100f
            )
            cornerRadius = typedArray.getDimension(
                R.styleable.QlzProgressBar_qlzCornerRadius,
                resources.getDimension(R.dimen.radius_xs)
            )
            progressColor = typedArray.getColor(
                R.styleable.QlzProgressBar_qlzProgressColor,
                ContextCompat.getColor(context, R.color.accent)
            )
            backgroundColor = typedArray.getColor(
                R.styleable.QlzProgressBar_qlzBackgroundColor,
                ContextCompat.getColor(context, R.color.background_secondary)
            )
        } finally {
            typedArray.recycle()
        }
    }

    private fun initPaints() {
        backgroundPaint.color = backgroundColor
        backgroundPaint.style = Paint.Style.FILL

        progressPaint.color = progressColor
        progressPaint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()

        // Draw background
        rectF.set(0f, 0f, width, height)
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, backgroundPaint)

        // Draw progress
        val progressWidth = width * (progress / maxProgress)
        rectF.set(0f, 0f, progressWidth, height)
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, progressPaint)
    }

    /**
     * Set the progress value
     */
    fun setProgress(progress: Float) {
        this.progress = progress.coerceIn(0f, maxProgress)
        invalidate()
    }

    /**
     * Get the current progress value
     */
    fun getProgress(): Float {
        return progress
    }

    /**
     * Set the maximum progress value
     */
    fun setMaxProgress(maxProgress: Float) {
        this.maxProgress = maxProgress
        invalidate()
    }

    /**
     * Get the maximum progress value
     */
    fun getMaxProgress(): Float {
        return maxProgress
    }

    /**
     * Set the progress color
     */
    fun setProgressColor(color: Int) {
        progressColor = color
        progressPaint.color = color
        invalidate()
    }

    /**
     * Set the background color
     */
    override fun setBackgroundColor(color: Int) {
        backgroundColor = color
        backgroundPaint.color = color
        invalidate()
    }

    /**
     * Set the corner radius
     */
    fun setCornerRadius(radius: Float) {
        cornerRadius = radius
        invalidate()
    }
}
