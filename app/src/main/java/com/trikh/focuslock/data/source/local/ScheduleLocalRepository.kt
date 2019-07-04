package com.trikh.focuslock.data.source.local

import android.content.Context
import android.util.Log
import com.trikh.focuslock.Application
import com.trikh.focuslock.data.model.InstantLockSchedule
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.data.model.WeekDayTime
import com.trikh.focuslock.data.source.local.db.AppDatabase
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

class ScheduleLocalRepository(context: Context) {
    private val scheduleDao = AppDatabase.getInstance(context).scheduleDao()
    private val instantLockDao = AppDatabase.getInstance(context).instantLockDao()
    //private val applicationDao = AppDatabase.getInstance(context).applicationsDao()

    fun addSchedule(schedule: Schedule) =
        Observable.fromCallable { scheduleDao.addSchedule(schedule) }
            .subscribeOn(Schedulers.io())


    fun getScheduleById(id: Int) =
        Observable.fromCallable { scheduleDao.getScheduleById(id) }
            .subscribeOn(Schedulers.io())

    fun getScheduleEndTime() = Observable.fromCallable { scheduleDao.getAllEndTimes() }
        .subscribeOn(Schedulers.io())
        .map {
            val list = ArrayList<WeekDayTime>()
            list.addAll(it)
            return@map list
        }

    fun getInstantLockEndTime() = Observable.fromCallable { instantLockDao.getEndTime() }
        .subscribeOn(Schedulers.io())
        .map {
            val list = ArrayList<Calendar>()
            val cal = Calendar.getInstance()
            cal.timeInMillis = it
            list.add(cal)
            return@map list
        }

    fun getLastSchedule() = Observable.fromCallable { scheduleDao.getMaxId() }
        .subscribeOn(Schedulers.io())



    // Not Yet Used May Be Used In Future
    /*fun addApplicationList(list: List<com.trikh.focuslock.data.model.Application>){
        Observable.fromCallable { applicationDao.insertApplicationList(list) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun getAllApplicationList(id: Int) = applicationDao.getApplicationsByScheduleId(id)

*/

    fun updateSchedule(schedule: Schedule) =
        Observable.fromCallable { scheduleDao.updateSchedule(schedule) }
            .subscribeOn(Schedulers.io())



    fun removeSchedule(scheduleId: Int) {
        Observable.fromCallable { scheduleDao.removeSchedule(scheduleId) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun getSchedules() = scheduleDao.getSchedules().subscribeOn(Schedulers.io())

    fun insertInstantLock(schedule: InstantLockSchedule) = Observable.fromCallable { instantLockDao.insertSchedule(schedule) }
            .subscribeOn(Schedulers.io())

    fun deleteInstantLock() {
        Observable.fromCallable { instantLockDao.deleteSchedule() }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun updateInstantSchedule(schedule: InstantLockSchedule) {
        Log.d("LocalRepository: ", " $schedule")
        Observable.fromCallable { instantLockDao.updateSchedule(schedule) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun getCount() = Observable.fromCallable { instantLockDao.getCount() }
        .subscribeOn(Schedulers.io())

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
