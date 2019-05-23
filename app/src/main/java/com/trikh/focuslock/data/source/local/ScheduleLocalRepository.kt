package com.trikh.focuslock.data.source.local

import android.content.Context
import com.trikh.focuslock.Application
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.data.source.local.db.AppDatabase

class ScheduleLocalRepository(context: Context) {
    private val scheduleDao = AppDatabase.getInstance(context).scheduleDao()

    fun addSchedule(schedule: Schedule) = scheduleDao.addSchedule(schedule)

    companion object {
        @Volatile private var instance: ScheduleLocalRepository? = null
        private val LOCK = Any()

        fun getInstance() = instance ?: synchronized(LOCK) {
            instance ?: ScheduleLocalRepository(Application.instance)
        }
    }
}
