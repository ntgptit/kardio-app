// ui/components/buttons/QlzPrimaryButton.kt
package com.kardio.ui.components.buttons

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.kardio.R

class QlzPrimaryButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val textView: TextView
    private val progressBar: ProgressBar
    private val iconView: ImageView

    // Di chuyển khai báo cornerRadius lên trước khối init
    private val cornerRadius = resources.getDimension(R.dimen.button_corner_radius)

    var isLoading: Boolean = false
        set(value) {
            field = value
            updateLoadingState()
        }

    init {
        // Inflate layout
        View.inflate(context, R.layout.component_primary_button, this)

        // Find views
        textView = findViewById(R.id.button_text)
        progressBar = findViewById(R.id.button_progress)
        iconView = findViewById(R.id.button_icon)

        // Get attributes
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.QlzPrimaryButton)
        try {
            // Set button text
            if (typedArray.hasValue(R.styleable.QlzPrimaryButton_qlzButtonText)) {
                textView.text = typedArray.getString(R.styleable.QlzPrimaryButton_qlzButtonText)
            }

            // Set text color
            if (typedArray.hasValue(R.styleable.QlzPrimaryButton_qlzTextColor)) {
                textView.setTextColor(typedArray.getColor(
                    R.styleable.QlzPrimaryButton_qlzTextColor,
                    ContextCompat.getColor(context, R.color.text_primary)
                ))
            }

            // Set icon if available
            if (typedArray.hasValue(R.styleable.QlzPrimaryButton_icon)) {
                val iconRes = typedArray.getResourceId(R.styleable.QlzPrimaryButton_icon, 0)
                if (iconRes != 0) {
                    iconView.setImageResource(iconRes)
                    iconView.visibility = View.VISIBLE

                    // Apply icon tint if specified
                    if (typedArray.hasValue(R.styleable.QlzPrimaryButton_iconTint)) {
                        val tintColor = typedArray.getColor(
                            R.styleable.QlzPrimaryButton_iconTint,
                            textView.currentTextColor
                        )
                        iconView.setColorFilter(tintColor)
                    } else {
                        // Use text color by default
                        iconView.setColorFilter(textView.currentTextColor)
                    }
                } else {
                    iconView.visibility = View.GONE
                }
            } else {
                iconView.visibility = View.GONE
            }

            // Set background color
            val backgroundColor = typedArray.getColor(
                R.styleable.QlzPrimaryButton_qlzBackgroundColor,
                ContextCompat.getColor(context, R.color.primary_button)
            )

            // Apply background with rounded corners
            val background = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                setColor(backgroundColor)
                cornerRadius = this@QlzPrimaryButton.cornerRadius
            }
            this.background = background

            // Set loading state
            isLoading = typedArray.getBoolean(
                R.styleable.QlzPrimaryButton_qlzIsLoading,
                false
            )
        } finally {
            typedArray.recycle()
        }

        // Initialize loading state
        updateLoadingState()
    }

    /**
     * Update the button's appearance based on loading state
     */
    private fun updateLoadingState() {
        if (isLoading) {
            textView.visibility = View.INVISIBLE
            iconView.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
            isEnabled = false
        } else {
            textView.visibility = View.VISIBLE
            iconView.visibility = if (iconView.drawable != null) View.VISIBLE else View.GONE
            progressBar.visibility = View.GONE
            isEnabled = true
        }
    }

    /**
     * Set the button text
     */
    fun setText(text: String) {
        textView.text = text
    }

    /**
     * Get the button text
     */
    fun getText(): String {
        return textView.text.toString()
    }

    /**
     * Set button icon
     */
    fun setIcon(resourceId: Int) {
        if (resourceId != 0) {
            iconView.setImageResource(resourceId)
            iconView.visibility = View.VISIBLE
        } else {
            iconView.visibility = View.GONE
        }
    }

    /**
     * Set icon tint color
     */
    fun setIconTint(color: Int) {
        iconView.setColorFilter(color)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        alpha = if (enabled) 1.0f else 0.5f
    }
}