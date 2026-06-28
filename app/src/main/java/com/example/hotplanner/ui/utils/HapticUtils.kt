package com.example.hotplanner.ui.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

enum class HapticType {
    TICK,     // Very light  — every subtask check/uncheck
    CONFIRM,  // Medium      — task complete, restore to active
    WARNING   // Firm        — delete actions
}

/**
 * Returns a lambda you call to trigger vibration.
 * Automatically respects the user's haptics setting.
 * Usage: val haptic = rememberAppHaptic(hapticsEnabled)
 *        haptic(HapticType.TICK)
 */
@Composable
fun rememberAppHaptic(hapticsEnabled: Boolean): (HapticType) -> Unit {
    val context = LocalContext.current
    return remember(hapticsEnabled) {
        { type ->
            if (hapticsEnabled) {
                val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE)
                            as VibratorManager).defaultVibrator
                } else {
                    @Suppress("DEPRECATION")
                    context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                }
                val effect = when (type) {
                    HapticType.TICK    -> VibrationEffect.createOneShot(28,  55)
                    HapticType.CONFIRM -> VibrationEffect.createOneShot(48, 100)
                    HapticType.WARNING -> VibrationEffect.createOneShot(65, 128)
                }
                vibrator.vibrate(effect)
            }
        }
    }
}