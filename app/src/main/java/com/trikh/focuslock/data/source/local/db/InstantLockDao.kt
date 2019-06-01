package com.trikh.focuslock.data.source.local.db

import androidx.room.Insert
import androidx.room.Query
import com.trikh.focuslock.data.model.InstantLockSchedule

interface InstantLockDao{

    @Insert
    fun insertSchedule(schedule : InstantLockSchedule)

    @Query("DELETE FROM instant_lock")
    fun deleteSchedule()
}