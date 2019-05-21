package com.trikh.focuslock.widget.app_picker

import android.app.Application
import android.content.pm.ApplicationInfo
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers

class AppListViewModel(application: Application) : AndroidViewModel(application) {

    private val appInfoList = MutableLiveData<List<AppInfo>>()
    private val packageManager = application.packageManager
    private val compositeDisposable = CompositeDisposable()
    private val appListObservable: Single<List<AppInfo>> =
        packageManager.getInstalledApplications(0).toObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .filter { return@filter it.flags and ApplicationInfo.FLAG_SYSTEM == 0 }
            .map { it.toAppInfo() }
            .toSortedList { o1, o2 -> o1.name.compareTo(o2.name, true) }

    init {
        appListObservable.subscribeBy {
            appInfoList.postValue(it)
        }.addTo(compositeDisposable)
    }

    fun getAppInfoList() = appInfoList as LiveData<List<AppInfo>>

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    private fun ApplicationInfo.toAppInfo(): AppInfo {
        return AppInfo(
            packageManager?.getApplicationLabel(this).toString(),
            loadIcon(packageManager),
            false,
            packageName
        )
    }
}