package com.trikh.focuslock.data.source.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.trikh.focuslock.data.model.Application

@Dao
interface ApplicationsDao {

    @Insert
    fun insertApplicationList(appList: List<Application>)

    @Query("SELECT * FROM application WHERE scheduleId = :scheduleId")
    fun getApplicationsByScheduleId(scheduleId: Int) : List<Application>
}