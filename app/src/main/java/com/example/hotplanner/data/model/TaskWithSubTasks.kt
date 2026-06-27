package com.example.hotplanner.data.model

import androidx.room.Embedded
import androidx.room.Relation

// This combines a Task with all its SubTasks in one object.
// Room handles the join automatically using @Relation.
data class TaskWithSubTasks(
    @Embedded
    val task: Task,

    @Relation(
        parentColumn = "id",
        entityColumn = "taskId"
    )
    val subTasks: List<SubTask>
) {
    // Convenience computed properties
    val progress: Float
        get() = if (subTasks.isEmpty()) 0f
        else subTasks.count { it.isDone }.toFloat() / subTasks.size

    val progressPercent: Int
        get() = (progress * 100).toInt()

    val isAllDone: Boolean
        get() = subTasks.isNotEmpty() && subTasks.all { it.isDone }
}