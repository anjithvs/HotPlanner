package com.example.hotplanner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotplanner.data.model.SubTask
import com.example.hotplanner.data.model.Task
import com.example.hotplanner.data.model.TaskWithSubTasks
import com.example.hotplanner.data.preferences.SettingsRepository
import com.example.hotplanner.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository,
    private val settings: SettingsRepository
) : ViewModel() {

    // ── Task Streams (auto-update UI whenever DB changes) ─────────────────────

    val activeTasks: StateFlow<List<TaskWithSubTasks>> = repository.activeTasks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val completedTasks: StateFlow<List<TaskWithSubTasks>> = repository.completedTasks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    // ── Settings (exposed directly from SettingsRepository) ───────────────────

    val notifications: StateFlow<Boolean> = settings.notifications
    val haptics: StateFlow<Boolean>        = settings.haptics
    val darkMode: StateFlow<Boolean>       = settings.darkMode

    // ── Celebration UI State ──────────────────────────────────────────────────

    private val _showConfetti    = MutableStateFlow(false)
    val showConfetti: StateFlow<Boolean> = _showConfetti.asStateFlow()

    private val _showCelebration = MutableStateFlow(false)
    val showCelebration: StateFlow<Boolean> = _showCelebration.asStateFlow()

    // ═══════════════════════════════════════════════════════════════════════════
    // TASK ACTIONS
    // ═══════════════════════════════════════════════════════════════════════════

    fun addTask(
        title: String,
        category: String,
        priority: String,
        dueDate: String,
        notification: Boolean
    ) {
        viewModelScope.launch {
            repository.insertTask(
                Task(
                    title        = title,
                    category     = category,
                    priority     = priority,
                    dueDate      = dueDate,
                    notification = notification,
                    orderIndex   = activeTasks.value.size   // Add to end of list
                )
            )
        }
    }

    // Mark task and ALL its subtasks as done → triggers celebration
    fun completeTask(taskWithSubTasks: TaskWithSubTasks) {
        viewModelScope.launch {
            repository.completeTask(taskWithSubTasks)
            triggerCelebration()
        }
    }

    // Move task back to active — last subtask becomes unchecked
    fun restoreTask(taskWithSubTasks: TaskWithSubTasks) {
        viewModelScope.launch {
            repository.restoreTask(taskWithSubTasks)
        }
    }

    fun deleteTask(taskWithSubTasks: TaskWithSubTasks) {
        viewModelScope.launch {
            repository.deleteTask(taskWithSubTasks.task)
        }
    }

    // Called after user drag-and-drops tasks to reorder them
    fun reorderTasks(reorderedList: List<TaskWithSubTasks>) {
        viewModelScope.launch {
            repository.reorderTasks(reorderedList)
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // SUBTASK ACTIONS
    // ═══════════════════════════════════════════════════════════════════════════

    // Check or uncheck a subtask.
    // If checking the LAST unchecked subtask → auto-complete the whole task.
    fun toggleSubTask(taskWithSubTasks: TaskWithSubTasks, subTask: SubTask) {
        viewModelScope.launch {
            val isNowDone = !subTask.isDone

            // Update the subtask in the database
            repository.updateSubTask(subTask.copy(isDone = isNowDone))

            // Only check for completion when CHECKING (not unchecking)
            if (isNowDone) {
                val allWillBeDone = taskWithSubTasks.subTasks
                    .map { if (it.id == subTask.id) it.copy(isDone = true) else it }
                    .all { it.isDone }

                if (allWillBeDone) {
                    delay(350)  // Wait for the checkbox animation to finish
                    repository.updateTask(taskWithSubTasks.task.copy(isCompleted = true))
                    triggerCelebration()
                }
            }
        }
    }

    // Insert a subtask at a specific position within a task
    fun addSubTask(
        taskId: Long,
        text: String,
        afterIndex: Int,
        hasNotification: Boolean,
        currentSubTasks: List<SubTask>
    ) {
        viewModelScope.launch {
            repository.insertSubTaskAtPosition(
                taskId          = taskId,
                text            = text,
                afterIndex      = afterIndex,
                hasNotification = hasNotification,
                currentSubTasks = currentSubTasks.sortedBy { it.orderIndex }
            )
        }
    }

    fun deleteSubTask(subTask: SubTask) {
        viewModelScope.launch {
            repository.deleteSubTask(subTask)
        }
    }

    // Called after user drag-and-drops subtasks to reorder them
    fun reorderSubTasks(reorderedList: List<SubTask>) {
        viewModelScope.launch {
            repository.reorderSubTasks(reorderedList)
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // SETTINGS ACTIONS
    // ═══════════════════════════════════════════════════════════════════════════

    fun setNotifications(enabled: Boolean) = settings.setNotifications(enabled)
    fun setHaptics(enabled: Boolean)        = settings.setHaptics(enabled)
    fun setDarkMode(enabled: Boolean)       = settings.setDarkMode(enabled)

    // ═══════════════════════════════════════════════════════════════════════════
    // CELEBRATION
    // ═══════════════════════════════════════════════════════════════════════════

    private fun triggerCelebration() {
        viewModelScope.launch {
            _showConfetti.value    = true
            _showCelebration.value = true
            delay(2800)             // Banner shows for 2.8 seconds
            _showCelebration.value = false
            // Confetti dismissed separately by the UI after animation ends
        }
    }

    fun dismissConfetti() {
        _showConfetti.value = false
    }
}