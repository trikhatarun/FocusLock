package com.trikh.focuslock.ui.schedule.primaryschedule

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trikh.focuslock.R
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.data.source.ScheduleRepository
import com.trikh.focuslock.utils.Event
import com.trikh.focuslock.widget.app_picker.AppInfo
import com.trikh.focuslock.widget.timepicker.TimeSliderRangePicker
import io.reactivex.Observable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PrimaryViewModel : ViewModel() {


    // do not make them private they are used by data binding
    val startTime: MutableLiveData<Calendar> = MutableLiveData()
    val endTime: MutableLiveData<Calendar> = MutableLiveData()

    private val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val applicationList: MutableLiveData<List<AppInfo>> =
        MutableLiveData<List<AppInfo>>().apply { value = emptyList() }
    val appPicker: MutableLiveData<Event<Unit>> = MutableLiveData()
    val scheduleRepository = ScheduleRepository()
    val level: MutableLiveData<Int> = MutableLiveData<Int>().apply { value = 0 }
    val levelId: MutableLiveData<Int> = MutableLiveData()
    val levelTitles = arrayOf(R.string.level_1, R.string.level_2, R.string.level_3)


    fun showAppPicker() {
        appPicker.postValue(Event(Unit))
    }

    fun setTime(start: Calendar, end: Calendar) {

        startTime.value = start
        endTime.value = end
    }

    fun onChecked(id: Int) {
        level.postValue(id)
    }

    fun getSleepTime(time: Date, level: Int): String? {
        return timeFormat.format(calulcateEndTime(time, level).time)
    }

    private fun calulcateEndTime(time: Date, level: Int): Calendar {
        val sleepTime = Calendar.getInstance()
        sleepTime.time = time
        sleepTime.add(Calendar.MINUTE, -(level * 30))
        return sleepTime
    }

    fun getAwakeTime(time: Date, level: Int): String? {
        return timeFormat.format(calculateStartTime(time, level).time)
    }

    private fun calculateStartTime(time: Date, level: Int): Calendar {
        val awakeTime = Calendar.getInstance()
        awakeTime.time = time
        awakeTime.add(Calendar.MINUTE, (level * 60))
        return awakeTime
    }

    /*fun getScheduleById(scheduleId: Int){
        val schedule = scheduleId.let {scheduleRepository.getScheduleById(it) }
        checkedIds.value = schedule.selectedWeekDays
        applicationList.value = schedule.appInfoList
    }*/


    val onTimeChangedListener = TimeSliderRangePicker.OnSliderRangeMovedListener { start, end ->
        this@PrimaryViewModel.endTime.value = end
        this@PrimaryViewModel.startTime.value = start
    }

    fun updateSchedule(id: Int): Observable<Int>? {
        val list: ArrayList<String> = ArrayList()
        applicationList.value?.forEach {
            list.add(it.packageName)
        }

        val schedule = Schedule(
            id = id,
            level = level.value!!,
            endTime = endTime.value!!,
            startTime = startTime.value!!,
            selectedWeekDays = arrayOf(true, true, true, true, true, true, true),
            appList = list as List<String>
        )

        return scheduleRepository.updateSchedule(schedule)

    }


}