// File: app/src/main/java/com/kardio/ui/components/header/QlzAppBar.kt
package com.kardio.ui.components.header

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.kardio.R

/**
 * QlzAppBar - Custom toolbar cho Dark Mode
 * Hỗ trợ left icon (thường là back/menu), title, right icon (action)
 */
class QlzAppBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var leftIconView: ImageView
    private lateinit var rightIconView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var dividerView: View

    private var title: String? = null
    private var leftIconRes: Int = 0
    private var rightIconRes: Int = 0
    private var showDivider: Boolean = true

    private var onLeftIconClickListener: (() -> Unit)? = null
    private var onRightIconClickListener: (() -> Unit)? = null

    init {
        inflateLayout()
        setupFromAttributes(attrs)
        setupViews()
    }

    private fun inflateLayout() {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.component_app_bar, this, true)

        leftIconView = view.findViewById(R.id.app_bar_left_icon)
        rightIconView = view.findViewById(R.id.app_bar_right_icon)
        titleTextView = view.findViewById(R.id.app_bar_title)
        dividerView = view.findViewById(R.id.app_bar_divider)
    }

    private fun setupFromAttributes(attrs: AttributeSet?) {
        if (attrs == null) return

        val typedArray = context.obtainStyledAttributes(
            attrs, R.styleable.QlzAppBar
        )

        try {
            title = typedArray.getString(R.styleable.QlzAppBar_qlzTitle)
            leftIconRes = typedArray.getResourceId(R.styleable.QlzAppBar_qlzLeftIcon, 0)
            rightIconRes = typedArray.getResourceId(R.styleable.QlzAppBar_qlzRightIcon, 0)
            showDivider = typedArray.getBoolean(R.styleable.QlzAppBar_qlzShowDivider, true)
        } finally {
            typedArray.recycle()
        }
    }

    private fun setupViews() {
        // Set title
        titleTextView.text = title

        // Set left icon
        if (leftIconRes != 0) {
            leftIconView.setImageResource(leftIconRes)
            leftIconView.visibility = View.VISIBLE
            leftIconView.setOnClickListener {
                onLeftIconClickListener?.invoke()
            }
        } else {
            leftIconView.visibility = View.GONE
        }

        // Set right icon
        if (rightIconRes != 0) {
            rightIconView.setImageResource(rightIconRes)
            rightIconView.visibility = View.VISIBLE
            rightIconView.setOnClickListener {
                onRightIconClickListener?.invoke()
            }
        } else {
            rightIconView.visibility = View.GONE
        }

        // Set divider
        dividerView.visibility = if (showDivider) View.VISIBLE else View.GONE

        // Set icons tint
        leftIconView.setColorFilter(ContextCompat.getColor(context, R.color.icon_primary))
        rightIconView.setColorFilter(ContextCompat.getColor(context, R.color.icon_primary))
    }

    /**
     * Set the title
     */
    fun setTitle(title: String) {
        this.title = title
        titleTextView.text = title
    }

    /**
     * Get the title
     */
    fun getTitle(): String {
        return titleTextView.text.toString()
    }

    /**
     * Set the left icon
     */
    fun setLeftIcon(@DrawableRes iconRes: Int) {
        leftIconRes = iconRes

        if (iconRes != 0) {
            leftIconView.setImageResource(iconRes)
            leftIconView.visibility = View.VISIBLE
        } else {
            leftIconView.visibility = View.GONE
        }
    }

    /**
     * Set the right icon
     */
    fun setRightIcon(@DrawableRes iconRes: Int) {
        rightIconRes = iconRes

        if (iconRes != 0) {
            rightIconView.setImageResource(iconRes)
            rightIconView.visibility = View.VISIBLE
        } else {
            rightIconView.visibility = View.GONE
        }
    }

    /**
     * Show or hide the divider
     */
    fun setShowDivider(show: Boolean) {
        showDivider = show
        dividerView.visibility = if (show) View.VISIBLE else View.GONE
    }

    /**
     * Set listener for left icon click
     */
    fun setOnLeftIconClickListener(listener: () -> Unit) {
        onLeftIconClickListener = listener
    }

    /**
     * Set listener for right icon click
     */
    fun setOnRightIconClickListener(listener: () -> Unit) {
        onRightIconClickListener = listener
    }
}