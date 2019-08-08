package com.trikh.focuslock.ui.schedule

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trikh.focuslock.data.model.InstantLockSchedule
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.data.source.ScheduleRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy

class ScheduleViewModel : ViewModel() {

    val scheduleList: MutableLiveData<List<Schedule>> = MutableLiveData()
    val instantLockSchedule: MutableLiveData<InstantLockSchedule> = MutableLiveData()
    private val scheduleRepository = ScheduleRepository()
    private val compositeDisposable = CompositeDisposable()


    init {
        compositeDisposable += scheduleRepository.getSchedules().subscribeBy {
            scheduleList.postValue(it)
        }

        compositeDisposable += scheduleRepository.getInstantLock().subscribeBy {
            instantLockSchedule.postValue(it)
        }
    }

    fun getInstantLockCount() = scheduleRepository.getInstantLockCount()

    fun enableOrDisableSchedule(schedule: Schedule) =
        scheduleRepository.updateSchedule(schedule)


    fun removeSchedule(id: Int) = scheduleRepository.removeSchedule(id)

    fun removeInstantLockSchedule() = scheduleRepository.deleteInstantLock()

    fun updateInstantLockSchedule(schedule: InstantLockSchedule) =
        scheduleRepository.updateInstantLockSchedule(schedule)
}