// File: app/src/main/java/com/kardio/core/navigation/NavigationExtensions.kt
package com.kardio.core.navigation

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Extension function for Fragment to setup navigation observation.
 * Collects navigation commands from the NavigationManager and performs the appropriate navigation.
 */
fun Fragment.observeNavigation(navigationManager: NavigationManager) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            navigationManager.navigationCommands.collectLatest { command ->
                handleNavigation(command)
            }
        }
    }
}

/**
 * Extension function for Fragment to handle a navigation command.
 */
private fun Fragment.handleNavigation(command: NavigationCommand) {
    val navController = findNavController()

    when (command) {
        is NavigationCommand.ToDirection -> {
            navController.navigate(command.directions)
        }
        is NavigationCommand.ToDirectionWithOptions -> {
            navController.navigate(command.directions, command.options)
        }
        is NavigationCommand.ToDestination -> {
            navController.navigate(command.destinationId, command.args)
        }
        is NavigationCommand.Back -> {
            navController.popBackStack()
        }
        is NavigationCommand.Up -> {
            navController.navigateUp()
        }
        is NavigationCommand.BackToDestination -> {
            navController.popBackStack(command.destinationId, command.inclusive)
        }
    }
}

/**
 * Extension function for NavController to safely navigate back to a destination.
 */
fun NavController.navigateBackTo(@IdRes destinationId: Int, inclusive: Boolean = false) {
    if (currentDestination?.id != destinationId) {
        popBackStack(destinationId, inclusive)
    }
}