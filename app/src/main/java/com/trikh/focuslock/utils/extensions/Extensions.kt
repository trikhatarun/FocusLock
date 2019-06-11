package com.trikh.focuslock.utils.extensions

import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import com.trikh.focuslock.widget.customdialog.CustomDialog
import java.util.*

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Calendar.roundMinutesToFive: Calendar
    get() {
        val minutes = this.get(Calendar.MINUTE)
        val roundedMinutes = Math.round(minutes / 5f) * 5
        set(Calendar.MINUTE, roundedMinutes)
        return this
    }

val Long.addOneDay: Long
    get() {
        return this.plus(86400000)
    }

val Context.hasUsageStatsPermission : Boolean
    get() {
        var granted = false
        val appOpsManager = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,android.os.Process.myUid(),packageName)
        if (mode == AppOpsManager.MODE_DEFAULT){
            granted = (checkCallingOrSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED)
        }else{
            granted = (mode == AppOpsManager.MODE_ALLOWED)
        }
        return granted
    }