package com.example.hotplanner.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val title: String,
    val category: String,
    val priority: String,       // "HIGH", "MEDIUM", or "LOW"
    val dueDate: String = "",   // Format: "YYYY-MM-DD" or empty string
    val notification: Boolean = false,
    val isCompleted: Boolean = false,
    val orderIndex: Int = 0,    // Used for drag-to-reorder
    val createdAt: Long = System.currentTimeMillis()
)