// utils/InsetsCompatWrapper.kt
package com.kardio.utils

import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding

/**
 * Utility class để xử lý Window Insets một cách thống nhất
 * giữa các phiên bản API khác nhau.
 */
object InsetsCompatWrapper {

    /**
     * Thiết lập listener cho window insets và xử lý insets đối với AppBar và BottomNavigation
     */
    fun setupInsetsForHomeScreen(
        rootView: View,
//        appBar: View,
        bottomNav: View
    ) {
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Apply top padding to AppBar
//            appBar.updatePadding(top = systemBars.top)

            // Apply bottom padding to BottomNavigationView
            bottomNav.updatePadding(bottom = systemBars.bottom)

            insets
        }
    }

    /**
     * Thiết lập listener cho window insets và cập nhật margin cho view được chỉ định
     */
    fun setupMarginsForView(
        view: View,
        applyTop: Boolean = false,
        applyBottom: Boolean = false,
        applyStart: Boolean = false,
        applyEnd: Boolean = false
    ) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Sử dụng ViewGroup.MarginLayoutParams chung cho mọi loại container
            (v.layoutParams as? ViewGroup.MarginLayoutParams)?.let { params ->
                if (applyTop) params.topMargin = systemBars.top
                if (applyBottom) params.bottomMargin = systemBars.bottom
                if (applyStart) params.leftMargin = systemBars.left
                if (applyEnd) params.rightMargin = systemBars.right

                v.layoutParams = params
            }

            insets
        }
    }

    /**
     * Thiết lập listener cho window insets và cập nhật padding cho view được chỉ định
     */
    fun setupPaddingForView(
        view: View,
        applyTop: Boolean = false,
        applyBottom: Boolean = false,
        applyStart: Boolean = false,
        applyEnd: Boolean = false
    ) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            val paddingStart = if (applyStart) systemBars.left else v.paddingStart
            val paddingTop = if (applyTop) systemBars.top else v.paddingTop
            val paddingEnd = if (applyEnd) systemBars.right else v.paddingEnd
            val paddingBottom = if (applyBottom) systemBars.bottom else v.paddingBottom

            v.setPadding(paddingStart, paddingTop, paddingEnd, paddingBottom)

            insets
        }
    }

    /**
     * Lấy chiều cao của status bar
     */
    fun getStatusBarHeight(view: View): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowInsets = view.rootWindowInsets
            windowInsets?.getInsetsIgnoringVisibility(WindowInsets.Type.statusBars())?.top ?: 0
        } else {
            val resourceId = view.context.resources.getIdentifier(
                "status_bar_height", "dimen", "android"
            )
            if (resourceId > 0) {
                view.context.resources.getDimensionPixelSize(resourceId)
            } else {
                0
            }
        }
    }

    /**
     * Lấy chiều cao của navigation bar
     */
    fun getNavigationBarHeight(view: View): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowInsets = view.rootWindowInsets
            windowInsets?.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars())?.bottom ?: 0
        } else {
            val resourceId = view.context.resources.getIdentifier(
                "navigation_bar_height", "dimen", "android"
            )
            if (resourceId > 0) {
                view.context.resources.getDimensionPixelSize(resourceId)
            } else {
                0
            }
        }
    }
}