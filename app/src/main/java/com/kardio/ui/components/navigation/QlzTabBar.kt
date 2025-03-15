// File: app/src/main/java/com/kardio/ui/components/navigation/QlzTabBar.kt
package com.kardio.ui.components.navigation

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.kardio.R

/**
 * QlzTabBar - Scrollable tab bar tối ưu cho Dark Mode
 * Hỗ trợ tab với text và indicator
 */
class QlzTabBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : HorizontalScrollView(context, attrs, defStyleAttr) {

    private lateinit var tabContainer: LinearLayout
    private lateinit var tabIndicator: View

    private val tabViews = mutableListOf<TextView>()
    private var selectedTabIndex = 0
    private var onTabSelectedListener: ((Int) -> Unit)? = null

    init {
        isHorizontalScrollBarEnabled = false
        inflateLayout()
        setupFromAttributes(attrs)
    }

    private fun inflateLayout() {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.component_tab_bar, this, true)

        tabContainer = view.findViewById(R.id.tab_container)
        tabIndicator = view.findViewById(R.id.tab_indicator)
    }

    private fun setupFromAttributes(attrs: AttributeSet?) {
        if (attrs == null) return

        val typedArray = context.obtainStyledAttributes(
            attrs, R.styleable.QlzTabBar
        )

        try {
            // Get tab entries if defined in XML
            val entriesResId = typedArray.getResourceId(
                R.styleable.QlzTabBar_qlzEntries, 0
            )

            if (entriesResId != 0) {
                val entries = resources.getStringArray(entriesResId)
                setTabs(entries.toList())
            }

            // Get selected index
            selectedTabIndex = typedArray.getInteger(
                R.styleable.QlzTabBar_qlzSelectedTabIndex, 0
            )

        } finally {
            typedArray.recycle()
        }
    }

    /**
     * Set tabs from a list of strings
     */
    fun setTabs(tabTexts: List<String>) {
        // Clear existing tabs
        tabContainer.removeAllViews()
        tabViews.clear()

        // Create new tabs
        val inflater = LayoutInflater.from(context)

        for (i in tabTexts.indices) {
            val tabView = inflater.inflate(
                R.layout.item_tab, tabContainer, false
            ) as TextView

            tabView.text = tabTexts[i]
            tabView.setOnClickListener {
                selectTab(i)
            }

            tabContainer.addView(tabView)
            tabViews.add(tabView)
        }

        // Select initial tab
        if (tabTexts.isNotEmpty()) {
            selectTab(selectedTabIndex.coerceIn(0, tabTexts.size - 1), false)
        }
    }

    /**
     * Select a tab
     */
    fun selectTab(index: Int, smoothScroll: Boolean = true) {
        if (index < 0 || index >= tabViews.size) {
            return
        }

        // Update selected state
        tabViews.forEachIndexed { i, tabView ->
            val isSelected = i == index
            updateTabAppearance(tabView, isSelected)
        }

        // Remember selected index
        selectedTabIndex = index

        // Update indicator position
        updateIndicatorPosition(smoothScroll)

        // Scroll to make the selected tab visible
        val selectedTab = tabViews[index]
        val scrollX = selectedTab.left - width / 2 + selectedTab.width / 2
        if (smoothScroll) {
            smoothScrollTo(scrollX.coerceAtLeast(0), 0)
        } else {
            scrollTo(scrollX.coerceAtLeast(0), 0)
        }

        // Notify listener
        onTabSelectedListener?.invoke(index)
    }

    /**
     * Update the appearance of a tab based on selection state
     */
    private fun updateTabAppearance(tabView: TextView, isSelected: Boolean) {
        val textColorRes = if (isSelected) R.color.tab_selected else R.color.tab_unselected
        tabView.setTextColor(ContextCompat.getColor(context, textColorRes))
        tabView.isSelected = isSelected
    }

    /**
     * Update the indicator position to match the selected tab
     */
    private fun updateIndicatorPosition(animate: Boolean) {
        if (selectedTabIndex < 0 || selectedTabIndex >= tabViews.size) {
            return
        }

        val selectedTab = tabViews[selectedTabIndex]
        val indicatorWidth = selectedTab.width
        val indicatorLeft = selectedTab.left

        val layoutParams = tabIndicator.layoutParams as LinearLayout.LayoutParams
        layoutParams.width = indicatorWidth
        layoutParams.setMargins(indicatorLeft, 0, 0, 0)
        tabIndicator.layoutParams = layoutParams

        if (animate) {
            tabIndicator.animate()
                .setDuration(200)
                .start()
        }
    }

    /**
     * Set a listener for tab selection
     */
    fun setOnTabSelectedListener(listener: (Int) -> Unit) {
        onTabSelectedListener = listener
    }

    /**
     * Get the currently selected tab index
     */
    fun getSelectedTabIndex(): Int {
        return selectedTabIndex
    }

    /**
     * Get the text of the currently selected tab
     */
    fun getSelectedTabText(): String {
        return if (selectedTabIndex >= 0 && selectedTabIndex < tabViews.size) {
            tabViews[selectedTabIndex].text.toString()
        } else {
            ""
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (changed) {
            // Update the indicator when layout changes
            updateIndicatorPosition(false)
        }
    }
}