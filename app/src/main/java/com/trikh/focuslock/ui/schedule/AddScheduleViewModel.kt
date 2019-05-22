package com.trikh.focuslock.ui.schedule

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trikh.focuslock.widget.timepicker.TimeSliderRangePicker
import java.text.SimpleDateFormat
import java.util.*

class AddScheduleViewModel : ViewModel() {
    val startTime: MutableLiveData<Calendar> = MutableLiveData()
    val endTime: MutableLiveData<Calendar> = MutableLiveData()
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val logTimeFormat = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())
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
        Log.d("startTime:", logTimeFormat.format(endTime.time))
        Log.d("endTime", logTimeFormat.format(startTime.time))
        val sleepTime = endTime.timeInMillis
        var awakeTime = startTime.timeInMillis
        if (sleepTime > awakeTime) {
            awakeTime += 1000 * 60 * 60 * 24
        }
        val difference = (awakeTime - sleepTime) / 60000 // in minutes
        val hours = difference / 60
        val minutes = difference.rem(60)
        return "$hours hr $minutes min"
    }

    val onTimeChangedListener = TimeSliderRangePicker.OnSliderRangeMovedListener { start, end ->
        this@AddScheduleViewModel.endTime.value = start
        this@AddScheduleViewModel.startTime.value = end
    }
}