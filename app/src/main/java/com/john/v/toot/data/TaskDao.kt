package com.john.v.toot.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TaskDao{

    @Query("SELECT * FROM task")
    fun getAllTasks(): List<Task>

    @Insert
    fun insertAll(vararg  tasks:Task )

}