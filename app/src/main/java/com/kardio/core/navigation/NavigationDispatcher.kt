//// File: app/src/main/java/com/kardio/core/navigation/NavigationDispatcher.kt
//package com.kardio.core.navigation
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import androidx.navigation.NavDirections
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
///**
// * Helper class to dispatch navigation commands from ViewModels.
// * ViewModels should inject this class to perform navigation.
// */
//class NavigationDispatcher @Inject constructor(
//    private val navigationManager: NavigationManager
//) {
//
//    /**
//     * Navigate to a destination using NavDirections.
//     */
//    fun navigate(directions: NavDirections) {
//        viewModelScope.launch {
//            navigationManager.navigate(directions)
//        }
//    }
//
//    /**
//     * Navigate back to the previous destination.
//     */
//    fun navigateBack() {
//        viewModelScope.launch {
//            navigationManager.navigateBack()
//        }
//    }
//
//    /**
//     * Navigate up in the back stack.
//     */
//    fun navigateUp() {
//        viewModelScope.launch {
//            navigationManager.navigateUp()
//        }
//    }
//
//    /**
//     * Navigate to a destination using a navigation command.
//     */
//    fun navigate(command: NavigationCommand) {
//        viewModelScope.launch {
//            navigationManager.navigate(command)
//        }
//    }
//
//    /**
//     * Extension function to help ViewModels navigate.
//     */
//    fun ViewModel.navigate(directions: NavDirections) {
//        viewModelScope.launch {
//            navigationManager.navigate(directions)
//        }
//    }
//}