// File: app/src/main/java/com/kardio/ui/components/feedback/QlzLoadingIndicator.kt
package com.kardio.ui.components.feedback

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.kardio.R
import com.kardio.utils.helpers.AnimationUtils
import androidx.core.view.isVisible

/**
 * QlzLoadingIndicator - Component hiển thị trạng thái loading
 * Bao gồm:
 * - Progress bar (CircularProgressIndicator)
 * - Message text (optional)
 */
class QlzLoadingIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var progressBar: ProgressBar
    private lateinit var messageTextView: TextView

    private var message: String? = null
    private var isLoading = false

    init {
        inflateLayout()
        obtainAttributes(attrs)
        setupViews()
    }

    private fun inflateLayout() {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.component_loading_indicator, this, true)

        progressBar = view.findViewById(R.id.loading_progress)
        messageTextView = view.findViewById(R.id.loading_message)
    }

    private fun obtainAttributes(attrs: AttributeSet?) {
        if (attrs == null) return

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.QlzLoadingIndicator)
        try {
            message = typedArray.getString(R.styleable.QlzLoadingIndicator_qlzLoadingMessage)
            isLoading = typedArray.getBoolean(R.styleable.QlzLoadingIndicator_qlzIsLoading, false)
        } finally {
            typedArray.recycle()
        }
    }

    private fun setupViews() {
        // Set progress bar color
        progressBar.indeterminateTintList = ContextCompat.getColorStateList(
            context, R.color.accent
        )

        // Set message
        if (!message.isNullOrEmpty()) {
            messageTextView.text = message
            messageTextView.visibility = View.VISIBLE
        } else {
            messageTextView.visibility = View.GONE
        }

        // Set initial visibility
        visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    /**
     * Hiển thị loading indicator
     */
    fun show(animate: Boolean = true) {
        if (isVisible) return

        isLoading = true
        if (animate) {
            AnimationUtils.fadeIn(this, context)
        } else {
            visibility = View.VISIBLE
        }
    }

    /**
     * Ẩn loading indicator
     */
    fun hide(animate: Boolean = true) {
        if (visibility == View.GONE) return

        isLoading = false
        if (animate) {
            AnimationUtils.fadeOut(this, context)
        } else {
            visibility = View.GONE
        }
    }

    /**
     * Đặt message cho loading indicator
     */
    fun setMessage(message: String?) {
        this.message = message

        if (message.isNullOrEmpty()) {
            messageTextView.visibility = View.GONE
        } else {
            messageTextView.text = message
            messageTextView.visibility = View.VISIBLE
        }
    }

    /**
     * Lấy trạng thái hiện tại của loading indicator
     */
    fun isLoading(): Boolean {
        return isLoading
    }
}
