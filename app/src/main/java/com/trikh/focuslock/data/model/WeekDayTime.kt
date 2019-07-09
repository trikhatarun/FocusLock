package com.trikh.focuslock.data.model

import androidx.room.ColumnInfo
import java.util.*

data class WeekDayTime(@ColumnInfo(name = "selected_week_days") val selectedWeekDays: Array<Boolean>? = null,
                       @ColumnInfo(name = "end_time") val endTime: Calendar )