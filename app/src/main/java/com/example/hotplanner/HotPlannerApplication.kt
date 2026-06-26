package com.example.hotplanner   // ← change yourname to your package name

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HotPlannerApplication : Application()