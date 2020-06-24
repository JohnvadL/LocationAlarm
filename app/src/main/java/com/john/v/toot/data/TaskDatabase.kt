package com.john.v.toot.data


import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Database

import com.john.v.toot.data.Task
import com.john.v.toot.data.TaskDao
import java.util.concurrent.Executors


@Database(entities = [Task::class], version = 1, exportSchema = false)

abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {

        @Volatile
        private var INSTANCE: TaskDatabase? = null
        private val NUMBER_OF_THREADS = 4
        internal val databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

        internal fun getDatabase(context: Context): TaskDatabase? {
            if (INSTANCE == null) {
                synchronized(TaskDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            TaskDatabase::class.java, "word_database"
                        )
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}