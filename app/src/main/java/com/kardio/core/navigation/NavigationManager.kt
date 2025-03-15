// File: app/src/main/java/com/kardio/core/navigation/NavigationManager.kt
package com.kardio.core.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Navigation manager class that handles navigation commands throughout the app.
 * Uses a SharedFlow to emit navigation commands to the UI layer.
 */
@Singleton
class NavigationManager @Inject constructor() {

    private val _navigationCommands = MutableSharedFlow<NavigationCommand>()
    val navigationCommands: SharedFlow<NavigationCommand> = _navigationCommands.asSharedFlow()

    /**
     * Emit a navigation command.
     */
    suspend fun navigate(command: NavigationCommand) {
        _navigationCommands.emit(command)
    }

    /**
     * Navigate to a destination using NavDirections.
     */
    suspend fun navigate(directions: androidx.navigation.NavDirections) {
        _navigationCommands.emit(NavigationCommand.ToDirection(directions))
    }

    /**
     * Navigate back to the previous destination.
     */
    suspend fun navigateBack() {
        _navigationCommands.emit(NavigationCommand.Back)
    }

    /**
     * Navigate up in the back stack.
     */
    suspend fun navigateUp() {
        _navigationCommands.emit(NavigationCommand.Up)
    }
}
