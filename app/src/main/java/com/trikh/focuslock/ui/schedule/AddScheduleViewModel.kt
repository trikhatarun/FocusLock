package com.trikh.focuslock.ui.schedule

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trikh.focuslock.widget.timepicker.OnSliderRangeMovedListener
import java.text.SimpleDateFormat
import java.util.*

class AddScheduleViewModel : ViewModel() {
    val startTime: MutableLiveData<Calendar> = MutableLiveData()
    val endTime: MutableLiveData<Calendar> = MutableLiveData()
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
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

    // This listener is an exception, do not keep any listener in viewmodel instead use data binding and bind functions as listeners
    val onTimeChangedListener = OnSliderRangeMovedListener { start, end ->
        this@AddScheduleViewModel.endTime.value = start
        this@AddScheduleViewModel.startTime.value = end
    }
}