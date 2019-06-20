package com.trikh.focuslock.ui.onboarding.blockapps

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trikh.focuslock.utils.Event
import com.trikh.focuslock.widget.app_picker.AppInfo

class BlockAppsViewModel : ViewModel(){
    val applicationList: MutableLiveData<List<AppInfo>> =
        MutableLiveData<List<AppInfo>>().apply { value = emptyList() }
    val appPicker: MutableLiveData<Event<Unit>> = MutableLiveData()
    val level: MutableLiveData<Int> = MutableLiveData<Int>().apply { value = 1 }

    fun showAppPicker() {
        appPicker.postValue(Event(Unit))
    }
}