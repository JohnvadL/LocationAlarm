package com.john.v.toot.data

import androidx.lifecycle.LiveData
import android.app.Application
import androidx.lifecycle.AndroidViewModel


class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val mRepository: TaskRepository = TaskRepository(application.applicationContext)

    public var allTasks: LiveData<List<Task>>

    init {
        allTasks = mRepository.allTasks
    }

    fun insert(task: Task) {
        mRepository.insert(task)
    }


}