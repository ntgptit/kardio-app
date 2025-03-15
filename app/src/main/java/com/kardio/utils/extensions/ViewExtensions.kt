// utils/extensions/ViewExtensions.kt
package com.kardio.utils.extensions

import android.view.View
import android.widget.ImageView
import com.kardio.R

/**
 * Extension function để thiết lập màu tint cho icon trong layout item_create_option
 */
fun View.setIconTint(color: Int) {
    findViewById<ImageView>(R.id.icon)?.setColorFilter(color)
}