package com.example.hotplanner.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.hotplanner.ui.theme.Mocha
import com.example.hotplanner.ui.theme.Poppins
import com.example.hotplanner.viewmodel.TaskViewModel

@Composable
fun HomeScreen(
    viewModel: TaskViewModel,
    padding: PaddingValues,
    onNavigateToDetail: (Long) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Text("🏠 Home — Building next step!", fontFamily = Poppins, color = Mocha)
    }
}