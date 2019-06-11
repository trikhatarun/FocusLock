package com.trikh.focuslock.data.source.local

import android.content.ComponentCallbacks
import android.content.Context
import com.trikh.focuslock.Application
import com.trikh.focuslock.data.model.InstantLockSchedule
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.data.source.ScheduleRepository
import com.trikh.focuslock.data.source.local.db.AppDatabase
import com.trikh.focuslock.data.source.local.db.ApplicationsDao
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class ScheduleLocalRepository(context: Context) {
    private val scheduleDao = AppDatabase.getInstance(context).scheduleDao()
    private val instantLockDao = AppDatabase.getInstance(context).instantLockDao()
    //private val applicationDao = AppDatabase.getInstance(context).applicationsDao()

    fun addSchedule(schedule: Schedule) {
        var id: Long? = null
        Observable.fromCallable { id = scheduleDao.addSchedule(schedule) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    // Not Yet Used May Be Used In Future
    /*fun addApplicationList(list: List<com.trikh.focuslock.data.model.Application>){
        Observable.fromCallable { applicationDao.insertApplicationList(list) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun getAllApplicationList(id: Int) = applicationDao.getApplicationsByScheduleId(id)
*/


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

    fun getSchedules() = scheduleDao.getSchedules().subscribeOn(Schedulers.io())

    fun insertInstantLock(schedule: InstantLockSchedule) {
        Observable.fromCallable { instantLockDao.insertSchedule(schedule) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun deleteInstantLock() {
        Observable.fromCallable { instantLockDao.deleteSchedule() }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun getInstantLock() = instantLockDao.getSchedule().subscribeOn(Schedulers.io())

    companion object {
        @Volatile
        private var instance: ScheduleLocalRepository? = null
        private val LOCK = Any()

        fun getInstance() = instance ?: synchronized(LOCK) {
            instance ?: ScheduleLocalRepository(Application.instance)
        }
    }
}
