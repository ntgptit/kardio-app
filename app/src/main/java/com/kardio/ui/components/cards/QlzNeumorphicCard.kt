// File: app/src/main/java/com/kardio/ui/components/cards/QlzNeumorphicCard.kt
package com.kardio.ui.components.cards

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.kardio.R

/**
 * QlzNeumorphicCard - Card với style neumorphic cho Dark Mode.
 * Sử dụng màu background_primary (#0F1429) và tạo hiệu ứng neumorphic
 * với bóng đổ nhẹ, phù hợp với dark mode.
 */
class QlzNeumorphicCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    // Shape type constants
    companion object {
        const val SHAPE_TYPE_FLAT = 0
        const val SHAPE_TYPE_PRESSED = 1
        const val SHAPE_TYPE_BASIN = 2
    }

    // Default values
    private val defaultBackgroundColor = ContextCompat.getColor(context, R.color.background_primary)
    private val defaultLightShadowColor = Color.parseColor("#2F2F2F") // Lighter than background
    private val defaultDarkShadowColor = Color.parseColor("#080C1A") // Darker than background
    private val defaultCornerRadius = resources.getDimension(R.dimen.radius_md)
    private val defaultShadowElevation = resources.getDimension(R.dimen.elevation_md)
    private val defaultStrokeWidth = 0f
    private val defaultStrokeColor = Color.TRANSPARENT
    private val defaultShapeType = SHAPE_TYPE_FLAT

    // Properties
    private var neumorphBackgroundColor: Int = defaultBackgroundColor
    private var neumorphLightShadowColor: Int = defaultLightShadowColor
    private var neumorphDarkShadowColor: Int = defaultDarkShadowColor
    private var neumorphCornerRadius: Float = defaultCornerRadius
    private var neumorphShadowElevation: Float = defaultShadowElevation
    private var neumorphStrokeWidth: Float = defaultStrokeWidth
    private var neumorphStrokeColor: Int = defaultStrokeColor
    private var neumorphShapeType: Int = defaultShapeType

    // Paint objects
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val lightShadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val darkShadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }

    // Path for drawing
    private val backgroundPath = Path()
    private val rectF = RectF()

    init {
        // Initialize from attributes
        obtainAttributes(attrs)

        // Set up the view
        setBackgroundColor(Color.TRANSPARENT)
        cardElevation = 0f
        radius = neumorphCornerRadius

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            outlineSpotShadowColor = Color.TRANSPARENT
            outlineAmbientShadowColor = Color.TRANSPARENT
        }

        updatePaints()

        // Set layer type for shadows
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    private fun obtainAttributes(attrs: AttributeSet?) {
        if (attrs == null) return

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.QlzNeumorphicCard)
        try {
            neumorphShapeType = typedArray.getInt(
                R.styleable.QlzNeumorphicCard_qlzNeumorphicShapeType,
                defaultShapeType
            )

            val hasStroke = typedArray.getBoolean(
                R.styleable.QlzNeumorphicCard_qlzHasStroke,
                false
            )

            if (hasStroke) {
                neumorphStrokeColor = typedArray.getColor(
                    R.styleable.QlzNeumorphicCard_qlzStrokeColor,
                    ContextCompat.getColor(context, R.color.divider)
                )

                neumorphStrokeWidth = typedArray.getDimension(
                    R.styleable.QlzNeumorphicCard_qlzStrokeWidth,
                    resources.getDimension(R.dimen.input_field_stroke_width)
                )
            }
        } finally {
            typedArray.recycle()
        }
    }

    private fun updatePaints() {
        // Background paint
        backgroundPaint.color = neumorphBackgroundColor

        // Shadow paints
        lightShadowPaint.setShadowLayer(
            neumorphShadowElevation,
            -neumorphShadowElevation / 2,
            -neumorphShadowElevation / 2,
            neumorphLightShadowColor
        )

        darkShadowPaint.setShadowLayer(
            neumorphShadowElevation,
            neumorphShadowElevation / 2,
            neumorphShadowElevation / 2,
            neumorphDarkShadowColor
        )

        // Stroke paint
        strokePaint.color = neumorphStrokeColor
        strokePaint.strokeWidth = neumorphStrokeWidth
    }

    override fun onDraw(canvas: Canvas) {
        // Draw the appropriate shape based on type
        when (neumorphShapeType) {
            SHAPE_TYPE_FLAT -> drawFlatShape(canvas)
            SHAPE_TYPE_PRESSED -> drawPressedShape(canvas)
            SHAPE_TYPE_BASIN -> drawBasinShape(canvas)
        }

        // Draw stroke if needed
        if (neumorphStrokeWidth > 0) {
            drawStroke(canvas)
        }

        // Draw children
        super.onDraw(canvas)
    }

    private fun drawFlatShape(canvas: Canvas) {
        // Draw background
        rectF.set(
            neumorphShadowElevation,
            neumorphShadowElevation,
            width.toFloat() - neumorphShadowElevation,
            height.toFloat() - neumorphShadowElevation
        )

        backgroundPath.reset()
        backgroundPath.addRoundRect(rectF, neumorphCornerRadius, neumorphCornerRadius, Path.Direction.CW)

        // Draw light shadow
        canvas.drawPath(backgroundPath, lightShadowPaint)

        // Draw dark shadow
        canvas.drawPath(backgroundPath, darkShadowPaint)

        // Draw background
        canvas.drawPath(backgroundPath, backgroundPaint)
    }

    private fun drawPressedShape(canvas: Canvas) {
        // For pressed effect, we invert the shadows
        rectF.set(
            neumorphShadowElevation,
            neumorphShadowElevation,
            width.toFloat() - neumorphShadowElevation,
            height.toFloat() - neumorphShadowElevation
        )

        backgroundPath.reset()
        backgroundPath.addRoundRect(rectF, neumorphCornerRadius, neumorphCornerRadius, Path.Direction.CW)

        // Draw background
        canvas.drawPath(backgroundPath, backgroundPaint)

        // For pressed, we invert the shadow colors and direction
        val tempPaint = Paint(lightShadowPaint)
        tempPaint.setShadowLayer(
            neumorphShadowElevation,
            neumorphShadowElevation / 2,
            neumorphShadowElevation / 2,
            neumorphLightShadowColor
        )
        canvas.drawPath(backgroundPath, tempPaint)

        tempPaint.setShadowLayer(
            neumorphShadowElevation,
            -neumorphShadowElevation / 2,
            -neumorphShadowElevation / 2,
            neumorphDarkShadowColor
        )
        canvas.drawPath(backgroundPath, tempPaint)
    }

    private fun drawBasinShape(canvas: Canvas) {
        // Basin effect is like a depression in the surface
        rectF.set(
            neumorphShadowElevation,
            neumorphShadowElevation,
            width.toFloat() - neumorphShadowElevation,
            height.toFloat() - neumorphShadowElevation
        )

        backgroundPath.reset()
        backgroundPath.addRoundRect(rectF, neumorphCornerRadius, neumorphCornerRadius, Path.Direction.CW)

        // Draw a darker background for basin
        val basinBackgroundPaint = Paint(backgroundPaint)
        basinBackgroundPaint.color = adjustColorBrightness(neumorphBackgroundColor, -0.1f)
        canvas.drawPath(backgroundPath, basinBackgroundPaint)

        // For basin effect, shadows are inside
        val innerShadowElevation = neumorphShadowElevation * 0.6f

        // Draw inner light shadow (bottom-right)
        val innerLightPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            setShadowLayer(
                innerShadowElevation,
                innerShadowElevation / 2,
                innerShadowElevation / 2,
                neumorphLightShadowColor
            )
            color = Color.TRANSPARENT
        }
        canvas.drawPath(backgroundPath, innerLightPaint)

        // Draw inner dark shadow (top-left)
        val innerDarkPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            setShadowLayer(
                innerShadowElevation,
                -innerShadowElevation / 2,
                -innerShadowElevation / 2,
                neumorphDarkShadowColor
            )
            color = Color.TRANSPARENT
        }
        canvas.drawPath(backgroundPath, innerDarkPaint)
    }

    private fun drawStroke(canvas: Canvas) {
        rectF.set(
            neumorphShadowElevation + neumorphStrokeWidth / 2,
            neumorphShadowElevation + neumorphStrokeWidth / 2,
            width.toFloat() - neumorphShadowElevation - neumorphStrokeWidth / 2,
            height.toFloat() - neumorphShadowElevation - neumorphStrokeWidth / 2
        )

        backgroundPath.reset()
        backgroundPath.addRoundRect(rectF, neumorphCornerRadius, neumorphCornerRadius, Path.Direction.CW)

        canvas.drawPath(backgroundPath, strokePaint)
    }

    /**
     * Adjust the brightness of a color
     * @param color The color to adjust
     * @param factor The factor to adjust by (-1.0 to 1.0)
     * @return The adjusted color
     */
    private fun adjustColorBrightness(color: Int, factor: Float): Int {
        val a = Color.alpha(color)
        val r = Math.min(Math.max(0, (Color.red(color) + 255 * factor).toInt()), 255)
        val g = Math.min(Math.max(0, (Color.green(color) + 255 * factor).toInt()), 255)
        val b = Math.min(Math.max(0, (Color.blue(color) + 255 * factor).toInt()), 255)
        return Color.argb(a, r, g, b)
    }

    // Setter methods
    /**
     * Set the background color of the neumorphic view
     */
    fun setNeumorphBackgroundColor(color: Int) {
        neumorphBackgroundColor = color
        updatePaints()
        invalidate()
    }

    /**
     * Set the light shadow color
     */
    fun setLightShadowColor(color: Int) {
        neumorphLightShadowColor = color
        updatePaints()
        invalidate()
    }

    /**
     * Set the dark shadow color
     */
    fun setDarkShadowColor(color: Int) {
        neumorphDarkShadowColor = color
        updatePaints()
        invalidate()
    }

    /**
     * Set the corner radius
     */
    fun setNeumorphCornerRadius(radius: Float) {
        neumorphCornerRadius = radius
        this.radius = radius
        invalidate()
    }

    /**
     * Set the shadow elevation
     */
    fun setShadowElevation(elevation: Float) {
        neumorphShadowElevation = elevation
        updatePaints()
        invalidate()
    }

    /**
     * Set the stroke width
     */
    fun setStrokeWidth(width: Float) {
        neumorphStrokeWidth = width
        updatePaints()
        invalidate()
    }

    /**
     * Set the stroke color
     */
    fun setStrokeColor(color: Int) {
        neumorphStrokeColor = color
        updatePaints()
        invalidate()
    }

    /**
     * Set the shape type
     * @param shapeType SHAPE_TYPE_FLAT, SHAPE_TYPE_PRESSED, or SHAPE_TYPE_BASIN
     */
    fun setShapeType(shapeType: Int) {
        if (shapeType in SHAPE_TYPE_FLAT..SHAPE_TYPE_BASIN) {
            neumorphShapeType = shapeType
            invalidate()
        }
    }
}