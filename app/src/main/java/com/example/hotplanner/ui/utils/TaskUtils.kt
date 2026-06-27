package com.example.hotplanner.ui.utils

import androidx.compose.ui.graphics.Color
import com.example.hotplanner.ui.theme.PriorityHigh
import com.example.hotplanner.ui.theme.PriorityLow
import com.example.hotplanner.ui.theme.PriorityMedium

fun priorityColor(priority: String): Color = when (priority.uppercase()) {
    "HIGH"   -> PriorityHigh
    "MEDIUM" -> PriorityMedium
    else     -> PriorityLow
}

fun priorityLabel(priority: String): String = when (priority.uppercase()) {
    "HIGH"   -> "High"
    "MEDIUM" -> "Medium"
    else     -> "Low"
}

fun formatDueDate(dateStr: String): String {
    if (dateStr.isEmpty()) return ""
    return try {
        val parts  = dateStr.split("-")
        if (parts.size != 3) return dateStr
        val months = listOf("Jan","Feb","Mar","Apr","May","Jun",
            "Jul","Aug","Sep","Oct","Nov","Dec")
        "${months[parts[1].toInt() - 1]} ${parts[2].toInt()}"
    } catch (e: Exception) { dateStr }
}