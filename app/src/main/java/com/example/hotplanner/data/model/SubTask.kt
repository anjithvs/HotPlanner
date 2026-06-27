package com.example.hotplanner.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "subtasks",
    foreignKeys = [
        ForeignKey(
            entity = Task::class,
            parentColumns = ["id"],
            childColumns = ["taskId"],
            onDelete = ForeignKey.CASCADE   // Deleting a task deletes its subtasks too
        )
    ],
    indices = [Index(value = ["taskId"])]   // Speeds up queries by taskId
)
data class SubTask(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val taskId: Long,           // Which task this subtask belongs to
    val text: String,
    val isDone: Boolean = false,
    val orderIndex: Int = 0,    // Position within the task's subtask list
    val notification: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)