// File: app/src/main/java/com/kardio/ui/components/inputs/QlzTextField.kt
package com.kardio.ui.components.inputs

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.kardio.R

/**
 * QlzTextField - Text field component tối ưu cho Dark Mode
 * Bao gồm:
 * - Label (optional)
 * - Input field với background style phù hợp dark mode
 * - Hint text
 * - Error text (optional)
 * - Helper text (optional)
 */
class QlzTextField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    // Views
    private lateinit var labelTextView: TextView
    private lateinit var editText: EditText
    private lateinit var errorTextView: TextView
    private lateinit var helperTextView: TextView
    private lateinit var inputContainer: View

    // Properties
    private var label: String? = null
    private var hint: String? = null
    private var helperText: String? = null
    private var errorText: String? = null
    private var isShowingError = false
    private var isEnabled = true

    // Background
    private lateinit var normalBackground: GradientDrawable
    private lateinit var focusedBackground: GradientDrawable
    private lateinit var disabledBackground: GradientDrawable
    private lateinit var errorBackground: GradientDrawable

    init {
        inflateLayout()
        setupBackgrounds()
        obtainAttributes(attrs)
        setupViews()
        setupListeners()
    }

    private fun inflateLayout() {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.component_text_field, this, true)

        labelTextView = view.findViewById(R.id.text_field_label)
        editText = view.findViewById(R.id.text_field_edit_text)
        errorTextView = view.findViewById(R.id.text_field_error)
        helperTextView = view.findViewById(R.id.text_field_helper)
        inputContainer = view.findViewById(R.id.text_field_container)
    }

    private fun setupBackgrounds() {
        // Normal state
        normalBackground = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = resources.getDimension(R.dimen.radius_sm)
            setColor(ContextCompat.getColor(context, R.color.background_input))
        }

        // Focused state - thêm viền accent color
        focusedBackground = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = resources.getDimension(R.dimen.radius_sm)
            setColor(ContextCompat.getColor(context, R.color.background_input))
            setStroke(
                resources.getDimensionPixelSize(R.dimen.input_field_stroke_width),
                ContextCompat.getColor(context, R.color.accent)
            )
        }

        // Disabled state - giảm alpha
        disabledBackground = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = resources.getDimension(R.dimen.radius_sm)
            setColor(ContextCompat.getColor(context, R.color.background_input))
            alpha = 128 // 50% transparency
        }

        // Error state - viền màu error
        errorBackground = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = resources.getDimension(R.dimen.radius_sm)
            setColor(ContextCompat.getColor(context, R.color.background_input))
            setStroke(
                resources.getDimensionPixelSize(R.dimen.input_field_stroke_width),
                ContextCompat.getColor(context, R.color.error)
            )
        }
    }

    private fun obtainAttributes(attrs: AttributeSet?) {
        if (attrs == null) return

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.QlzTextField)
        try {
            label = typedArray.getString(R.styleable.QlzTextField_qlzLabel)
            hint = typedArray.getString(R.styleable.QlzTextField_android_hint)
            helperText = typedArray.getString(R.styleable.QlzTextField_qlzHelperText)
            errorText = typedArray.getString(R.styleable.QlzTextField_qlzErrorText)
            isEnabled = typedArray.getBoolean(R.styleable.QlzTextField_android_enabled, true)
        } finally {
            typedArray.recycle()
        }
    }

    private fun setupViews() {
        // Set label text
        if (!label.isNullOrEmpty()) {
            labelTextView.text = label
            labelTextView.visibility = View.VISIBLE
        } else {
            labelTextView.visibility = View.GONE
        }

        // Set hint
        editText.hint = hint

        // Set helper text
        if (!helperText.isNullOrEmpty()) {
            helperTextView.text = helperText
            helperTextView.visibility = View.VISIBLE
        } else {
            helperTextView.visibility = View.GONE
        }

        // Set error text if exists
        if (!errorText.isNullOrEmpty()) {
            errorTextView.text = errorText
            showError(true)
        } else {
            showError(false)
        }

        // Set enabled state
        setEnabled(isEnabled)

        // Set default background
        inputContainer.background = normalBackground
    }

    private fun setupListeners() {
        editText.setOnFocusChangeListener { _, hasFocus ->
            updateBackground(hasFocus)
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Clear error when text changes
                if (isShowingError) {
                    showError(false)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun updateBackground(hasFocus: Boolean) {
        if (!isEnabled) {
            inputContainer.background = disabledBackground
            return
        }

        if (isShowingError) {
            inputContainer.background = errorBackground
            return
        }

        inputContainer.background = if (hasFocus) focusedBackground else normalBackground
    }

    // Public methods

    /**
     * Show or hide error message
     */
    fun showError(show: Boolean, errorMessage: String? = null) {
        isShowingError = show

        if (show) {
            if (errorMessage != null) {
                errorText = errorMessage
                errorTextView.text = errorMessage
            }
            errorTextView.visibility = View.VISIBLE
            helperTextView.visibility = View.GONE
        } else {
            errorTextView.visibility = View.GONE
            helperTextView.visibility = if (helperText.isNullOrEmpty()) View.GONE else View.VISIBLE
        }

        updateBackground(editText.hasFocus())
    }

    /**
     * Set the text of the input field
     */
    fun setText(text: String) {
        editText.setText(text)
    }

    /**
     * Get the current text from the input field
     */
    fun getText(): String {
        return editText.text.toString()
    }

    /**
     * Set the hint text
     */
    fun setHint(hintText: String) {
        hint = hintText
        editText.hint = hintText
    }

    /**
     * Set the helper text
     */
    fun setHelperText(text: String?) {
        helperText = text
        helperTextView.text = text

        if (text.isNullOrEmpty()) {
            helperTextView.visibility = View.GONE
        } else if (!isShowingError) {
            helperTextView.visibility = View.VISIBLE
        }
    }

    /**
     * Set the label text
     */
    fun setLabel(text: String?) {
        label = text
        labelTextView.text = text

        if (text.isNullOrEmpty()) {
            labelTextView.visibility = View.GONE
        } else {
            labelTextView.visibility = View.VISIBLE
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        isEnabled = enabled
        editText.isEnabled = enabled
        updateBackground(editText.hasFocus())
    }

    fun setInputType(inputType: Int) {
        editText.inputType = inputType
    }

    fun addTextChangedListener(watcher: TextWatcher) {
        editText.addTextChangedListener(watcher)
    }
}
