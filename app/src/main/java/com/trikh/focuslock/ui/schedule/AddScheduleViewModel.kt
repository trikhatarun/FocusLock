package com.trikh.focuslock.ui.schedule

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trikh.focuslock.widget.timepicker.OnSliderRangeMovedListener
import java.text.SimpleDateFormat
import java.util.*

class AddScheduleViewModel : ViewModel() {
     val startTime: MutableLiveData<Calendar> = MutableLiveData()
     val endTime: MutableLiveData<Calendar> = MutableLiveData()

    fun setTime(start: Calendar, end: Calendar) {
        startTime.value = start
        endTime.value = end
    }

    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

    val onTimeChangedListener = OnSliderRangeMovedListener { start, end ->
        this@AddScheduleViewModel.endTime.value = start
        this@AddScheduleViewModel.startTime.value = end
    }


}