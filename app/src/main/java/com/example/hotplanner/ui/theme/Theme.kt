package com.example.hotplanner.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
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

private val DarkColorScheme = darkColorScheme(
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
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val appColors   = if (darkTheme) DarkAppColors else AppColors()

    // Provide theme-aware colors to the entire composition tree
    CompositionLocalProvider(LocalAppColors provides appColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography  = Typography,
            content     = content
        )
    }
}