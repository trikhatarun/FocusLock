package com.trikh.focuslock.ui.onboarding.timepicker

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trikh.focuslock.Application
import com.trikh.focuslock.R
import java.util.*

class TimePickerViewModel : ViewModel() {
    val hours: MutableLiveData<Int> =
        MutableLiveData<Int>().apply { value = Calendar.getInstance().get(Calendar.HOUR) }
    val minutes: MutableLiveData<Int> =
        MutableLiveData<Int>().apply { value = Calendar.getInstance().get(Calendar.MINUTE) }
    val sleepHours: MutableLiveData<Int> = MutableLiveData<Int>().apply { value = 0 }
    val blockText: MutableLiveData<String> = MutableLiveData<String>().apply {
        value = Application.instance.resources.getString(R.string.selected_sleep_time, 4, 0)
    }

    fun setSleepTime(sleepTime: Int){
        sleepHours.postValue(sleepTime)
    }

    fun getStartTime(): Calendar {
        val cal = Calendar.getInstance()
        hours.value?.let { cal.set(Calendar.HOUR, it) }
        minutes.value?.let { cal.set(Calendar.MINUTE, it) }
        return cal
    }

    fun getSleepTime(): Calendar {
        val cal = Calendar.getInstance()
        val it = if (sleepHours.value != null) {
            sleepHours.value!!
        } else {
            0
        }
        var minute = 0
        var hour = 4
        when (it % 2) {
            0 -> {
                hour = (it / 2) + 4
                minute = 0
            }
            else -> {
                hour = ((it - 1) / 2) + 4
                minute = 30

            }

        }

        hours.value?.let { hour += it }
        minutes.value?.let { minute += it }

        cal.set(Calendar.HOUR, hour)
        cal.set(Calendar.MINUTE, minute)
        return cal

    }
}