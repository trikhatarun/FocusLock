package com.trikh.focuslock.data.utils

import androidx.room.TypeConverter
import java.util.*

class CalendarTypeConverters {
    companion object {
        @JvmStatic
        @TypeConverter
        fun toCalender(value: Long): Calendar {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = value
            return calendar
        }

        @JvmStatic
        @TypeConverter
        fun toLong(value: Calendar): Long {
            return value.timeInMillis
        }
    }
}