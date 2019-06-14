package com.trikh.focuslock.utils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.widget.app_picker.AppInfo

class IconsUtils( private val instance: Context? = null){




    fun getIconsFromPackageManager(schedule: Schedule): Schedule {
        val localAppList: ArrayList<AppInfo> = ArrayList()
        schedule.appList?.forEach {
            val pkgManager = instance!!.packageManager
            val appInfo = pkgManager.getApplicationInfo(it, 0)
            localAppList.add(
                AppInfo(
                    name = pkgManager.getApplicationLabel(appInfo).toString(),
                    icon = appInfo.loadIcon(pkgManager),
                    blocked = true,
                    packageName = it
                )
            )
        }
        schedule.appInfoList = localAppList
        return schedule
    }



}