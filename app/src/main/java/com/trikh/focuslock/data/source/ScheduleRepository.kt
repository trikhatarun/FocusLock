package com.trikh.focuslock.data.source

import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.data.source.local.ScheduleLocalRepository

class ScheduleRepository {
    private val scheduleLocalRepository = ScheduleLocalRepository.getInstance()

    fun addSchedule(schedule: Schedule) = scheduleLocalRepository.addSchedule(schedule)

    fun getSchedules() = scheduleLocalRepository.getSchedules()
}