package com.example.hotplanner.ui.theme   // ← change yourname to your package name

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.hotplanner.R             // ← change yourname to your package name

val Poppins = FontFamily(
    Font(R.font.poppins_light,     FontWeight.Light),
    Font(R.font.poppins_regular,   FontWeight.Normal),
    Font(R.font.poppins_medium,    FontWeight.Medium),
    Font(R.font.poppins_semibold,  FontWeight.SemiBold),
    Font(R.font.poppins_bold,      FontWeight.Bold),
    Font(R.font.poppins_extrabold, FontWeight.ExtraBold),
)

val Typography = Typography(
    displayLarge  = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.ExtraBold, fontSize = 32.sp, lineHeight = 40.sp),
    displayMedium = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.Bold,      fontSize = 28.sp, lineHeight = 36.sp),
    displaySmall  = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.Bold,      fontSize = 24.sp, lineHeight = 32.sp),

    headlineLarge  = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.Bold,      fontSize = 22.sp, lineHeight = 30.sp),
    headlineMedium = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.SemiBold,  fontSize = 20.sp, lineHeight = 28.sp),
    headlineSmall  = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.SemiBold,  fontSize = 18.sp, lineHeight = 26.sp),

    titleLarge  = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 17.sp, lineHeight = 24.sp),
    titleMedium = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, lineHeight = 22.sp),
    titleSmall  = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.Medium,   fontSize = 14.sp, lineHeight = 20.sp),

    bodyLarge  = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.Normal, fontSize = 15.sp, lineHeight = 22.sp),
    bodyMedium = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 21.sp),
    bodySmall  = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 19.sp),

    labelLarge  = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, lineHeight = 17.sp),
    labelMedium = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.Medium,   fontSize = 11.sp, lineHeight = 15.sp),
    labelSmall  = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.Medium,   fontSize = 10.sp, lineHeight = 14.sp),
)