package com.example.hotplanner.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Main   : Screen("main")
    object Detail : Screen("task_detail/{taskId}") {
        fun createRoute(taskId: Long) = "task_detail/$taskId"
    }
}