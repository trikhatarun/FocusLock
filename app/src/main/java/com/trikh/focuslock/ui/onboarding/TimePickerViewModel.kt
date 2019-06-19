package com.trikh.focuslock.ui.onboarding

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trikh.focuslock.Application
import com.trikh.focuslock.R
import com.trikh.focuslock.utils.TimeDurationUtils
import java.util.*

class TimePickerViewModel : ViewModel() {
    val hours: MutableLiveData<Int> = MutableLiveData()
    val minutes: MutableLiveData<Int> = MutableLiveData()
    val sleepHours: MutableLiveData<Int> = MutableLiveData<Int>().apply { value = 0 }
    val blockText: MutableLiveData<String> = MutableLiveData< String>().apply{ value = Application.instance.resources.getString(R.string.selected_sleep_time, 0)}

    init {

    }

    fun onClick(){
        val startTime = Calendar.getInstance()
        val endTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR, hours.value!!)
        startTime.set(Calendar.MINUTE, minutes.value!!)

        endTime.set(Calendar.HOUR, sleepHours.value!!)
        val duration = TimeDurationUtils.calculateDuration(startTime,endTime)
        Log.d("TimePickerViewModel:"," duration: $duration")
    }





}