// ui/components/extensions/CreateOptionExtensions.kt
package com.kardio.ui.components.extensions

import android.view.View
import com.kardio.databinding.ItemCreateOptionBinding

/**
 * Set the icon tint for a create option item
 */
fun View.setIconTint(color: Int) {
    val binding = ItemCreateOptionBinding.bind(this)
    binding.icon.setColorFilter(color)
}