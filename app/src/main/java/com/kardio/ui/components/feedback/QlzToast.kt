// File: app/src/main/java/com/kardio/ui/components/feedback/QlzSnackbar.kt
package com.kardio.ui.components.feedback

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.kardio.R

/**
 * QlzSnackbar - Custom snackbar component tối ưu cho Dark Mode
 * Hỗ trợ các loại thông báo:
 * - Success
 * - Error
 * - Info
 * - Warning
 *
 * Cung cấp giao diện toast-like nhưng sử dụng Snackbar API hiện đại
 */
class QlzSnackbar private constructor(
    private val context: Context,
    private val type: Int,
    private val message: String,
    private val duration: Int,
    private val iconRes: Int?
) {
    companion object {
        const val TYPE_SUCCESS = 0
        const val TYPE_ERROR = 1
        const val TYPE_INFO = 2
        const val TYPE_WARNING = 3

        const val LENGTH_SHORT = Snackbar.LENGTH_SHORT
        const val LENGTH_LONG = Snackbar.LENGTH_LONG
        const val LENGTH_INDEFINITE = Snackbar.LENGTH_INDEFINITE

        // Giữ tham chiếu đến Snackbar hiện tại
        private var currentSnackbar: Snackbar? = null

        /**
         * Hiển thị thông báo kiểu Success
         */
        fun showSuccess(context: Context, message: String, duration: Int = LENGTH_SHORT) {
            show(
                context,
                TYPE_SUCCESS,
                message,
                duration,
                R.drawable.ic_success
            )
        }

        /**
         * Hiển thị thông báo kiểu Success với string resource
         */
        fun showSuccess(context: Context, @StringRes messageResId: Int, duration: Int = LENGTH_SHORT) {
            showSuccess(context, context.getString(messageResId), duration)
        }

        /**
         * Hiển thị thông báo kiểu Error
         */
        fun showError(context: Context, message: String, duration: Int = LENGTH_SHORT) {
            show(
                context,
                TYPE_ERROR,
                message,
                duration,
                R.drawable.ic_error
            )
        }

        /**
         * Hiển thị thông báo kiểu Error với string resource
         */
        fun showError(context: Context, @StringRes messageResId: Int, duration: Int = LENGTH_SHORT) {
            showError(context, context.getString(messageResId), duration)
        }

        /**
         * Hiển thị thông báo kiểu Info
         */
        fun showInfo(context: Context, message: String, duration: Int = LENGTH_SHORT) {
            show(
                context,
                TYPE_INFO,
                message,
                duration,
                R.drawable.ic_info
            )
        }

        /**
         * Hiển thị thông báo kiểu Info với string resource
         */
        fun showInfo(context: Context, @StringRes messageResId: Int, duration: Int = LENGTH_SHORT) {
            showInfo(context, context.getString(messageResId), duration)
        }

        /**
         * Hiển thị thông báo kiểu Warning
         */
        fun showWarning(context: Context, message: String, duration: Int = LENGTH_SHORT) {
            show(
                context,
                TYPE_WARNING,
                message,
                duration,
                R.drawable.ic_warning
            )
        }

        /**
         * Hiển thị thông báo kiểu Warning với string resource
         */
        fun showWarning(context: Context, @StringRes messageResId: Int, duration: Int = LENGTH_SHORT) {
            showWarning(context, context.getString(messageResId), duration)
        }

        /**
         * Hiển thị thông báo với custom icon
         */
        fun showCustom(
            context: Context,
            message: String,
            @DrawableRes iconRes: Int,
            duration: Int = LENGTH_SHORT
        ) {
            show(
                context,
                TYPE_INFO,
                message,
                duration,
                iconRes
            )
        }

        /**
         * Hiển thị thông báo với action
         */
        fun showWithAction(
            context: Context,
            message: String,
            actionText: String,
            type: Int = TYPE_INFO,
            @DrawableRes iconRes: Int? = R.drawable.ic_info,
            duration: Int = LENGTH_INDEFINITE,
            action: () -> Unit
        ) {
            // Tìm view phù hợp để hiển thị Snackbar
            val activity = context.findActivity()
            if (activity != null) {
                val rootView = activity.findViewById<View>(android.R.id.content)
                QlzSnackbar(context, type, message, duration, iconRes).showSnackbarWithAction(rootView, actionText, action)
            }
        }

        /**
         * Hiển thị thông báo
         */
        private fun show(
            context: Context,
            type: Int,
            message: String,
            duration: Int,
            @DrawableRes iconRes: Int? = null
        ) {
            // Tìm view phù hợp để hiển thị Snackbar
            val activity = context.findActivity()
            if (activity != null) {
                val rootView = activity.findViewById<View>(android.R.id.content)
                QlzSnackbar(context, type, message, duration, iconRes).showSnackbar(rootView)
            }
        }

        /**
         * Ẩn thông báo hiện tại nếu có
         */
        fun dismiss() {
            currentSnackbar?.dismiss()
            currentSnackbar = null
        }

        // Helper function để tìm Activity từ Context
        private fun Context.findActivity(): android.app.Activity? {
            var context = this
            while (context is android.content.ContextWrapper) {
                if (context is android.app.Activity) {
                    return context
                }
                context = context.baseContext
            }
            return null
        }
    }

    /**
     * Hiển thị snackbar tùy chỉnh
     */
    @SuppressLint("RestrictedApi")
    private fun showSnackbar(rootView: View) {
        // Hủy snackbar hiện tại nếu có
        currentSnackbar?.dismiss()

        // Tạo snackbar mới
        val snackbar = Snackbar.make(rootView, "", duration)

        // Tùy chỉnh snackbar
        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout

        // Xóa bỏ TextView mặc định
        val textView = snackbarLayout.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.visibility = View.INVISIBLE

        // Inflate custom layout
        val customView = LayoutInflater.from(context).inflate(R.layout.component_snackbar, snackbarLayout, false)

        // Cấu hình layout
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.gravity = android.view.Gravity.CENTER_HORIZONTAL
        customView.layoutParams = layoutParams

        // Cấu hình các view trong custom layout
        val backgroundView = customView.findViewById<View>(R.id.toast_background)
        val iconView = customView.findViewById<ImageView>(R.id.toast_icon)
        val messageView = customView.findViewById<TextView>(R.id.toast_message)

        // Set message
        messageView.text = message

        // Cấu hình background dựa trên loại thông báo
        val backgroundColor = when (type) {
            TYPE_SUCCESS -> ContextCompat.getColor(context, R.color.success)
            TYPE_ERROR -> ContextCompat.getColor(context, R.color.error)
            TYPE_WARNING -> ContextCompat.getColor(context, R.color.warning)
            else -> ContextCompat.getColor(context, R.color.background_secondary)
        }

        if (backgroundView.background is android.graphics.drawable.GradientDrawable) {
            (backgroundView.background as android.graphics.drawable.GradientDrawable).setColor(backgroundColor)
        } else {
            backgroundView.setBackgroundColor(backgroundColor)
        }

        // Cấu hình icon
        if (iconRes != null) {
            iconView.setImageResource(iconRes)
            iconView.visibility = View.VISIBLE
        } else {
            iconView.visibility = View.GONE
        }

        // Thêm custom view vào snackbar
        snackbarLayout.addView(customView)

        // Thiết lập background cho snackbar
        snackbarLayout.setBackgroundColor(android.graphics.Color.TRANSPARENT)

        // Lưu tham chiếu và hiển thị
        currentSnackbar = snackbar
        snackbar.show()
    }

    /**
     * Hiển thị snackbar với action
     */
    private fun showSnackbarWithAction(rootView: View, actionText: String, actionListener: () -> Unit) {
        // Hủy snackbar hiện tại nếu có
        currentSnackbar?.dismiss()

        // Tạo snackbar mới
        val snackbar = Snackbar.make(rootView, message, duration)
            .setAction(actionText) { actionListener() }

        // Thiết lập màu cho action button
        snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.accent))

        // Thiết lập màu background dựa trên loại thông báo
        val backgroundColor = when (type) {
            TYPE_SUCCESS -> ContextCompat.getColor(context, R.color.success)
            TYPE_ERROR -> ContextCompat.getColor(context, R.color.error)
            TYPE_WARNING -> ContextCompat.getColor(context, R.color.warning)
            else -> ContextCompat.getColor(context, R.color.background_secondary)
        }

        snackbar.view.setBackgroundColor(backgroundColor)

        // Nếu có icon, thêm vào trước text
        if (iconRes != null) {
            val textView = snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            if (textView != null) {
                // Tạo drawable cho icon và đặt bên trái text
                val drawable = ContextCompat.getDrawable(context, iconRes)
                drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
                textView.setCompoundDrawablePadding(context.resources.getDimensionPixelSize(R.dimen.spacing_sm))
                textView.setCompoundDrawables(drawable, null, null, null)
            }
        }

        // Lưu tham chiếu và hiển thị
        currentSnackbar = snackbar
        snackbar.show()
    }
}