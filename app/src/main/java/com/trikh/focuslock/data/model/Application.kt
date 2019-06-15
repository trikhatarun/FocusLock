package com.trikh.focuslock.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
// Not yet used may be used in future
@Entity(
    tableName = "application", foreignKeys = [ForeignKey(
        entity = Schedule::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("scheduleId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Application(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val scheduleId: Int,
    val packageName: String
)