// File: app/src/main/java/com/kardio/ui/components/navigation/QlzBottomNavBar.kt
package com.kardio.ui.components.navigation

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
import com.kardio.utils.helpers.AnimationUtils

/**
 * QlzBottomNavBar - Bottom navigation bar tối ưu cho Dark Mode
 * Bao gồm 4-5 tab với icon và text, hỗ trợ animation khi chuyển tab
 */
class QlzBottomNavBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val MAX_ITEMS = 5
    }

    private lateinit var navContainer: View
    private val navItems = mutableListOf<NavItemView>()

    private var selectedItemIndex = 0
    private var onItemSelectedListener: ((Int) -> Unit)? = null
    private var isAnimationEnabled = true

    init {
        inflateLayout()
        setupFromAttributes(attrs)
    }

    private fun inflateLayout() {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.component_bottom_nav_bar, this, true)

        navContainer = view.findViewById(R.id.bottom_nav_container)

        // Initialize nav items
        navItems.add(NavItemView(view.findViewById(R.id.nav_item_1)))
        navItems.add(NavItemView(view.findViewById(R.id.nav_item_2)))
        navItems.add(NavItemView(view.findViewById(R.id.nav_item_3)))
        navItems.add(NavItemView(view.findViewById(R.id.nav_item_4)))
        navItems.add(NavItemView(view.findViewById(R.id.nav_item_5)))

        // Set all items to inactive initially
        navItems.forEach { it.setActive(false) }

        // Setup click listeners
        for (i in navItems.indices) {
            navItems[i].itemView.setOnClickListener {
                if (i != selectedItemIndex) {
                    selectItem(i)
                }
            }
        }
    }

    private fun setupFromAttributes(attrs: AttributeSet?) {
        if (attrs == null) return

        val typedArray = context.obtainStyledAttributes(
            attrs, R.styleable.QlzBottomNavBar
        )

        try {
            isAnimationEnabled = typedArray.getBoolean(
                R.styleable.QlzBottomNavBar_qlzAnimationEnabled, true
            )

            // Get item count or default to 4
            val itemCount = typedArray.getInteger(
                R.styleable.QlzBottomNavBar_qlzItemCount, 4
            ).coerceIn(2, MAX_ITEMS)

            // Show only the number of items specified
            for (i in navItems.indices) {
                navItems[i].itemView.visibility = if (i < itemCount) View.VISIBLE else View.GONE
            }

            // Get selected index
            selectedItemIndex = typedArray.getInteger(
                R.styleable.QlzBottomNavBar_qlzSelectedItemIndex, 0
            ).coerceIn(0, itemCount - 1)

            // Get and set item icons/labels if provided
            for (i in 0 until itemCount) {
                val iconAttr = when (i) {
                    0 -> R.styleable.QlzBottomNavBar_qlzItem1Icon
                    1 -> R.styleable.QlzBottomNavBar_qlzItem2Icon
                    2 -> R.styleable.QlzBottomNavBar_qlzItem3Icon
                    3 -> R.styleable.QlzBottomNavBar_qlzItem4Icon
                    4 -> R.styleable.QlzBottomNavBar_qlzItem5Icon
                    else -> 0
                }

                val labelAttr = when (i) {
                    0 -> R.styleable.QlzBottomNavBar_qlzItem1Label
                    1 -> R.styleable.QlzBottomNavBar_qlzItem2Label
                    2 -> R.styleable.QlzBottomNavBar_qlzItem3Label
                    3 -> R.styleable.QlzBottomNavBar_qlzItem4Label
                    4 -> R.styleable.QlzBottomNavBar_qlzItem5Label
                    else -> 0
                }

                if (iconAttr != 0) {
                    val iconRes = typedArray.getResourceId(iconAttr, 0)
                    if (iconRes != 0) {
                        navItems[i].setIcon(iconRes)
                    }
                }

                if (labelAttr != 0) {
                    val label = typedArray.getString(labelAttr)
                    if (!label.isNullOrEmpty()) {
                        navItems[i].setLabel(label)
                    }
                }
            }

        } finally {
            typedArray.recycle()
        }

        // Set initial selected item
        selectItem(selectedItemIndex, false)
    }

    /**
     * Select an item in the bottom nav
     */
    fun selectItem(index: Int, animate: Boolean = isAnimationEnabled) {
        if (index < 0 || index >= navItems.size || navItems[index].itemView.visibility != View.VISIBLE) {
            return
        }

        val previousIndex = selectedItemIndex
        selectedItemIndex = index

        // Update UI
        navItems[previousIndex].setActive(false)
        navItems[index].setActive(true)

        // Animate if enabled
        if (animate) {
            AnimationUtils.applyButtonClickAnimation(navItems[index].itemView, context)
        }

        // Notify listener
        onItemSelectedListener?.invoke(index)
    }

    /**
     * Set a listener for item selection
     */
    fun setOnItemSelectedListener(listener: (Int) -> Unit) {
        onItemSelectedListener = listener
    }

    /**
     * Configure a nav item
     */
    fun configureItem(
        index: Int,
        @DrawableRes iconRes: Int,
        label: String
    ) {
        if (index < 0 || index >= navItems.size) {
            return
        }

        navItems[index].setIcon(iconRes)
        navItems[index].setLabel(label)
    }

    /**
     * Enable or disable animations
     */
    fun setAnimationEnabled(enabled: Boolean) {
        isAnimationEnabled = enabled
    }

    /**
     * Select item by its ID
     */
    fun selectItemById(itemId: Int) {
        for (i in navItems.indices) {
            val navItemId = when (i) {
                0 -> R.id.nav_item_1
                1 -> R.id.nav_item_2
                2 -> R.id.nav_item_3
                3 -> R.id.nav_item_4
                4 -> R.id.nav_item_5
                else -> -1
            }

            if (navItemId == itemId) {
                selectItem(i)
                break
            }
        }
    }

    /**
     * Get the currently selected item index
     */
    fun getSelectedItemIndex(): Int {
        return selectedItemIndex
    }

    /**
     * Inner class to represent a navigation item view
     */
    private inner class NavItemView(val itemView: View) {
        private val iconView: ImageView = itemView.findViewById(R.id.nav_item_icon)
        private val labelView: TextView = itemView.findViewById(R.id.nav_item_label)

        fun setActive(active: Boolean) {
            val colorRes = if (active) R.color.nav_item_active else R.color.nav_item_inactive
            val color = ContextCompat.getColor(context, colorRes)

            iconView.setColorFilter(color)
            labelView.setTextColor(color)

            if (active) {
                itemView.isSelected = true
            } else {
                itemView.isSelected = false
            }
        }

        fun setIcon(@DrawableRes iconRes: Int) {
            iconView.setImageResource(iconRes)
        }

        fun setLabel(text: String) {
            labelView.text = text
        }
    }
}