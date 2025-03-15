// ui/components/header/HomeAppBar.kt
package com.kardio.ui.components.header

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.kardio.R

class HomeAppBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : Toolbar(context, attrs, defStyleAttr) {

    init {
        setupAppBar()
    }

    private fun setupAppBar() {
        // Set gradient background
        background = ContextCompat.getDrawable(context, R.drawable.bg_app_bar)

        // Set elevation
        elevation = resources.getDimension(R.dimen.app_bar_elevation)

        // Set layout params
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}