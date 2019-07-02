package com.trikh.focuslock.data.source

import android.util.Log
import com.trikh.focuslock.data.model.InstantLockSchedule
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.data.source.local.ScheduleLocalRepository
import com.trikh.focuslock.data.utils.ServiceUtil
import com.trikh.focuslock.widget.app_picker.AppInfo
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

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

    fun getInstantLockBlockedPackages() = getInstantLock().map {
        val list = ArrayList<String>()
        if (it.endTime.compareTo(System.currentTimeMillis()) > 0) {
            list.addAll(it.blockedApps)
        } else {
            deleteInstantLock()
        }
        return@map list
    }


    fun getScheduleBlockedPackages() = scheduleLocalRepository.getSchedules().map {
        val packageList = ServiceUtil.getAllBlockedPackages(it)
        Log.d("jkg", "${packageList.size}")
        return@map packageList
    }

    fun getBlockedPackages() =
        scheduleLocalRepository.getCount().flatMap {
            return@flatMap choosePackage(it)

            /*if (it > 0) {
              return@flatMap getInstantLockBlockedPackages().map {
                    val list = it
                    return@map getScheduleBlockedPackages().map {
                        list.addAll(it)
                        return@map list
                    }
                }
            } else {
               return@flatMap getScheduleBlockedPackages().map {
                    return@map it
                }
            }*/

        }

    /*fun choosePackage(size: Int) = when {
        size > 0 -> {
            val list = ArrayList<String>()
            getInstantLockBlockedPackages().map {
                list.addAll(it)
                 getScheduleBlockedPackages().map {
                    list.addAll(it)
                    list
                }
            }

        }
        else -> {
            getScheduleBlockedPackages().map {
                return@map it
            }

        }*/


    fun mergePackages() = Observable.zip(
        getScheduleBlockedPackages(),
        getInstantLockBlockedPackages(),
        BiFunction { t2: ArrayList<String>, t1: ArrayList<String> ->
            t2.addAll(t1)
            return@BiFunction t2
        }
    )

    fun choosePackage(size: Int) = when {
        size > 0 -> {
            mergePackages()
        }
        else -> {
            getScheduleBlockedPackages().map {
                return@map it
            }
        }
    }


    fun mergeTiming() = Observable.zip(
        scheduleLocalRepository.getScheduleEndTime(),
        scheduleLocalRepository.getInstantLockEndTime(),
        BiFunction { t1: ArrayList<Calendar>, t2: ArrayList<Calendar> ->
            t1.addAll(t2)
            return@BiFunction ServiceUtil.getRunningTime(t1)

        }
    )

    fun chooseTiming(size: Int) =
        when {
            size > 0 -> {
                mergeTiming()
            }
            else -> {
                scheduleLocalRepository.getScheduleEndTime().map {
                    return@map ServiceUtil.getRunningTime(it)
                    //return@flatMap checkSmallestEndTime(it)
                }
            }
        }


    fun removeSchedule(scheduleId: Int) = scheduleLocalRepository.removeSchedule(scheduleId)

    fun getSchedules() =
        scheduleLocalRepository.getSchedules().flatMap { return@flatMap withDrawableAndLabel(it) }

    /*fun getScheduleById(id: Int) = scheduleLocalRepository.getScheduleById(id).flatMap {
        return@flatMap scheduleWithAppInfoList(it)
    }*/

    fun getRunningTime() = scheduleLocalRepository.getCount().flatMap {

        return@flatMap chooseTiming(it)
    }


    /*private fun checkSmallestEndTime(scheduleEndTime: List<Calendar>): Observable<Long> {
        return scheduleLocalRepository.getInstantLockEndTime().map {
            var cal = Calendar.getInstance()
            cal.timeInMillis = it
            val list = (scheduleEndTime as ArrayList<Calendar>)
            list.add(cal)
            return@map ServiceUtil.getRunningTime(list)

        }
    }*/


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

    fun updateInstantLockSchedule(schedule: InstantLockSchedule) =
        scheduleLocalRepository.updateInstantSchedule(schedule)


    fun getInstantLock() = scheduleLocalRepository.getInstantLock()

}