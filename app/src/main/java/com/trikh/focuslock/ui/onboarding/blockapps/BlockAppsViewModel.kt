package com.trikh.focuslock.ui.onboarding.blockapps

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trikh.focuslock.R
import com.trikh.focuslock.data.model.Application
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.data.source.ScheduleRepository
import com.trikh.focuslock.utils.Event
import com.trikh.focuslock.utils.TimeUtils
import com.trikh.focuslock.widget.app_picker.AppInfo
import java.util.*
import kotlin.collections.ArrayList

class BlockAppsViewModel : ViewModel() {
    val applicationList: MutableLiveData<List<AppInfo>> =
        MutableLiveData<List<AppInfo>>().apply { value = emptyList() }
    val appPicker: MutableLiveData<Event<Unit>> = MutableLiveData()
    val level: MutableLiveData<Int> = MutableLiveData()
    val scheduleRepository = ScheduleRepository()

    val appBlockText: MutableLiveData<String> = MutableLiveData()

    fun showAppPicker() {
        appPicker.postValue(Event(Unit))
    }


}