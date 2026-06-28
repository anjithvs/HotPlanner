package com.example.hotplanner.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Property names intentionally match the Color.kt constants.
// Shadowing them per-composable makes existing code work with zero other changes.
@Suppress("PropertyName")
data class AppColors(
    val CreamBg:     Color = Color(0xFFFAF6F1),
    val CreamCard:   Color = Color(0xFFFFF8F3),
    val CreamDark:   Color = Color(0xFFF0E6D8),
    val CoffeeDark:  Color = Color(0xFF3D2314),
    val Mocha:       Color = Color(0xFF9C7B5A),
    val BorderColor: Color = Color(0xFFE8D5BE),
)

val DarkAppColors = AppColors(
    CreamBg     = Color(0xFF1C120A),                     // Very dark brown
    CreamCard   = Color(0xFF2E1E12),                     // Slightly lighter dark
    CreamDark   = Color(0xFF2A1508),                     // Deep coffee
    CoffeeDark  = Color(0xFFFAF6F1),                     // Light text on dark bg
    Mocha       = Color(0xFFC9A87C).copy(alpha = 0.80f), // Soft latte tone
    BorderColor = Color(0xFF6F4E37).copy(alpha = 0.55f), // Coffee-tinted border
)

val LocalAppColors = staticCompositionLocalOf { AppColors() }