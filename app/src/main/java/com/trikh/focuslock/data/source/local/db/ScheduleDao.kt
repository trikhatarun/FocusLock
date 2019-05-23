package com.trikh.focuslock.data.source.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.trikh.focuslock.data.model.Schedule
import io.reactivex.Observable

@Dao
interface ScheduleDao {

    @Insert
    fun addSchedule(schedule: Schedule)

    @Update
    fun updateSchedule(schedule: Schedule)

    @Query("DELETE FROM schedule where id = :id")
    fun removeSchedule(id: Int)

    @Query("SELECT * FROM schedule")
    fun getSchedules(): Observable<Schedule>
}