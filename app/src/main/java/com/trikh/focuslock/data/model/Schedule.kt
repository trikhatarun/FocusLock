package com.trikh.focuslock.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "schedule")
data class Schedule(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "start_time") val startTime: Calendar,
    @ColumnInfo(name = "end_time") val endTime: Calendar,
    val level: Int?,
    val active: Boolean?
)