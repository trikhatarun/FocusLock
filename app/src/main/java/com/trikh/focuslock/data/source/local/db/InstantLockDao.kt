package com.trikh.focuslock.data.source.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.trikh.focuslock.data.model.InstantLockSchedule
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface InstantLockDao{

    @Insert
    fun insertSchedule(schedule : InstantLockSchedule)

    @Query("DELETE FROM instant_lock")
    fun deleteSchedule()

    @Query("SELECT * FROM instant_lock")
    fun getSchedule() : Observable<InstantLockSchedule>
}