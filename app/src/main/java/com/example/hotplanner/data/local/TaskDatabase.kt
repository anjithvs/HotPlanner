package com.example.hotplanner.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.hotplanner.data.model.SubTask
import com.example.hotplanner.data.model.Task

@Database(
    entities = [Task::class, SubTask::class],
    version = 1,
    exportSchema = false
)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}