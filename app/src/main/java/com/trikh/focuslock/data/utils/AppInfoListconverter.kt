package com.trikh.focuslock.data.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.trikh.focuslock.widget.app_picker.AppInfo

class AppInfoListconverter {
    companion object{
        @JvmStatic
        @TypeConverter
        fun listToJson(value: List<AppInfo>): String {
            return Gson().toJson(value)
        }

        @JvmStatic
        @TypeConverter
        fun jsonToList(value: String): List<AppInfo>? {
            val objects = Gson().fromJson(value, Array<String>::class.java) as Array<AppInfo>
            return objects.toList()
        }
    }
}