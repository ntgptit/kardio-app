// utils/extensions/TabLayoutExtensions.kt
package com.kardio.utils.extensions

import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * Extension function to connect TabLayout with ViewPager2
 */
fun TabLayout.setupWithViewPager2(viewPager2: ViewPager2) {
    TabLayoutMediator(this, viewPager2) { _, _ -> }.attach()
}