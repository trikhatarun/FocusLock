package com.trikh.focuslock.ui.schedule

import android.util.Log

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.widget.Toast
import com.trikh.focuslock.data.model.Application
import com.trikh.focuslock.data.model.InstantLockSchedule
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.data.source.ScheduleRepository
import com.trikh.focuslock.utils.Event
import com.trikh.focuslock.widget.app_picker.AppInfo
import com.trikh.focuslock.widget.timepicker.TimeSliderRangePicker
import io.reactivex.rxkotlin.subscribeBy
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ScheduleViewModel : ViewModel() {


    val scheduleList: MutableLiveData<List<Schedule>> = MutableLiveData()
    // do not make them private they are used by data binding
    val startTime: MutableLiveData<Calendar> = MutableLiveData()
    val endTime: MutableLiveData<Calendar> = MutableLiveData()
    val checkedIds: MutableLiveData<Array<Boolean>> =
        MutableLiveData<Array<Boolean>>().apply {
            value = arrayOf(false, false, false, false, false, false, false)
        }
    private val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val applicationList: MutableLiveData<List<AppInfo>> =
        MutableLiveData<List<AppInfo>>().apply { value = emptyList() }
    val appPicker: MutableLiveData<Event<Unit>> = MutableLiveData()
    val scheduleRepository = ScheduleRepository()
    fun showAppPicker() {
        appPicker.postValue(Event(Unit))
    }

    init {
        scheduleRepository.getSchedules().subscribeBy {
            scheduleList.postValue(it)
        }
    }


    fun setTime(start: Calendar, end: Calendar) {

        startTime.value = start
        endTime.value = end

    }

    fun onChecked(check: Boolean, id: Int) {

        val selectedIds = checkedIds.value!!
        selectedIds.set(id, check)
        checkedIds.postValue(selectedIds)


    }

    /*fun setChecked(id: Int): Boolean {

       return checkedIds.value?.get(id)!!
    }*/


    fun getSleepTime(time: Date, level: Int): String? {
        val sleepTime = Calendar.getInstance()
        sleepTime.time = time
        sleepTime.add(Calendar.MINUTE, -(level * 30))
        return timeFormat.format(sleepTime.time)
    }

    fun getAwakeTime(time: Date, level: Int): String? {
        val awakeTime = Calendar.getInstance()
        awakeTime.time = time
        awakeTime.add(Calendar.MINUTE, (level * 60))
        return timeFormat.format(awakeTime.time)
    }

    fun calculateDuration(startTime: Calendar, endTime: Calendar): String {
        val sleepTime = startTime.timeInMillis
        var awakeTime = endTime.timeInMillis
        if (sleepTime > awakeTime) awakeTime +=  86400000
        val difference = (awakeTime - sleepTime) / 60000 // in minutes
        val hours = difference / 60
        val minutes = Math.round(difference.rem(60) / 10f) * 10
        return "$hours hr $minutes min"
    }

    /*fun getScheduleById(scheduleId: Int){
        val schedule = scheduleId.let {scheduleRepository.getScheduleById(it) }
        checkedIds.value = schedule.selectedWeekDays
        applicationList.value = schedule.appInfoList
    }*/


    val onTimeChangedListener = TimeSliderRangePicker.OnSliderRangeMovedListener { start, end ->
        this@ScheduleViewModel.endTime.value = end
        this@ScheduleViewModel.startTime.value = start
    }

    fun createSchedule(): Schedule {
        val list: ArrayList<String> = ArrayList()
        applicationList.value?.forEach {
            list.add(it.packageName)
        }

        val schedule = Schedule(
            endTime = endTime.value!!,
            startTime = startTime.value!!,
            selectedWeekDays = checkedIds.value,
            appList = list as List<String>
        )
        Log.e(
            "Schedule ",
            "Start Time: " + startTime.value!!.timeInMillis + " End Time: " + endTime.value!!.timeInMillis
        )
        scheduleRepository.addSchedule(schedule)


        return schedule
    }

    fun updateSchedule() {
        val list: ArrayList<String> = ArrayList()
        applicationList.value?.forEach {
            list.add(it.packageName)
        }

        val schedule = Schedule(
            endTime = endTime.value!!,
            startTime = startTime.value!!,
            selectedWeekDays = checkedIds.value,
            appList = list as List<String>
        )
        Log.e(
            "Schedule ",
            "Start Time: " + startTime.value!!.timeInMillis + " End Time: " + endTime.value!!.timeInMillis
        )
        scheduleRepository.updateSchedule(schedule)

    }


    fun enableOrDisableSchedule(schedule: Schedule) {
        Log.e("ScheduleViewModel: "," $schedule")
        scheduleRepository.updateSchedule(schedule)
    }

    fun removeSchedule(id: Int) = scheduleRepository.removeSchedule(id)


}