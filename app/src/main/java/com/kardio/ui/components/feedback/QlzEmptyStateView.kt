// File: app/src/main/java/com/kardio/ui/components/feedback/QlzEmptyStateView.kt
package com.kardio.ui.components.feedback

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.kardio.R

/**
 * QlzEmptyStateView - Component hiển thị trạng thái empty
 * Bao gồm:
 * - Icon
 * - Title
 * - Description
 * - Action button (optional)
 */
class QlzEmptyStateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var iconView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var actionButton: Button

    private var title: String? = null
    private var description: String? = null
    private var buttonText: String? = null
    private var iconResId: Int = R.drawable.ic_empty_state

    private var onActionClickListener: (() -> Unit)? = null

    init {
        inflateLayout()
        obtainAttributes(attrs)
        setupViews()
    }

    private fun inflateLayout() {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.component_empty_state, this, true)

        iconView = view.findViewById(R.id.empty_state_icon)
        titleTextView = view.findViewById(R.id.empty_state_title)
        descriptionTextView = view.findViewById(R.id.empty_state_description)
        actionButton = view.findViewById(R.id.empty_state_action_button)
    }

    private fun obtainAttributes(attrs: AttributeSet?) {
        if (attrs == null) return

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.QlzEmptyStateView)
        try {
            title = typedArray.getString(R.styleable.QlzEmptyStateView_qlzTitle)
            description = typedArray.getString(R.styleable.QlzEmptyStateView_qlzDescription)
            buttonText = typedArray.getString(R.styleable.QlzEmptyStateView_qlzButtonText)
            iconResId = typedArray.getResourceId(
                R.styleable.QlzEmptyStateView_qlzIcon,
                R.drawable.ic_empty_state
            )
        } finally {
            typedArray.recycle()
        }
    }

    private fun setupViews() {
        // Set icon
        iconView.setImageResource(iconResId)

        // Set title
        if (!title.isNullOrEmpty()) {
            titleTextView.text = title
            titleTextView.visibility = View.VISIBLE
        } else {
            titleTextView.visibility = View.GONE
        }

        // Set description
        if (!description.isNullOrEmpty()) {
            descriptionTextView.text = description
            descriptionTextView.visibility = View.VISIBLE
        } else {
            descriptionTextView.visibility = View.GONE
        }

        // Set button
        if (!buttonText.isNullOrEmpty()) {
            actionButton.text = buttonText
            actionButton.visibility = View.VISIBLE
            actionButton.setOnClickListener {
                onActionClickListener?.invoke()
            }
        } else {
            actionButton.visibility = View.GONE
        }
    }

    /**
     * Đặt title cho empty state
     */
    fun setTitle(text: String?) {
        title = text

        if (text.isNullOrEmpty()) {
            titleTextView.visibility = View.GONE
        } else {
            titleTextView.text = text
            titleTextView.visibility = View.VISIBLE
        }
    }

    /**
     * Đặt description cho empty state
     */
    fun setDescription(text: String?) {
        description = text

        if (text.isNullOrEmpty()) {
            descriptionTextView.visibility = View.GONE
        } else {
            descriptionTextView.text = text
            descriptionTextView.visibility = View.VISIBLE
        }
    }

    /**
     * Đặt text cho action button
     */
    fun setButtonText(text: String?) {
        buttonText = text

        if (text.isNullOrEmpty()) {
            actionButton.visibility = View.GONE
        } else {
            actionButton.text = text
            actionButton.visibility = View.VISIBLE
        }
    }

    /**
     * Đặt icon cho empty state
     */
    fun setIcon(@DrawableRes iconRes: Int) {
        iconResId = iconRes
        iconView.setImageResource(iconRes)
    }

    /**
     * Đặt listener cho action button
     */
    fun setOnActionClickListener(listener: () -> Unit) {
        onActionClickListener = listener
    }
}