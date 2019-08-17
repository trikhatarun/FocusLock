package com.trikh.focuslock.utils.extensions

import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import com.trikh.focuslock.Application
import com.trikh.focuslock.R
import com.trikh.focuslock.data.model.Error
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.ui.schedule.PopupMenuOption
import com.trikh.focuslock.utils.Constants.Companion.MENU_DELETE
import com.trikh.focuslock.utils.Constants.Companion.MENU_DISABLE
import com.trikh.focuslock.utils.Constants.Companion.MENU_EDIT
import com.trikh.focuslock.utils.Constants.Companion.MENU_ENABLE
import org.json.JSONObject
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Calendar.roundMinutesToFive: Calendar
    get() {
        val minutes = this.get(Calendar.MINUTE)
        val roundedMinutes = (minutes / 5f).roundToInt() * 5
        set(Calendar.MINUTE, roundedMinutes)
        return this
    }

val Context.hasUsageStatsPermission: Boolean
    get() {
        val granted: Boolean
        val appOpsManager = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOpsManager.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            packageName
        )
        granted = if (mode == AppOpsManager.MODE_DEFAULT) {
            (checkCallingOrSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED)
        } else {
            (mode == AppOpsManager.MODE_ALLOWED)
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

val Schedule.getMenuOptions: List<PopupMenuOption>
    get() {
        val menuOptionList = ArrayList<PopupMenuOption>()
        menuOptionList.add(PopupMenuOption(R.drawable.ic_edit, Application.instance.getString(R.string.edit), MENU_EDIT))
        if (level == -1) {
            if (active!!) {
                menuOptionList.add(PopupMenuOption(R.drawable.ic_disable, Application.instance.getString(R.string.disable), MENU_DISABLE))
            } else {
                menuOptionList.add(PopupMenuOption(R.drawable.ic_enable, Application.instance.getString(R.string.enable), MENU_ENABLE))
            }
            menuOptionList.add(PopupMenuOption(R.drawable.ic_delete, Application.instance.getString(R.string.delete), MENU_DELETE))
        }
        return menuOptionList
    }