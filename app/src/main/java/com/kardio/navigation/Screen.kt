package com.kardio.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kardio.features.auth.presentation.screens.ForgotPasswordScreen
import com.kardio.features.auth.presentation.screens.LoginScreen
import com.kardio.features.auth.presentation.screens.RegisterScreen
import com.kardio.features.dashboard.presentation.screens.DashboardScreen
import com.kardio.features.home.presentation.screens.HomeScreen
import com.kardio.features.library.presentation.screens.CreateClassScreen
import com.kardio.features.library.presentation.screens.CreateFolderScreen
import com.kardio.features.library.presentation.screens.LibraryScreen
import com.kardio.features.module.presentation.screens.CreateStudyModuleScreen
import com.kardio.features.module.presentation.screens.ModuleDetailScreen
import com.kardio.features.welcome.presentation.screens.WelcomeScreen

/**
 * Định nghĩa các route cho Navigation
 */
sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")
    object Home : Screen("home")
    object Dashboard : Screen("dashboard")
    object Library : Screen("library")
    object CreateFolder : Screen("create_folder")
    object CreateClass : Screen("create_class")
    object FolderDetail : Screen("folder_detail/{folderId}?folderName={folderName}") {
        fun createRoute(folderId: String, folderName: String = "") =
            "folder_detail/$folderId?folderName=$folderName"
    }

    object ClassDetail : Screen("class_detail/{classId}?className={className}") {
        fun createRoute(classId: String, className: String = "") =
            "class_detail/$classId?className=$className"
    }

    object ModuleDetail : Screen("module_detail/{moduleId}") {
        fun createRoute(moduleId: String) = "module_detail/$moduleId"
    }

    object CreateStudyModule : Screen("create_study_module")
}

/**
 * NavHost chính của ứng dụng
 */
@Composable
fun KardioNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Welcome.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Welcome Screen
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                navigateToRegister = { navController.navigate(Screen.Register.route) },
                navigateToLogin = { navController.navigate(Screen.Login.route) },
                navigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        // Login Screen
        composable(Screen.Login.route) {
            LoginScreen(
                navigateToRegister = {
                    navController.navigate(Screen.Register.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                navigateToForgotPassword = { navController.navigate(Screen.ForgotPassword.route) },
                navigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        // Register Screen
        composable(Screen.Register.route) {
            RegisterScreen(
                navigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                navigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        // Forgot Password Screen
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                navigateBack = { navController.popBackStack() }
            )
        }

        // Home Screen (Container với bottom navigation)
        composable(Screen.Home.route) {
            HomeScreen(
                navigateToCreateFolder = { navController.navigate(Screen.CreateFolder.route) },
                navigateToCreateClass = { navController.navigate(Screen.CreateClass.route) },
                navigateToCreateStudyModule = { navController.navigate(Screen.CreateStudyModule.route) },
                navigateToFolderDetail = { folderId, folderName ->
                    navController.navigate(Screen.FolderDetail.createRoute(folderId, folderName))
                },
                navigateToClassDetail = { classId, className ->
                    navController.navigate(Screen.ClassDetail.createRoute(classId, className))
                },
                navigateToModuleDetail = { moduleId ->
                    navController.navigate(Screen.ModuleDetail.createRoute(moduleId))
                }
            )
        }

        // Create Folder Screen
        composable(Screen.CreateFolder.route) {
            CreateFolderScreen(
                navigateBack = { navController.popBackStack() }
            )
        }

        // Create Class Screen
        composable(Screen.CreateClass.route) {
            CreateClassScreen(
                navigateBack = { navController.popBackStack() }
            )
        }

        // Folder Detail Screen
        composable(
            route = Screen.FolderDetail.route,
            arguments = listOf(
                navArgument("folderId") { type = NavType.StringType },
                navArgument("folderName") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val folderId = backStackEntry.arguments?.getString("folderId") ?: ""
            val folderName = backStackEntry.arguments?.getString("folderName") ?: ""

            // FolderDetailScreen sẽ được triển khai sau
        }

        // Class Detail Screen
        composable(
            route = Screen.ClassDetail.route,
            arguments = listOf(
                navArgument("classId") { type = NavType.StringType },
                navArgument("className") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val classId = backStackEntry.arguments?.getString("classId") ?: ""
            val className = backStackEntry.arguments?.getString("className") ?: ""

            // ClassDetailScreen sẽ được triển khai sau
        }

        // Module Detail Screen
        composable(
            route = Screen.ModuleDetail.route,
            arguments = listOf(
                navArgument("moduleId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val moduleId = backStackEntry.arguments?.getString("moduleId") ?: ""

            ModuleDetailScreen(
                moduleId = moduleId,
                navigateBack = { navController.popBackStack() }
            )
        }

        // Create Study Module Screen
        composable(Screen.CreateStudyModule.route) {
            CreateStudyModuleScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}

/**
 * NavHost cho HomeScreen với Bottom Navigation
 */
@Composable
fun HomeNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    onNavigateToCreateFolder: () -> Unit,
    onNavigateToCreateClass: () -> Unit,
    onNavigateToCreateStudyModule: () -> Unit,
    onNavigateToFolderDetail: (String, String) -> Unit,
    onNavigateToClassDetail: (String, String) -> Unit,
    onNavigateToModuleDetail: (String) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        modifier = modifier
    ) {
        // Dashboard Screen
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                navigateToFolderDetail = onNavigateToFolderDetail,
                navigateToClassDetail = onNavigateToClassDetail,
                navigateToModuleDetail = onNavigateToModuleDetail
            )
        }

        // Library Screen
        composable(Screen.Library.route) {
            LibraryScreen(
                navigateToFolderDetail = onNavigateToFolderDetail,
                navigateToClassDetail = onNavigateToClassDetail,
                navigateToModuleDetail = onNavigateToModuleDetail,
                navigateToCreateFolder = onNavigateToCreateFolder,
                navigateToCreateClass = onNavigateToCreateClass,
                navigateToCreateStudyModule = onNavigateToCreateStudyModule
            )
        }

        // Screens cho các tab khác (Solutions, Profile) sẽ được triển khai tương tự
    }
}