package com.trikh.focuslock.data.source.local

import android.content.Context
import com.trikh.focuslock.Application
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.data.source.local.db.AppDatabase
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class ScheduleLocalRepository(context: Context) {
    private val scheduleDao = AppDatabase.getInstance(context).scheduleDao()

    fun addSchedule(schedule: Schedule) {
        Observable.fromCallable { scheduleDao.addSchedule(schedule) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun updateSchedule(schedule: Schedule) {
        Observable.fromCallable { scheduleDao.updateSchedule(schedule) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun removeSchedule(scheduleId: Int) {
        Observable.fromCallable { scheduleDao.removeSchedule(scheduleId) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun getSchedules() = scheduleDao.getSchedules()

    companion object {
        @Volatile
        private var instance: ScheduleLocalRepository? = null
        private val LOCK = Any()

        fun getInstance() = instance ?: synchronized(LOCK) {
            instance ?: ScheduleLocalRepository(Application.instance)
        }
    }
}
