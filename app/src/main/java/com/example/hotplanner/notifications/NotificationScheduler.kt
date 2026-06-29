package com.example.hotplanner.notifications

import android.content.Context
import android.util.Log
import androidx.work.*
import com.example.hotplanner.data.model.Task
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val workManager = WorkManager.getInstance(context)

    // ── Real scheduling ───────────────────────────────────────────────────────

    fun scheduleTask(task: Task) {
        if (!task.notification) {
            Log.d(TAG, "Skipped '${task.title}' — notification is off")
            return
        }
        if (task.dueDate.isEmpty()) {
            Log.d(TAG, "Skipped '${task.title}' — no due date set")
            return
        }

        val delayMs = delayUntil9AM(task.dueDate)
        if (delayMs <= 0) {
            Log.d(TAG, "Skipped '${task.title}' — due date already passed ($delayMs ms)")
            return
        }

        Log.d(TAG, "Scheduling '${task.title}' → fires in ${delayMs / 1000}s")

        workManager.enqueueUniqueWork(
            workName(task.id),
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequestBuilder<TaskReminderWorker>()
                .setInitialDelay(delayMs, TimeUnit.MILLISECONDS)
                .setInputData(
                    workDataOf(
                        TaskReminderWorker.KEY_TITLE to "📋 ${task.title}",
                        TaskReminderWorker.KEY_BODY  to "Don't forget to complete this task today!"
                    )
                )
                .build()
        )
    }

    fun cancelTask(taskId: Long) {
        Log.d(TAG, "Cancelled notification for task $taskId")
        workManager.cancelUniqueWork(workName(taskId))
    }

    fun rescheduleTask(task: Task) {
        cancelTask(task.id)
        scheduleTask(task)
    }

    // ── Test function — bypasses ALL conditions, fires in 5 seconds ───────────

    fun scheduleTestNotification() {
        Log.d(TAG, "Test notification enqueued — fires in 5 seconds ⏱️")
        workManager.enqueue(
            OneTimeWorkRequestBuilder<TaskReminderWorker>()
                .setInitialDelay(5, TimeUnit.SECONDS)
                .setInputData(
                    workDataOf(
                        TaskReminderWorker.KEY_TITLE to "📋 Test Reminder",
                        TaskReminderWorker.KEY_BODY  to "Notifications are working! ✅"
                    )
                )
                .build()
        )
    }

    private fun workName(taskId: Long) = "task_reminder_$taskId"

    private fun delayUntil9AM(dueDateStr: String): Long {
        return try {
            val parts  = dueDateStr.split("-")
            val target = Calendar.getInstance().apply {
                set(Calendar.YEAR,         parts[0].toInt())
                set(Calendar.MONTH,        parts[1].toInt() - 1)
                set(Calendar.DAY_OF_MONTH, parts[2].toInt())
                set(Calendar.HOUR_OF_DAY,  9)
                set(Calendar.MINUTE,       0)
                set(Calendar.SECOND,       0)
                set(Calendar.MILLISECOND,  0)
            }
            target.timeInMillis - System.currentTimeMillis()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse date '$dueDateStr'", e)
            -1L
        }
    }

    companion object {
        const val TAG = "NotifScheduler"
    }
}