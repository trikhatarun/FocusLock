package com.trikh.focuslock.ui.schedule

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trikh.focuslock.utils.extensions.addOneDay
import com.trikh.focuslock.widget.timepicker.TimeSliderRangePicker
import java.text.SimpleDateFormat
import java.util.*

class AddScheduleViewModel : ViewModel() {
    // do not make them private they are used by data binding
    val startTime: MutableLiveData<Calendar> = MutableLiveData()
    val endTime: MutableLiveData<Calendar> = MutableLiveData()
    private val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

    fun setTime(start: Calendar, end: Calendar) {
        startTime.value = start
        endTime.value = end
    }

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
        val awakeTime = endTime.timeInMillis
        if (sleepTime > awakeTime) awakeTime.addOneDay
        val difference = (awakeTime - sleepTime) / 60000 // in minutes
        val hours = difference / 60
        val minutes = Math.round(difference.rem(60) / 10f) * 10
        return "$hours hr $minutes min"
    }

    val onTimeChangedListener = TimeSliderRangePicker.OnSliderRangeMovedListener { start, end ->
        this@AddScheduleViewModel.endTime.value = end
        this@AddScheduleViewModel.startTime.value = start
    }
}