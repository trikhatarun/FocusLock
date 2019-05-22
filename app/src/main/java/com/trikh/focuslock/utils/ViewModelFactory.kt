package com.trikh.focuslock.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trikh.focuslock.widget.app_picker.AppInfo
import com.trikh.focuslock.widget.app_picker.AppListViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory: ViewModelProvider.Factory {

    private val selectedAppList: List<AppInfo>
    private val application:Application

    constructor(application: Application, selectedAppList: List<AppInfo>) {
        this.selectedAppList = selectedAppList
        this.application = application
    }


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AppListViewModel(application, selectedAppList) as T
    }
}