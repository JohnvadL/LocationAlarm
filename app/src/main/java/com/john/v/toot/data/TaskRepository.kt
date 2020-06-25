package com.john.v.toot.data

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData


internal class TaskRepository
    (context: Context) {

    private val taskDao: TaskDao

    val allTasks: LiveData<List<Task>>

    init {
        val db = TaskDatabase.getDatabase(context)
        taskDao = db!!.taskDao()
        allTasks = taskDao.getAllTasksLive()
    }


    fun insert(task: Task) {
        TaskDatabase.databaseWriteExecutor.execute {
            taskDao.insertAll(task)
        }
    }
}
