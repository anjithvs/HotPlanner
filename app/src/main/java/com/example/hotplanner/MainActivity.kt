package com.example.hotplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hotplanner.ui.navigation.AppNavigation
import com.example.hotplanner.ui.theme.TaskFlowTheme
import com.example.hotplanner.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Single ViewModel instance shared across all screens
            val viewModel: TaskViewModel = hiltViewModel()
            val darkMode by viewModel.darkMode.collectAsState()

            TaskFlowTheme(darkTheme = darkMode) {
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}