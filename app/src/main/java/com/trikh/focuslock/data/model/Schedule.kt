package com.trikh.focuslock.data.model

import android.content.Context
import android.content.pm.PackageManager
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.trikh.focuslock.widget.app_picker.AppInfo
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = "schedule")
data class Schedule(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "start_time") val startTime: Calendar,
    @ColumnInfo(name = "end_time") val endTime: Calendar,
    @ColumnInfo(name = "selected_week_days") val selectedWeekDays: Array<Boolean>? = null,
    val level: Int? = null,
    val active: Boolean? = null
) {
    @Ignore
    var appList: ArrayList<AppInfo> = ArrayList()

    constructor(
        id: Int,
        startTime: Calendar,
        endTime: Calendar,
        selectedWeekDays: Array<Boolean>?,
        level: Int?,
        active: Boolean?,
        appList: ArrayList<AppInfo>
    ) : this(id, startTime, endTime, selectedWeekDays, level, active) {
        this.appList = appList
    }

    fun setAppList(list: List<Application>){
        val instance= com.trikh.focuslock.Application.instance
        var appList:ArrayList<AppInfo> = ArrayList()
        list.forEach {
            val appInfo = instance.packageManager.getApplicationInfo(it.packageName, 0)
            appList.add(
                AppInfo(
                    name = instance.packageManager.getApplicationLabel(appInfo).toString(),
                    packageName = it.packageName,
                    icon = appInfo.loadIcon(instance.packageManager),
                    blocked = true))
        }
    }
}