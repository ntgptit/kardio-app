// File: app/src/main/java/com/kardio/core/navigation/NavigationCommand.kt
package com.kardio.core.navigation

import android.os.Bundle
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions

/**
 * Sealed class representing different navigation commands.
 * Used for abstracting navigation logic from UI components.
 */
sealed class NavigationCommand {
    /**
     * Navigate to a destination using NavDirections.
     */
    data class ToDirection(val directions: NavDirections) : NavigationCommand()

    /**
     * Navigate to a destination using NavDirections with custom NavOptions.
     */
    data class ToDirectionWithOptions(
        val directions: NavDirections,
        val options: NavOptions
    ) : NavigationCommand()

    /**
     * Navigate to a destination using destination ID.
     */
    data class ToDestination(val destinationId: Int, val args: Bundle? = null) : NavigationCommand()

    /**
     * Navigate back to the previous destination.
     */
    object Back : NavigationCommand()

    /**
     * Navigate up in the back stack.
     */
    object Up : NavigationCommand()

    /**
     * Navigate back to a specific destination.
     */
    data class BackToDestination(val destinationId: Int, val inclusive: Boolean = false) : NavigationCommand()
}