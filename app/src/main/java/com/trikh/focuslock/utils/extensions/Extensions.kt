package com.trikh.focuslock.utils.extensions

import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import com.trikh.focuslock.data.model.Error
import org.json.JSONObject
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
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

val Context.hasUsageStatsPermission: Boolean
    get() {
        var granted = false
        val appOpsManager = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOpsManager.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            packageName
        )
        if (mode == AppOpsManager.MODE_DEFAULT) {
            granted =
                (checkCallingOrSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED)
        } else {
            granted = (mode == AppOpsManager.MODE_ALLOWED)
        }
        return granted
    }

val Throwable.error: Error
    get() {
        return when (this) {
            is HttpException -> {
                JSONObject(this@error.response()?.errorBody()?.string()).run {
                    Error(response()?.code(), getString("message"))
                }
            }
            is UnknownHostException -> {
                Error(null, "No Internet Connection")
            }
            is SocketTimeoutException -> {
                Error(null, "Server is taking to long to respond")
            }
            else -> Error(null, message)
        }
    }