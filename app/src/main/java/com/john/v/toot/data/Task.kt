package com.john.v.toot.data

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Data needed to store
// Name
// time
// low_batter
// power_off
// Message

@Entity(tableName = "task")
data class Task (
    @PrimaryKey val name:String,
    @ColumnInfo val time:Double,
    @ColumnInfo val lowBattery:Boolean,
    @ColumnInfo val powerOff: Boolean,
    @ColumnInfo val customMessage: String
)

