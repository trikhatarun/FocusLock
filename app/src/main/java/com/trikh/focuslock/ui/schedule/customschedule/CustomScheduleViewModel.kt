package com.trikh.focuslock.ui.schedule.customschedule

import android.util.Log

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.data.source.ScheduleRepository
import com.trikh.focuslock.utils.Event
import com.trikh.focuslock.widget.app_picker.AppInfo
import com.trikh.focuslock.widget.timepicker.TimeSliderRangePicker
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CustomScheduleViewModel : ViewModel() {


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


    val onTimeChangedListener = TimeSliderRangePicker.OnSliderRangeMovedListener { start, end ->
        this@CustomScheduleViewModel.endTime.value = end
        this@CustomScheduleViewModel.startTime.value = start
    }

    fun createSchedule(): Observable<Long>? {
        val list: ArrayList<String> = ArrayList()
        applicationList.value?.forEach {
            list.add(it.packageName)
        }

        val schedule = Schedule(
            level = -1,
            endTime = endTime.value!!,
            startTime = startTime.value!!,
            selectedWeekDays = checkedIds.value,
            active = true,
            appList = list as List<String>
        )

        return scheduleRepository.addSchedule(schedule)

    }

    fun updateSchedule(id: Int, level: Int, active: Boolean): Observable<Int>? {
        val list: ArrayList<String> = ArrayList()
        applicationList.value?.forEach {
            list.add(it.packageName)
        }

        Log.e(
            "Schedule ",
            "active: $active  End Time: ${endTime.value!!.timeInMillis}"
        )

        Log.d("Update Custom Schedule", "applist: $list")


        val schedule = Schedule(
            id = id,
            active = active,
            level = level,
            endTime = endTime.value!!,
            startTime = startTime.value!!,
            selectedWeekDays = checkedIds.value,
            appList = list as List<String>
        )

       return scheduleRepository.updateSchedule(schedule)

    }





}