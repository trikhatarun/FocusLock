package com.trikh.focuslock.ui.schedule

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trikh.focuslock.widget.timepicker.OnSliderRangeMovedListener
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.HOUR

class AddScheduleViewModel : ViewModel() {
    private val currentTime = Date()
    val start: MutableLiveData<Calendar> = MutableLiveData<Calendar>().apply {
        val startCalender = Calendar.getInstance()
        startCalender.time = currentTime
        value = startCalender
    }
    val end: MutableLiveData<Calendar> = MutableLiveData<Calendar>().apply {
        val endCalender = Calendar.getInstance()
        endCalender.time = currentTime
        endCalender.add(HOUR, 8)
        value = endCalender
    }
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

    val onTimeChangedListener = OnSliderRangeMovedListener { start, end ->
        this@AddScheduleViewModel.start.value = start
        this@AddScheduleViewModel.end.value = end
    }


}