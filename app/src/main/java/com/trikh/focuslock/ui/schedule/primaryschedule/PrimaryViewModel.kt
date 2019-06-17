package com.trikh.focuslock.ui.schedule.primaryschedule

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trikh.focuslock.R
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.data.source.ScheduleRepository
import com.trikh.focuslock.utils.Event
import com.trikh.focuslock.widget.app_picker.AppInfo
import com.trikh.focuslock.widget.timepicker.TimeSliderRangePicker
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Level
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
    val levelTitles = arrayOf(R.string.level_1, R.string.level_2, R.string.level_3)


    fun showAppPicker() {
        appPicker.postValue(Event(Unit))
    }

    init {

    }


    fun setTime(start: Calendar, end: Calendar) {

        startTime.value = start
        endTime.value = end

    }


    fun setLevels(id: Int) {
        Log.d("setLevels: ", "level: $id")
        level.postValue(id)
    }

    fun getLevels(id: Int): Boolean {
        if (id == level.value) {
            Log.d("getLevels: ", "level: $id")
            return true
        }
        return false

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


    /*fun getScheduleById(scheduleId: Int){
        val schedule = scheduleId.let {scheduleRepository.getScheduleById(it) }
        checkedIds.value = schedule.selectedWeekDays
        applicationList.value = schedule.appInfoList
    }*/


    val onTimeChangedListener = TimeSliderRangePicker.OnSliderRangeMovedListener { start, end ->
        this@PrimaryViewModel.endTime.value = end
        this@PrimaryViewModel.startTime.value = start
    }

    fun createSchedule(): Schedule {
        val list: ArrayList<String> = ArrayList()
        applicationList.value?.forEach {
            list.add(it.packageName)
        }

        val schedule = Schedule(
            level = -1,
            endTime = endTime.value!!,
            startTime = startTime.value!!,
            active = true,
            appList = list as List<String>
        )

        scheduleRepository.addSchedule(schedule)


        return schedule
    }

    fun updateSchedule(id: Int, active: Boolean) {
        val list: ArrayList<String> = ArrayList()
        applicationList.value?.forEach {
            list.add(it.packageName)
        }

        Log.e(
            "Schedule ",
            "active: $active  End Time: ${endTime.value!!.timeInMillis}"
        )


        val schedule = Schedule(
            id = id,
            active = active,
            level = level.value!!,
            endTime = endTime.value!!,
            startTime = startTime.value!!,
            appList = list as List<String>
        )

        scheduleRepository.updateSchedule(schedule)

    }


}