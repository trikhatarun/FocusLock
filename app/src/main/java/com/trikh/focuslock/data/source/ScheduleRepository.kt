package com.trikh.focuslock.data.source

import android.content.pm.PackageManager
import com.trikh.focuslock.Application
import com.trikh.focuslock.data.model.InstantLockSchedule
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.data.model.WeekDayTime
import com.trikh.focuslock.data.source.local.ScheduleLocalRepository
import com.trikh.focuslock.data.utils.ServiceUtil
import com.trikh.focuslock.widget.app_picker.AppInfo
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

class ScheduleRepository {
    private val scheduleLocalRepository = ScheduleLocalRepository.getInstance()

    fun addSchedule(schedule: Schedule): Observable<Long> =
        scheduleLocalRepository.addSchedule(schedule)

    fun updateSchedule(schedule: Schedule): Observable<Int> =
        scheduleLocalRepository.updateSchedule(schedule)

    private fun getInstantLockBlockedPackages(): Observable<ArrayList<String>> = getInstantLock().map {
        val list = ArrayList<String>()
        if (it.endTime > System.currentTimeMillis()) {
            list.addAll(it.blockedApps)
        } else {
            deleteInstantLock()
        }
        return@map list
    }

    fun setEmergencyModeOn(): Observable<Int> =
        scheduleLocalRepository.deleteInstantLock().flatMap {
            return@flatMap scheduleLocalRepository.setEmergencyModeOn(false).map {
                return@map it
            }
        }

    fun setPrimaryScheduleActive(): Observable<Int> =
        scheduleLocalRepository.setPrimaryScheduleActive()

    private fun getScheduleBlockedPackages(): Observable<ArrayList<String>> = scheduleLocalRepository.getSchedules().map {
        return@map ServiceUtil.getAllBlockedPackages(it)
    }


    fun getInstantLockCount(): Observable<Int> = scheduleLocalRepository.getCount()

    fun getBlockedPackages(): Observable<ArrayList<String>> =
        scheduleLocalRepository.getCount().flatMap {
            return@flatMap choosePackage(it)
        }

    private fun mergePackages(): Observable<ArrayList<String>> = Observable.zip(
        getScheduleBlockedPackages(),
        getInstantLockBlockedPackages(),
        BiFunction { t2: ArrayList<String>, t1: ArrayList<String> ->
            t2.addAll(t1)
            return@BiFunction t2
        }
    )

    private fun choosePackage(size: Int): Observable<ArrayList<String>> = when {
        size > 0 -> {
            mergePackages()
        }
        else -> {
            getScheduleBlockedPackages().map {
                return@map it
            }
        }
    }


    private fun mergeTiming(): Observable<Long> = Observable.zip(
        scheduleLocalRepository.getScheduleEndTime(),
        scheduleLocalRepository.getInstantLockEndTime(),
        BiFunction { t1: ArrayList<WeekDayTime>, t2: ArrayList<Calendar> ->
            t2.addAll(timeWithWeekDays(t1))
            return@BiFunction ServiceUtil.getRunningTime(t2)

        }
    )

    private fun chooseTiming(size: Int): Observable<Long> =
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

    fun getLastSchedule(): Observable<Int> = scheduleLocalRepository.getLastSchedule()


    fun removeSchedule(scheduleId: Int) = scheduleLocalRepository.removeSchedule(scheduleId)

    fun getSchedules(): Observable<List<Schedule>> =
        scheduleLocalRepository.getSchedules().flatMap { return@flatMap withDrawableAndLabel(it) }

    fun getRunningTime(): Observable<Long> = scheduleLocalRepository.getCount().flatMap {
        return@flatMap chooseTiming(it)
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

    fun insertInstantLock(schedule: InstantLockSchedule): Observable<Long> =
        scheduleLocalRepository.insertInstantLock(schedule)

    fun deleteInstantLock(): Disposable = scheduleLocalRepository.deleteInstantLock().subscribe()

    fun updateInstantLockSchedule(schedule: InstantLockSchedule) =
        scheduleLocalRepository.updateInstantSchedule(schedule)

    fun getInstantLock(): Observable<InstantLockSchedule> = scheduleLocalRepository.getInstantLock()
}