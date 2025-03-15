package com.kardio.features.dashboard.presentation.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.kardio.R
import com.kardio.features.dashboard.domain.model.StreakDay

/**
 * Helper class to bind streak data to the streak day views
 */
class StreakDayAdapter {

    /**
     * Update a single streak day view with data
     *
     * @param rootView The root view of the streak day item
     * @param streakDay The streak day data
     */
    fun bindStreakDay(rootView: View, streakDay: StreakDay) {
        val dayNumber = rootView.findViewById<TextView>(R.id.day_number)
        val dayFlame = rootView.findViewById<ImageView>(R.id.day_flame)

        // Set the day number
        dayNumber.text = streakDay.day.toString()

        // Show/hide flame icon and update text color based on completion
        if (streakDay.hasCompleted) {
            dayFlame.visibility = View.VISIBLE
            dayNumber.setTextColor(rootView.context.getColor(R.color.text_primary))
        } else {
            dayFlame.visibility = View.GONE
            dayNumber.setTextColor(rootView.context.getColor(R.color.text_secondary))
        }
    }

    /**
     * Update a list of streak day views with data
     *
     * @param rootViews List of root views for streak day items
     * @param streakDays List of streak day data
     */
    fun bindStreakDays(rootViews: List<View>, streakDays: List<StreakDay>) {
        val minSize = minOf(rootViews.size, streakDays.size)

        for (i in 0 until minSize) {
            bindStreakDay(rootViews[i], streakDays[i])
        }
    }
}