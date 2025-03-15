// File: app/src/main/java/com/kardio/ui/components/quiz/QlzAnswerOption.kt
package com.kardio.ui.components.quiz

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.kardio.R
import com.kardio.utils.helpers.AnimationUtils

/**
 * QlzAnswerOption - Component hiển thị một lựa chọn trả lời trong quiz
 * Hỗ trợ các trạng thái: normal, selected, correct, incorrect
 */
class QlzAnswerOption @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        const val STATE_NORMAL = 0
        const val STATE_SELECTED = 1
        const val STATE_CORRECT = 2
        const val STATE_INCORRECT = 3
    }

    private lateinit var container: View
    private lateinit var optionText: TextView
    private lateinit var optionIndicator: TextView

    private var text: String? = null
    private var indicator: String? = null
    private var currentState = STATE_NORMAL
    private var isEnabled = true

    private var onClickListener: (() -> Unit)? = null

    init {
        inflateLayout()
        obtainAttributes(attrs)
        setupViews()
    }

    private fun inflateLayout() {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.component_answer_option, this, true)

        container = view.findViewById(R.id.answer_option_container)
        optionText = view.findViewById(R.id.answer_option_text)
        optionIndicator = view.findViewById(R.id.answer_option_indicator)
    }

    private fun obtainAttributes(attrs: AttributeSet?) {
        if (attrs == null) return

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.QlzAnswerOption)
        try {
            text = typedArray.getString(R.styleable.QlzAnswerOption_qlzOptionText)
            indicator = typedArray.getString(R.styleable.QlzAnswerOption_qlzOptionIndicator)
            currentState = typedArray.getInteger(R.styleable.QlzAnswerOption_qlzOptionState, STATE_NORMAL)
            isEnabled = typedArray.getBoolean(R.styleable.QlzAnswerOption_android_enabled, true)
        } finally {
            typedArray.recycle()
        }
    }

    private fun setupViews() {
        // Set text
        optionText.text = text

        // Set indicator
        optionIndicator.text = indicator

        // Set initial state
        updateState(currentState)

        // Set enabled state
        setEnabled(isEnabled)

        // Set click listener
        container.setOnClickListener {
            if (isEnabled && currentState == STATE_NORMAL) {
                AnimationUtils.applyButtonClickAnimation(container, context)
                onClickListener?.invoke()
            }
        }
    }

    /**
     * Update the visual state of the answer option
     */
    private fun updateState(state: Int) {
        currentState = state

        val background = container.background as GradientDrawable

        when (state) {
            STATE_NORMAL -> {
                background.setColor(ContextCompat.getColor(context, R.color.background_secondary))
                background.setStroke(
                    resources.getDimensionPixelSize(R.dimen.input_field_stroke_width),
                    ContextCompat.getColor(context, R.color.divider)
                )
                optionText.setTextColor(ContextCompat.getColor(context, R.color.text_primary))
                optionIndicator.setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
            }
            STATE_SELECTED -> {
                background.setColor(ContextCompat.getColor(context, R.color.background_secondary))
                background.setStroke(
                    resources.getDimensionPixelSize(R.dimen.input_field_stroke_width),
                    ContextCompat.getColor(context, R.color.accent)
                )
                optionText.setTextColor(ContextCompat.getColor(context, R.color.text_primary))
                optionIndicator.setTextColor(ContextCompat.getColor(context, R.color.accent))
            }
            STATE_CORRECT -> {
                background.setColor(ContextCompat.getColor(context, R.color.background_secondary))
                background.setStroke(
                    resources.getDimensionPixelSize(R.dimen.input_field_stroke_width),
                    ContextCompat.getColor(context, R.color.success)
                )
                optionText.setTextColor(ContextCompat.getColor(context, R.color.text_primary))
                optionIndicator.setTextColor(ContextCompat.getColor(context, R.color.success))
            }
            STATE_INCORRECT -> {
                background.setColor(ContextCompat.getColor(context, R.color.background_secondary))
                background.setStroke(
                    resources.getDimensionPixelSize(R.dimen.input_field_stroke_width),
                    ContextCompat.getColor(context, R.color.error)
                )
                optionText.setTextColor(ContextCompat.getColor(context, R.color.text_primary))
                optionIndicator.setTextColor(ContextCompat.getColor(context, R.color.error))
            }
        }
    }

    /**
     * Set the option text
     */
    fun setText(text: String) {
        this.text = text
        optionText.text = text
    }

    /**
     * Get the option text
     */
    fun getText(): String {
        return optionText.text.toString()
    }

    /**
     * Set the option indicator (usually A, B, C, D)
     */
    fun setIndicator(indicator: String) {
        this.indicator = indicator
        optionIndicator.text = indicator
    }

    /**
     * Get the option indicator
     */
    fun getIndicator(): String {
        return optionIndicator.text.toString()
    }

    /**
     * Set the option state
     */
    fun setState(state: Int) {
        updateState(state)
    }

    /**
     * Get the current state
     */
    fun getState(): Int {
        return currentState
    }

    /**
     * Set click listener
     */
    fun setOnOptionClickListener(listener: () -> Unit) {
        onClickListener = listener
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        isEnabled = enabled
        container.isEnabled = enabled
        container.alpha = if (enabled) 1.0f else 0.5f
    }
}