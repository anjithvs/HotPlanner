package com.example.hotplanner.data.local

import androidx.room.*
import com.example.hotplanner.data.model.SubTask
import com.example.hotplanner.data.model.Task
import com.example.hotplanner.data.model.TaskWithSubTasks
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    // ── Active Tasks ─────────────────────────────────────────────────────────

    @Transaction
    @Query("SELECT * FROM tasks WHERE isCompleted = 0 ORDER BY orderIndex ASC, createdAt ASC")
    fun getActiveTasks(): Flow<List<TaskWithSubTasks>>

    // ── Completed Tasks ───────────────────────────────────────────────────────

    @Transaction
    @Query("SELECT * FROM tasks WHERE isCompleted = 1 ORDER BY createdAt DESC")
    fun getCompletedTasks(): Flow<List<TaskWithSubTasks>>

    // ── Single Task ───────────────────────────────────────────────────────────

    @Transaction
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getTaskById(taskId: Long): Flow<TaskWithSubTasks?>

    // ── Insert / Update / Delete Task ─────────────────────────────────────────

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("UPDATE tasks SET orderIndex = :orderIndex WHERE id = :taskId")
    suspend fun updateTaskOrder(taskId: Long, orderIndex: Int)

    // ── Insert / Update / Delete SubTask ──────────────────────────────────────

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubTask(subTask: SubTask): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubTasks(subTasks: List<SubTask>)

    @Update
    suspend fun updateSubTask(subTask: SubTask)

    @Update
    suspend fun updateSubTasks(subTasks: List<SubTask>)

    @Delete
    suspend fun deleteSubTask(subTask: SubTask)

    @Query("DELETE FROM subtasks WHERE taskId = :taskId")
    suspend fun deleteAllSubTasksForTask(taskId: Long)

    @Query("UPDATE subtasks SET orderIndex = :orderIndex WHERE id = :subTaskId")
    suspend fun updateSubTaskOrder(subTaskId: Long, orderIndex: Int)
}