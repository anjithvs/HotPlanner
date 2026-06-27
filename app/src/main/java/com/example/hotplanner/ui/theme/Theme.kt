package com.example.hotplanner.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary              = CoffeeBrown,
    onPrimary            = Color.White,
    primaryContainer     = Latte,
    onPrimaryContainer   = CoffeeDark,
    secondary            = Mocha,
    onSecondary          = Color.White,
    secondaryContainer   = CreamDark,
    onSecondaryContainer = CoffeeDark,
    background           = CreamBg,
    onBackground         = CoffeeDark,
    surface              = CreamCard,
    onSurface            = CoffeeDark,
    surfaceVariant       = CreamDark,
    onSurfaceVariant     = Mocha,
    outline              = BorderColor,
    error                = PriorityHigh,
    onError              = Color.White,
)

private val DarkColors = darkColorScheme(
    primary              = Latte,
    onPrimary            = CoffeeDark,
    primaryContainer     = CoffeeBrown,
    onPrimaryContainer   = CreamBg,
    secondary            = Mocha,
    onSecondary          = CoffeeDark,
    secondaryContainer   = CoffeeDeep,
    onSecondaryContainer = Latte,
    background           = SurfaceDark,
    onBackground         = CreamBg,
    surface              = SurfaceVariantDark,
    onSurface            = CreamBg,
    surfaceVariant       = CoffeeDeep,
    onSurfaceVariant     = Latte,
    outline              = CoffeeBrown,
    error                = PriorityHigh,
    onError              = Color.White,
)

@Composable
fun TaskFlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}