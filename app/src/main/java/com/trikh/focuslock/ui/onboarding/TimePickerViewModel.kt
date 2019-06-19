package com.trikh.focuslock.ui.onboarding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimePickerViewModel : ViewModel() {
    val hours: MutableLiveData<Int> = MutableLiveData()
    val minutes: MutableLiveData<Int> = MutableLiveData()
    val sleepHours: MutableLiveData<Int> = MutableLiveData<Int>().apply { value = 0 }

}