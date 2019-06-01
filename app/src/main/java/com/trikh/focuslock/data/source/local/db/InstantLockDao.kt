package com.trikh.focuslock.data.source.local.db

import androidx.room.Insert
import androidx.room.Query
import com.trikh.focuslock.data.model.InstantLock

interface InstantLockDao{

    @Insert
    fun insertSchedule(schedule : InstantLock)

    @Query("DELETE FROM instant_lock")
    fun deleteSchedule()
}