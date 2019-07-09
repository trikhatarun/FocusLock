package com.trikh.focuslock.data.utils

import android.util.Log
import com.google.gson.Gson
import com.trikh.focuslock.widget.app_picker.AppInfo

class ListConverters {
    companion object {


        fun fetchList(appList: List<String>): List<String> {
            Log.d("ListConverter: ", " AppList: $appList")
            val list = ArrayList<String>()
            appList.forEach {
                if (it != null) {
                    list.add(it)
                }
            }
            return list
        }
    }


}