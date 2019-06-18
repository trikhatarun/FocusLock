package com.trikh.focuslock.ui.schedule

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trikh.focuslock.data.model.InstantLockSchedule
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.data.source.ScheduleRepository
import io.reactivex.rxkotlin.subscribeBy

class ScheduleViewModel: ViewModel(){

    val scheduleList: MutableLiveData<List<Schedule>> = MutableLiveData()

    val instantLockSchedule: MutableLiveData<InstantLockSchedule> = MutableLiveData()

    val scheduleRepository = ScheduleRepository()

    init {
        scheduleRepository.getSchedules().subscribeBy {
            scheduleList.postValue(it)
        }

        scheduleRepository.getInstantLock().subscribeBy {
            instantLockSchedule.postValue(it)
        }
    }

    fun enableOrDisableSchedule(schedule: Schedule) {
        Log.e("ScheduleViewModel: "," $schedule")
        scheduleRepository.updateSchedule(schedule)
    }

    fun removeSchedule(id: Int) = scheduleRepository.removeSchedule(id)

    fun removeInstantLockSchedule() = scheduleRepository.deleteInstantLock()

    fun updateInstantLockSchedule(schedule: InstantLockSchedule) = scheduleRepository.updateInstantLockSchedule(schedule)



}