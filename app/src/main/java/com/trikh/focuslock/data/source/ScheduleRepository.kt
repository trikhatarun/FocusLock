package com.trikh.focuslock.data.source

import android.util.Log
import com.trikh.focuslock.data.model.Application
import com.trikh.focuslock.data.model.InstantLockSchedule
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.data.source.local.ScheduleLocalRepository
import com.trikh.focuslock.widget.app_picker.AppInfo
import io.reactivex.Observable
import io.reactivex.rxkotlin.flatMapIterable
import io.reactivex.schedulers.Schedulers

class ScheduleRepository {
    private val scheduleLocalRepository = ScheduleLocalRepository.getInstance()

    fun addSchedule(schedule: Schedule) =
        scheduleLocalRepository.addSchedule(schedule)

    //fun addApplicationList(list: List<Application>) =
    //scheduleLocalRepository.addApplicationList(list)

    //fun getAllApplicationList(id: Int) = scheduleLocalRepository.getAllApplicationList(id)

    fun updateSchedule(schedule: Schedule) {
        Log.d("ScheduleRepository: ", " $schedule")
        scheduleLocalRepository.updateSchedule(schedule)
    }

    fun removeSchedule(scheduleId: Int) = scheduleLocalRepository.removeSchedule(scheduleId)

    fun getSchedules() = scheduleLocalRepository.getSchedules().flatMap { return@flatMap withDrawableAndLabel(it) }

    fun getScheduleById(id: Int) = scheduleLocalRepository.getScheduleById(id).flatMap{ return@flatMap scheduleWithAppInfoList(it)    }



    private fun scheduleWithAppInfoList(schedule: Schedule): Observable<Schedule> {
        return Observable.fromCallable {
            val localAppList: ArrayList<AppInfo> = ArrayList()
            schedule.appList?.forEach {
                val pkgManager = com.trikh.focuslock.Application.instance.packageManager
                val appInfo = pkgManager.getApplicationInfo(it, 0)
                localAppList.add(
                    AppInfo(
                        name = pkgManager.getApplicationLabel(appInfo).toString(),
                        icon = appInfo.loadIcon(pkgManager),
                        blocked = true,
                        packageName = it
                    )
                )
            }
            schedule.appInfoList = localAppList
            return@fromCallable schedule
        }
    }

    private fun withDrawableAndLabel(schedules: List<Schedule>): Observable<List<Schedule>> {
        return Observable.fromCallable {
            schedules.forEach {
                val localAppList: ArrayList<AppInfo> = ArrayList()
                it.appList?.forEach {
                    val pkgManager = com.trikh.focuslock.Application.instance.packageManager
                    val appInfo = pkgManager.getApplicationInfo(it, 0)
                    localAppList.add(
                        AppInfo(
                            name = pkgManager.getApplicationLabel(appInfo).toString(),
                            icon = appInfo.loadIcon(pkgManager),
                            blocked = true,
                            packageName = it
                        )
                    )
                }
                it.appInfoList = localAppList
            }
            return@fromCallable schedules
        }.subscribeOn(Schedulers.io())
    }

    fun insertInstantLock(schedule: InstantLockSchedule) =
        scheduleLocalRepository.insertInstantLock(schedule)

    fun deleteInstantLock() = scheduleLocalRepository.deleteInstantLock()

    fun updateInstantLockSchedule(schedule: InstantLockSchedule) = scheduleLocalRepository.updateInstantSchedule(schedule)


    fun getInstantLock() = scheduleLocalRepository.getInstantLock()

}