// File: app/src/main/java/com/kardio/ui/components/inputs/QlzSearchField.kt
package com.kardio.ui.components.inputs

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.kardio.R

/**
 * QlzSearchField - Search input component tối ưu cho Dark Mode
 * Bao gồm:
 * - Search icon
 * - Input field
 * - Clear button (hiển thị khi có text)
 */
class QlzSearchField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    // Views
    private lateinit var searchIcon: ImageView
    private lateinit var clearButton: ImageView
    private lateinit var searchEditText: EditText

    // Properties
    private var hint: String? = null
    private var onSearchTextChangedListener: ((String) -> Unit)? = null
    private var onClearClickListener: (() -> Unit)? = null

    init {
        inflateLayout()
        obtainAttributes(attrs)
        setupViews()
        setupListeners()
    }

    private fun inflateLayout() {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.component_search_field, this, true)

        searchIcon = view.findViewById(R.id.search_icon)
        clearButton = view.findViewById(R.id.search_clear_button)
        searchEditText = view.findViewById(R.id.search_edit_text)
    }

    private fun obtainAttributes(attrs: AttributeSet?) {
        if (attrs == null) return

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.QlzSearchField)
        try {
            hint = typedArray.getString(R.styleable.QlzSearchField_android_hint)
        } finally {
            typedArray.recycle()
        }
    }

    private fun setupViews() {
        // Set hint
        searchEditText.hint = hint

        // Set icon tint
        searchIcon.setColorFilter(
            ContextCompat.getColor(context, R.color.icon_secondary)
        )

        // Set clear button tint and visibility
        clearButton.setColorFilter(
            ContextCompat.getColor(context, R.color.icon_secondary)
        )
        clearButton.visibility = View.GONE
    }

    private fun setupListeners() {
        searchEditText.doOnTextChanged { text, _, _, _ ->
            val hasText = !text.isNullOrEmpty()
            clearButton.visibility = if (hasText) View.VISIBLE else View.GONE
            onSearchTextChangedListener?.invoke(text.toString())
        }

        clearButton.setOnClickListener {
            searchEditText.text.clear()
            onClearClickListener?.invoke()
        }
    }

    // Public methods

    /**
     * Set the search text
     */
    fun setSearchText(text: String) {
        searchEditText.setText(text)
    }

    /**
     * Get the current search text
     */
    fun getSearchText(): String {
        return searchEditText.text.toString()
    }

    /**
     * Set the hint text
     */
    fun setHint(hintText: String) {
        hint = hintText
        searchEditText.hint = hintText
    }

    /**
     * Set listener for search text changes
     */
    fun setOnSearchTextChangedListener(listener: (String) -> Unit) {
        onSearchTextChangedListener = listener
    }

    /**
     * Set listener for clear button clicks
     */
    fun setOnClearClickListener(listener: () -> Unit) {
        onClearClickListener = listener
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        searchEditText.isEnabled = enabled
        searchIcon.alpha = if (enabled) 1.0f else 0.5f
        clearButton.isEnabled = enabled
    }
}