package com.trikh.focuslock.data.source

import com.trikh.focuslock.data.model.Application
import com.trikh.focuslock.data.model.InstantLockSchedule
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.data.source.local.ScheduleLocalRepository
import io.reactivex.Observable
import io.reactivex.rxkotlin.flatMapIterable

class ScheduleRepository {
    private val scheduleLocalRepository = ScheduleLocalRepository.getInstance()

    fun addSchedule(schedule: Schedule, callBacks: ScheduleCallBacks) =
        scheduleLocalRepository.addSchedule(schedule, callBacks)

    fun addApplicationList(list: List<Application>) =
        scheduleLocalRepository.addApplicationList(list)

    fun getAllApplicationList(id: Int) = scheduleLocalRepository.getAllApplicationList(id)

    fun updateSchedule(schedule: Schedule) = scheduleLocalRepository.updateSchedule(schedule)

    fun removeSchedule(scheduleId: Int) = scheduleLocalRepository.removeSchedule(scheduleId)

    fun getSchedules(): Observable<List<Schedule>>? {
        /*return scheduleLocalRepository.getSchedules().flatMapIterable().map {
            it.setAppList(getAllApplicationList(it.id))
            it
        }*/
        return scheduleLocalRepository.getSchedules()

    }

    fun insertInstantLock(schedule: InstantLockSchedule) =
        scheduleLocalRepository.insertInstantLock(schedule)

    fun deleteInstantLock() = scheduleLocalRepository.deleteInstantLock()

    fun getInstantLock() = scheduleLocalRepository.getInstantLock()

    interface ScheduleCallBacks {
        fun onScheduleAdded(id: Int)
    }
}