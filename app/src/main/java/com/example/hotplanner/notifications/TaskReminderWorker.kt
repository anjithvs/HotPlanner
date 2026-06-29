package com.example.hotplanner.notifications

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class TaskReminderWorker(
    private val context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        Log.d(TAG, "doWork() triggered ✅")
        return try {
            val title = inputData.getString(KEY_TITLE) ?: run {
                Log.e(TAG, "No title in input — failing")
                return Result.failure()
            }
            val body = inputData.getString(KEY_BODY) ?: "Time to work on your task!"

            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(android.R.drawable.ic_popup_reminder)  // ← safe system icon
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)
                .build()

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager

            // Double-check channel exists before notifying
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                if (manager.getNotificationChannel(CHANNEL_ID) == null) {
                    Log.e(TAG, "Channel '$CHANNEL_ID' does not exist!")
                    createChannelIfNeeded(manager)
                }
            }

            manager.notify(System.currentTimeMillis().toInt(), notification)
            Log.d(TAG, "Notification delivered: $title ✅")
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Notification failed: ${e.message}", e)
            Result.failure()
        }
    }

    private fun createChannelIfNeeded(manager: NotificationManager) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = android.app.NotificationChannel(
                CHANNEL_ID, "Task Reminders",
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
            Log.d(TAG, "Channel created inside Worker as fallback")
        }
    }

    companion object {
        const val TAG        = "TaskReminderWorker"
        const val CHANNEL_ID = "taskflow_reminders"
        const val KEY_TITLE  = "task_title"
        const val KEY_BODY   = "task_body"
    }
}