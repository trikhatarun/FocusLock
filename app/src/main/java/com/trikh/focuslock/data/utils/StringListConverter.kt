package com.trikh.focuslock.data.utils

import androidx.room.TypeConverter
import com.google.gson.Gson


class StringListConverter {
    companion object{
        @JvmStatic
        @TypeConverter
        fun listToJson(value: List<String>): String {
            return Gson().toJson(value)
        }

        @JvmStatic
        @TypeConverter
        fun jsonToList(value: String): List<String>? {
            val objects = Gson().fromJson(value, Array<String>::class.java) as Array<String>
            return objects.toList()
        }
    }
}