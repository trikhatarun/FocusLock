package com.trikh.focuslock.ui.onboarding.timepicker

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trikh.focuslock.Application
import com.trikh.focuslock.R
import java.util.*

class TimePickerViewModel : ViewModel() {
    val hours: MutableLiveData<Int> = MutableLiveData<Int>().apply { value = Calendar.getInstance().get(Calendar.HOUR) }
    val minutes: MutableLiveData<Int> = MutableLiveData<Int>().apply { value = Calendar.getInstance().get(Calendar.MINUTE) }
    val sleepHours: MutableLiveData<Int> = MutableLiveData<Int>().apply { value = 0 }
    val blockText: MutableLiveData<String> = MutableLiveData< String>().apply{ value = Application.instance.resources.getString(R.string.selected_sleep_time, 0)}
    }