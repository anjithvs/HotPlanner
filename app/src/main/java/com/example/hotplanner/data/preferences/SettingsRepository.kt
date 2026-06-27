package com.example.hotplanner.data.preferences

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext context: Context
) {
    // SharedPreferences — persists settings across app restarts
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // ── Notifications ──────────────────────────────────────────────────────────
    private val _notifications = MutableStateFlow(prefs.getBoolean(KEY_NOTIFICATIONS, true))
    val notifications: StateFlow<Boolean> = _notifications.asStateFlow()

    fun setNotifications(enabled: Boolean) {
        _notifications.value = enabled
        prefs.edit().putBoolean(KEY_NOTIFICATIONS, enabled).apply()
    }

    // ── Haptics ────────────────────────────────────────────────────────────────
    private val _haptics = MutableStateFlow(prefs.getBoolean(KEY_HAPTICS, true))
    val haptics: StateFlow<Boolean> = _haptics.asStateFlow()

    fun setHaptics(enabled: Boolean) {
        _haptics.value = enabled
        prefs.edit().putBoolean(KEY_HAPTICS, enabled).apply()
    }

    // ── Dark Mode ──────────────────────────────────────────────────────────────
    private val _darkMode = MutableStateFlow(prefs.getBoolean(KEY_DARK_MODE, false))
    val darkMode: StateFlow<Boolean> = _darkMode.asStateFlow()

    fun setDarkMode(enabled: Boolean) {
        _darkMode.value = enabled
        prefs.edit().putBoolean(KEY_DARK_MODE, enabled).apply()
    }

    companion object {
        private const val PREFS_NAME       = "hotplanner_settings"
        private const val KEY_NOTIFICATIONS = "notifications"
        private const val KEY_HAPTICS       = "haptics"
        private const val KEY_DARK_MODE     = "dark_mode"
    }
}