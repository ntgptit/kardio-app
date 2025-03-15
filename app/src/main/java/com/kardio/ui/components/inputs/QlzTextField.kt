// ui/components/inputs/QlzTextField.kt
package com.kardio.ui.components.inputs

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.kardio.R

class QlzTextField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val labelTextView: TextView
    private val editText: EditText
    private val errorTextView: TextView
    private val iconView: ImageView
    private val requiredIndicator: TextView

    private var isPasswordField = false
    private var isPasswordVisible = false

    // Thuộc tính error
    var error: String? = null
        set(value) {
            field = value
            updateErrorState()
        }

    init {
        // Inflate layout
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.component_text_field, this, true)

        // Initialize views
        labelTextView = findViewById(R.id.text_field_label)
        editText = findViewById(R.id.text_field_edit_text)
        errorTextView = findViewById(R.id.text_field_error)
        iconView = findViewById(R.id.text_field_icon)
        requiredIndicator = findViewById(R.id.text_field_required)

        // Load attributes
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.QlzTextField)
        try {
            // Set label
            if (typedArray.hasValue(R.styleable.QlzTextField_qlzLabel)) {
                labelTextView.text = typedArray.getString(R.styleable.QlzTextField_qlzLabel)
                labelTextView.visibility = View.VISIBLE
            } else {
                labelTextView.visibility = View.GONE
            }

            // Set icon
            if (typedArray.hasValue(R.styleable.QlzTextField_qlzIcon)) {
                val iconResId = typedArray.getResourceId(R.styleable.QlzTextField_qlzIcon, 0)
                if (iconResId != 0) {
                    iconView.setImageResource(iconResId)
                    iconView.visibility = View.VISIBLE
                } else {
                    iconView.visibility = View.GONE
                }
            } else {
                iconView.visibility = View.GONE
            }

            // Set hint
            if (typedArray.hasValue(R.styleable.QlzTextField_qlzHint)) {
                editText.hint = typedArray.getString(R.styleable.QlzTextField_qlzHint)
            } else if (typedArray.hasValue(R.styleable.QlzTextField_android_hint)) {
                editText.hint = typedArray.getString(R.styleable.QlzTextField_android_hint)
            }

            // Check if required
            val isRequired = typedArray.getBoolean(R.styleable.QlzTextField_qlzRequired, false)
            requiredIndicator.visibility = if (isRequired) View.VISIBLE else View.GONE

            // Check if password field
            isPasswordField = typedArray.getBoolean(R.styleable.QlzTextField_qlzIsPassword, false)
            if (isPasswordField) {
                editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                setupPasswordToggle()
            }

            // Set initial error
            error = typedArray.getString(R.styleable.QlzTextField_qlzErrorText)

        } finally {
            typedArray.recycle()
        }
    }

    private fun setupPasswordToggle() {
        // Setup password visibility toggle if needed
    }

    private fun updateErrorState() {
        if (error.isNullOrEmpty()) {
            errorTextView.visibility = View.GONE
        } else {
            errorTextView.text = error
            errorTextView.visibility = View.VISIBLE
        }
    }

    /**
     * Get the EditText
     */
    fun getEditText(): EditText = editText

    /**
     * Get the text from the EditText
     */
    fun getText(): String = editText.text.toString()

    /**
     * Set the text in the EditText
     */
    fun setText(text: String) {
        editText.setText(text)
    }
}