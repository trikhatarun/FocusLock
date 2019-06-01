package com.trikh.focuslock.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "instant_lock")
class InstantLock(
    @PrimaryKey val id: Int,
    val endTime: Calendar,
    val blockedApps: List<String>
)