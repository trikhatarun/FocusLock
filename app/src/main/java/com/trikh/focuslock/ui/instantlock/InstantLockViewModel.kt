package com.trikh.focuslock.ui.instantlock

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trikh.focuslock.utils.Event
import com.trikh.focuslock.widget.app_picker.AppInfo

class InstantLockViewModel : ViewModel() {
    val hours: MutableLiveData<Int> = MutableLiveData<Int>().apply { value = 0 }
    val minutes: MutableLiveData<Int> = MutableLiveData<Int>().apply{ value = 0}
    val applicationList: MutableLiveData<List<AppInfo>> =
        MutableLiveData<List<AppInfo>>().apply { value = emptyList() }
    val appPicker: MutableLiveData<Event<Unit>> = MutableLiveData()

    fun showAppPicker() {
        appPicker.postValue(Event(Unit))
    }
}