package com.example.hotplanner.data.repository

import com.example.hotplanner.data.local.TaskDao
import com.example.hotplanner.data.model.SubTask
import com.example.hotplanner.data.model.Task
import com.example.hotplanner.data.model.TaskWithSubTasks
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val dao: TaskDao
) {
    // ── Observe Tasks (reactive — UI updates automatically) ───────────────────

    val activeTasks: Flow<List<TaskWithSubTasks>>    = dao.getActiveTasks()
    val completedTasks: Flow<List<TaskWithSubTasks>> = dao.getCompletedTasks()

    fun getTaskById(id: Long): Flow<TaskWithSubTasks?> = dao.getTaskById(id)

    // ── Task Operations ───────────────────────────────────────────────────────

    suspend fun insertTask(task: Task): Long = dao.insertTask(task)

    suspend fun updateTask(task: Task) = dao.updateTask(task)

    suspend fun deleteTask(task: Task) = dao.deleteTask(task)

    // Mark task and all subtasks as complete
    suspend fun completeTask(taskWithSubTasks: TaskWithSubTasks) {
        val completedSubs = taskWithSubTasks.subTasks.map { it.copy(isDone = true) }
        dao.updateSubTasks(completedSubs)
        dao.updateTask(taskWithSubTasks.task.copy(isCompleted = true))
    }

    // Restore: uncheck only the last subtask, mark task active again
    suspend fun restoreTask(taskWithSubTasks: TaskWithSubTasks) {
        val subTasks = taskWithSubTasks.subTasks.sortedBy { it.orderIndex }
        if (subTasks.isNotEmpty()) {
            dao.updateSubTask(subTasks.last().copy(isDone = false))
        }
        dao.updateTask(taskWithSubTasks.task.copy(isCompleted = false))
    }

    // Reorder tasks after drag-and-drop
    suspend fun reorderTasks(reorderedTasks: List<TaskWithSubTasks>) {
        reorderedTasks.forEachIndexed { index, item ->
            dao.updateTaskOrder(item.task.id, index)
        }
    }

    // ── SubTask Operations ────────────────────────────────────────────────────

    suspend fun updateSubTask(subTask: SubTask) = dao.updateSubTask(subTask)

    suspend fun deleteSubTask(subTask: SubTask) = dao.deleteSubTask(subTask)

    // Inserts a subtask at a specific position, shifting others down
    suspend fun insertSubTaskAtPosition(
        taskId: Long,
        text: String,
        afterIndex: Int,
        hasNotification: Boolean,
        currentSubTasks: List<SubTask>
    ) {
        val insertAt = if (afterIndex == -1) 0 else afterIndex + 1

        // Shift all subtasks at or after insertAt down by 1
        val shifted = currentSubTasks.mapIndexed { index, subTask ->
            if (index >= insertAt) subTask.copy(orderIndex = index + 1)
            else subTask.copy(orderIndex = index)
        }
        dao.updateSubTasks(shifted)

        // Insert the new subtask at the correct position
        dao.insertSubTask(
            SubTask(
                taskId = taskId,
                text = text,
                isDone = false,
                orderIndex = insertAt,
                notification = hasNotification
            )
        )
    }

    // Reorder subtasks after drag-and-drop
    suspend fun reorderSubTasks(reorderedSubTasks: List<SubTask>) {
        reorderedSubTasks.forEachIndexed { index, subTask ->
            dao.updateSubTaskOrder(subTask.id, index)
        }
    }
}