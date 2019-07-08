package com.trikh.focuslock.data.source.local.db

import androidx.room.*
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.data.model.WeekDayTime
import io.reactivex.Observable
import java.util.*

@Dao
interface ScheduleDao {

    @Insert
    fun addSchedule(schedule: Schedule): Long

    @Query("UPDATE schedule SET active = :active")
    fun setEmergencyModeOn(active: Boolean): Int

    @Update
    fun updateSchedule(schedule: Schedule): Int

    @Query("SELECT MAX(id) from schedule")
    fun getMaxId(): Int

    @Query("DELETE FROM schedule where id = :id")
    fun removeSchedule(id: Int)

    @Query("SELECT * FROM schedule where id = :id")
    fun getScheduleById(id: Int): Schedule

    @Query("SELECT * FROM schedule")
    fun getSchedules(): Observable<List<Schedule>>

    @Query("SELECT selected_week_days , end_time FROM schedule")
    fun getAllEndTimes(): List<WeekDayTime>


/*  @Query("UPDATE schedule SET start_time = :startTime , end_time = :endTime ,selected_week_days = :selectedWeeks , level = :level , active = :active , appList = :appList  WHERE id LIKE :id")
    fun updateSchedule(id: Int,
                       startTime: Calendar,
                       endTime: Calendar,
                       selectedWeeks: Array<Boolean>,
                       level: Int,
                       active: Boolean,
                       appList: List<String>)*/
}