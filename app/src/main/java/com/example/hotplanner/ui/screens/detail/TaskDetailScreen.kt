package com.example.hotplanner.ui.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.hotplanner.ui.theme.Mocha
import com.example.hotplanner.ui.theme.Poppins
import com.example.hotplanner.viewmodel.TaskViewModel

@Composable
fun TaskDetailScreen(
    taskId: Long,
    viewModel: TaskViewModel,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("📋 Task Detail — Coming soon! (ID: $taskId)", fontFamily = Poppins, color = Mocha)
    }
}