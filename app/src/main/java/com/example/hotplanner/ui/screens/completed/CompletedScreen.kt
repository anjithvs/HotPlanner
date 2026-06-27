package com.example.hotplanner.ui.screens.completed

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.hotplanner.ui.theme.Mocha
import com.example.hotplanner.ui.theme.Poppins
import com.example.hotplanner.viewmodel.TaskViewModel

@Composable
fun CompletedScreen(
    viewModel: TaskViewModel,
    padding: PaddingValues
) {
    Box(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Text("✅ Completed — Coming soon!", fontFamily = Poppins, color = Mocha)
    }
}