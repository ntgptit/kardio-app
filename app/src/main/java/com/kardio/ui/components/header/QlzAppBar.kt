// ui/components/header/HomeAppBar.kt
package com.kardio.ui.components.header

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
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
            resources.getDimensionPixelSize(R.dimen.toolbar_height)
        )

        // Set content insets
        setContentInsetsRelative(
            resources.getDimensionPixelSize(R.dimen.spacing_md),
            resources.getDimensionPixelSize(R.dimen.spacing_md)
        )

        // Adjust padding
        val horizontalPadding = resources.getDimensionPixelSize(R.dimen.spacing_md)
        val verticalPadding = resources.getDimensionPixelSize(R.dimen.spacing_sm)
        setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Make sure the height is at least the toolbar height
        val minHeight = resources.getDimensionPixelSize(R.dimen.toolbar_height)
        val heightSpec = MeasureSpec.makeMeasureSpec(
            Math.max(MeasureSpec.getSize(heightMeasureSpec), minHeight),
            MeasureSpec.EXACTLY
        )
        super.onMeasure(widthMeasureSpec, heightSpec)
    }
}