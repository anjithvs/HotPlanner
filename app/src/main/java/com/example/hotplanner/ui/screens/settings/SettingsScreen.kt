package com.example.hotplanner.ui.screens.settings

import androidx.compose.foundation.background
import com.example.hotplanner.ui.theme.LocalAppColors
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hotplanner.ui.theme.*
import com.example.hotplanner.viewmodel.TaskViewModel

@Composable
fun SettingsScreen(
    viewModel: TaskViewModel,
    padding: PaddingValues
) {
    val (CreamBg, CreamCard, CreamDark, CoffeeDark, Mocha, BorderColor) = LocalAppColors.current
    val notifications by viewModel.notifications.collectAsState()
    val haptics       by viewModel.haptics.collectAsState()
    val darkMode      by viewModel.darkMode.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .verticalScroll(rememberScrollState())
    ) {
        // ── Header ────────────────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CreamCard)
                .padding(horizontal = 22.dp, vertical = 18.dp)
        ) {
            Text(
                "Settings ⚙️",
                fontFamily = Poppins, fontWeight = FontWeight.ExtraBold,
                fontSize = 26.sp, color = CoffeeDark
            )
            Text(
                "Manage your preferences",
                fontFamily = Poppins, fontSize = 13.sp, color = Mocha
            )
        }

        Spacer(Modifier.height(16.dp))

        // ── Toggle rows ───────────────────────────────────────────────────────
        Card(
            modifier  = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape     = RoundedCornerShape(20.dp),
            colors    = CardDefaults.cardColors(containerColor = CreamCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column {
                SettingRow(
                    icon        = "🔔",
                    title       = "Notifications",
                    desc        = "Task & subtask reminders",
                    checked     = notifications,
                    onToggle    = { viewModel.setNotifications(!notifications) },
                    showDivider = true
                )
                SettingRow(
                    icon        = "📳",
                    title       = "Haptics",
                    desc        = "Vibration feedback on actions",
                    checked     = haptics,
                    onToggle    = { viewModel.setHaptics(!haptics) },
                    showDivider = true
                )
                SettingRow(
                    icon        = "🌙",
                    title       = "Dark Mode",
                    desc        = "Easy on the eyes at night",
                    checked     = darkMode,
                    onToggle    = { viewModel.setDarkMode(!darkMode) },
                    showDivider = false
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // ── App info ──────────────────────────────────────────────────────────
        Card(
            modifier  = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape     = RoundedCornerShape(20.dp),
            colors    = CardDefaults.cardColors(containerColor = CreamCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier             = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 26.dp),
                horizontalAlignment  = Alignment.CenterHorizontally
            ) {
                Text("☕", fontSize = 38.sp)
                Spacer(Modifier.height(10.dp))
                Text(
                    "HotPlanner",
                    fontFamily = Poppins, fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp, color = CoffeeDark
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Version 1.0.0 · Built with care",
                    fontFamily = Poppins, fontSize = 12.sp, color = Mocha
                )
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}

// ── Setting Row ────────────────────────────────────────────────────────────────

@Composable
private fun SettingRow(
    icon: String,
    title: String,
    desc: String,
    checked: Boolean,
    onToggle: () -> Unit,
    showDivider: Boolean
) {
    val (CreamBg, CreamCard, CreamDark, CoffeeDark, Mocha, BorderColor) = LocalAppColors.current
    Column {
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Icon + labels
            Row(
                modifier              = Modifier.weight(1f),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier         = Modifier
                        .size(44.dp)
                        .background(Latte.copy(alpha = 0.28f), RoundedCornerShape(13.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(icon, fontSize = 20.sp)
                }
                Column {
                    Text(
                        title, fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = CoffeeDark
                    )
                    Text(
                        desc, fontFamily = Poppins,
                        fontSize = 12.sp, color = Mocha,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            // Toggle
            Switch(
                checked         = checked,
                onCheckedChange = { onToggle() },
                colors          = SwitchDefaults.colors(
                    checkedThumbColor   = Color.White,
                    checkedTrackColor   = CoffeeBrown,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = BorderColor
                )
            )
        }

        if (showDivider) {
            HorizontalDivider(
                modifier  = Modifier.padding(horizontal = 18.dp),
                color     = BorderColor,
                thickness = 0.8.dp
            )
        }
    }
}