package com.example.hotplanner.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hotplanner.ui.components.CelebrationBanner
import com.example.hotplanner.ui.components.ConfettiOverlay
import com.example.hotplanner.ui.theme.LocalAppColors
import com.example.hotplanner.ui.screens.completed.CompletedScreen
import com.example.hotplanner.ui.screens.home.HomeScreen
import com.example.hotplanner.ui.screens.settings.SettingsScreen
import com.example.hotplanner.ui.theme.*
import com.example.hotplanner.viewmodel.TaskViewModel

@Composable
fun MainScreen(
    viewModel: TaskViewModel,
    onNavigateToDetail: (Long) -> Unit
) {
    val (CreamBg, CreamCard, CreamDark, CoffeeDark, Mocha, BorderColor) = LocalAppColors.current
    var selectedTab by remember { mutableIntStateOf(0) }

    val showConfetti    by viewModel.showConfetti.collectAsState()
    val showCelebration by viewModel.showCelebration.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        Scaffold(
            containerColor = CreamBg,
            bottomBar = {
                HotPlannerBottomBar(
                    selectedTab   = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
            }
        ) { padding ->
            when (selectedTab) {
                0 -> HomeScreen(
                    viewModel          = viewModel,
                    padding            = padding,
                    onNavigateToDetail = onNavigateToDetail
                )
                1 -> CompletedScreen(viewModel = viewModel, padding = padding)
                2 -> SettingsScreen(viewModel  = viewModel, padding = padding)
            }
        }

        // ── Global overlays (appear above all tabs) ───────────────────────
        ConfettiOverlay(visible = showConfetti) {
            viewModel.dismissConfetti()
        }
        CelebrationBanner(visible = showCelebration)
    }
}

@Composable
private fun HotPlannerBottomBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    val (CreamBg, CreamCard, CreamDark, CoffeeDark, Mocha, BorderColor) = LocalAppColors.current
    NavigationBar(
        containerColor = CreamCard,
        tonalElevation = 0.dp
    ) {
        listOf(
            Triple("🏠", "Home",     0),
            Triple("✅", "Done",     1),
            Triple("⚙️", "Settings", 2)
        ).forEach { (emoji, label, index) ->
            NavigationBarItem(
                selected = selectedTab == index,
                onClick  = { onTabSelected(index) },
                icon     = { Text(emoji, fontSize = 22.sp) },
                label    = {
                    Text(
                        text       = label,
                        fontFamily = Poppins,
                        fontSize   = 11.sp,
                        fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedTextColor   = CoffeeBrown,
                    unselectedTextColor = Mocha,
                    indicatorColor      = CreamDark
                )
            )
        }
    }
}