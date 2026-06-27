package com.example.hotplanner.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.hotplanner.ui.screens.detail.TaskDetailScreen
import com.example.hotplanner.ui.screens.main.MainScreen
import com.example.hotplanner.ui.screens.splash.SplashScreen
import com.example.hotplanner.viewmodel.TaskViewModel

@Composable
fun AppNavigation(viewModel: TaskViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController    = navController,
        startDestination = Screen.Splash.route
    ) {
        // ── Splash ─────────────────────────────────────────────────────────
        composable(Screen.Splash.route) {
            SplashScreen {
                navController.navigate(Screen.Main.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            }
        }

        // ── Main (Home / Completed / Settings tabs) ─────────────────────────
        composable(Screen.Main.route) {
            MainScreen(
                viewModel          = viewModel,
                onNavigateToDetail = { taskId ->
                    navController.navigate(Screen.Detail.createRoute(taskId))
                }
            )
        }

        // ── Task Detail ─────────────────────────────────────────────────────
        composable(
            route     = Screen.Detail.route,
            arguments = listOf(navArgument("taskId") { type = NavType.LongType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getLong("taskId") ?: return@composable
            TaskDetailScreen(
                taskId    = taskId,
                viewModel = viewModel,
                onBack    = { navController.popBackStack() }
            )
        }
    }
}