package com.john.v.toot.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.lifecycle.LiveData
import androidx.room.Update


@Dao
interface TaskDao{

    @Query("SELECT * FROM task")
    fun getAllTasks(): List<Task>

    @Insert
    fun insertAll(vararg  tasks:Task )

    @Query("SELECT * from task ORDER BY name ASC")
    fun getAllTasksLive(): LiveData<List<Task>>


    @Update
    fun updateTasks(vararg tasks:Task )

}