package com.trikh.focuslock.widget.app_picker

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.*


class AppPickerDialog : DialogFragment() {

    private val packageManager: PackageManager by lazy { activity!!.packageManager }
    private var blockAppsSet: Set<String>? = null

    val applicationListObservable = Observable.fromCallable {
        packageManager.getInstalledApplications(0)
    }.subscribeOn(Schedulers.io())
        .observeOn(Schedulers.computation())
        .filter { installedApps ->
            /*for (app in installedApps) {
                val appName: String
                val packageName: String
                val icon: Drawable

                if (app.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
                    appName = packageManager.getApplicationLabel(app) as String
                    packageName = app.packageName
                    icon = app.loadIcon(packageManager)
                    blockAppsSet?.let {
                        if (it.contains(packageName)) {
                            appInfoList.add(AppInfo(appName, icon, true, packageName))
                        } else {
                            appInfoList.add(AppInfo(appName, icon, false, packageName))
                        }
                    }
                }
            }*/
            if (it.contains(packageName)) {
                appInfoList.add(AppInfo(appName, icon, true, packageName))
            } else {
                appInfoList.add(AppInfo(appName, icon, false, packageName))
            }
        }.sorted { o1, o2 ->
            o1.getAppName().compareToIgnoreCase(o1.getAppName())
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}