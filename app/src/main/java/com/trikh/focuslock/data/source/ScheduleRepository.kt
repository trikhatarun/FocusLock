package com.trikh.focuslock.data.source

import android.content.pm.PackageManager
import android.util.Log
import com.trikh.focuslock.Application
import com.trikh.focuslock.data.model.InstantLockSchedule
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.data.model.WeekDayTime
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

    fun updateSchedule(schedule: Schedule) =
        scheduleLocalRepository.updateSchedule(schedule)

    fun getInstantLockBlockedPackages() = getInstantLock().map {
        val list = ArrayList<String>()
        if (it.endTime > System.currentTimeMillis()) {
            list.addAll(it.blockedApps)
        } else {
            deleteInstantLock()
        }
        return@map list
    }

    fun setEmergencyModeOn() =
        scheduleLocalRepository.deleteInstantLock().flatMap {
            return@flatMap scheduleLocalRepository.setEmergencyModeOn(false).map {
                return@map it
            }

        }

    fun setPrimaryScheduleActive() =
        scheduleLocalRepository.setPrimaryScheduleActive()

    private fun setAllScheduleInActive(list: ArrayList<Schedule>): ArrayList<Schedule> {


        list.forEach { it: Schedule ->
            it.active = false
        }
        Log.d("all Schedule Disabled ", "$list")
        return list
    }


    fun getScheduleBlockedPackages() = scheduleLocalRepository.getSchedules().map {
        val packageList = ServiceUtil.getAllBlockedPackages(it)
        Log.d("jkg", "${packageList.size}")
        return@map packageList
    }


    fun getInstantLockCount() = scheduleLocalRepository.getCount()

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
        BiFunction { t1: ArrayList<WeekDayTime>, t2: ArrayList<Calendar> ->
            t2.addAll(timeWithWeekDays(t1))
            return@BiFunction ServiceUtil.getRunningTime(t2)

        }
    )

    fun chooseTiming(size: Int) =
        when {
            size > 0 -> {
                mergeTiming()
            }
            else -> {
                scheduleLocalRepository.getScheduleEndTime().map {
                    return@map ServiceUtil.getRunningTime(timeWithWeekDays(it))
                    //return@flatMap checkSmallestEndTime(it)
                }
            }
        }

    private fun timeWithWeekDays(list: ArrayList<WeekDayTime>): ArrayList<Calendar> {
        val endTimeList = ArrayList<Calendar>()
        val day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1

        list.forEach {
            val weekDay = it.selectedWeekDays?.get(day) != null
            if (weekDay) {
                endTimeList.add(it.endTime)
            }
        }
        return endTimeList


    }

    fun getLastSchedule() = scheduleLocalRepository.getLastSchedule()


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
                val pkgManager = Application.instance.packageManager
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
            schedules.forEach { schedule ->
                val localAppList: ArrayList<AppInfo> = ArrayList()
                schedule.appList?.forEach {
                    val pkgManager = Application.instance.packageManager
                    try {
                        val appInfo = pkgManager.getApplicationInfo(it, 0)
                        localAppList.add(
                            AppInfo(
                                name = pkgManager.getApplicationLabel(appInfo).toString(),
                                icon = appInfo.loadIcon(pkgManager),
                                blocked = true,
                                packageName = it
                            )
                        )
                    } catch (ignored: PackageManager.NameNotFoundException) { }
                }
                schedule.appInfoList = localAppList
            }
            return@fromCallable schedules
        }.subscribeOn(Schedulers.io())
    }

    fun insertInstantLock(schedule: InstantLockSchedule) =
        scheduleLocalRepository.insertInstantLock(schedule)

    fun deleteInstantLock() = scheduleLocalRepository.deleteInstantLock().subscribe()

    fun updateInstantLockSchedule(schedule: InstantLockSchedule) =
        scheduleLocalRepository.updateInstantSchedule(schedule)


    fun getInstantLock() = scheduleLocalRepository.getInstantLock()

}