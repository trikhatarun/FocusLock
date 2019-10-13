package com.trikh.focuslock.ui.instantlock

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trikh.focuslock.data.model.InstantLockSchedule
import com.trikh.focuslock.data.source.ScheduleRepository
import com.trikh.focuslock.utils.Event
import com.trikh.focuslock.widget.app_picker.AppInfo
import io.reactivex.Observable
import java.util.*
import kotlin.collections.ArrayList

class InstantLockViewModel : ViewModel() {
    val hours: MutableLiveData<Int> = MutableLiveData<Int>().apply { value = 0 }
    val minutes: MutableLiveData<Int> = MutableLiveData<Int>().apply{ value = 0}
    val applicationList: MutableLiveData<List<AppInfo>> =
        MutableLiveData<List<AppInfo>>().apply { value = emptyList() }
    val appPicker: MutableLiveData<Event<Unit>> = MutableLiveData()
    private val scheduleRepository = ScheduleRepository()
    fun showAppPicker() {
        appPicker.postValue(Event(Unit))
    }

    private fun getEndTime(): Long{
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR,hours.value!!)
        calendar.add(Calendar.MINUTE,minutes.value!! * 10)
        return calendar.timeInMillis
    }

    fun setMinutes(sleepTime: Int){
        minutes.postValue(sleepTime)
    }

    fun setHours(sleepTime: Int){

        hours.postValue((sleepTime/10)-1)
    }

    private fun getBlockedPackageNames() : List<String>{
        val packageList = ArrayList<String>()
        applicationList.value?.forEach{
            packageList.add(it.packageName)
        }
        return packageList
    }

    fun createInstantLockSchedule(): Observable<Long>? {
        val instantLockSchedule = InstantLockSchedule(endTime = getEndTime(), blockedApps = getBlockedPackageNames())

        return scheduleRepository.insertInstantLock(instantLockSchedule)
    }
}