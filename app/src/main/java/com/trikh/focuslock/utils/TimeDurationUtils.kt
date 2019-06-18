package com.trikh.focuslock.utils

import java.util.*

class TimeDurationUtils{
    companion object{
        @JvmStatic
        fun calculateDuration(startTime: Calendar, endTime: Calendar): String {
            val sleepTime = startTime.timeInMillis
            var awakeTime = endTime.timeInMillis
            if (sleepTime > awakeTime) awakeTime +=  86400000
            val difference = (awakeTime - sleepTime) / 60000 // in minutes
            val hours = difference / 60
            val minutes = Math.round(difference.rem(60) / 10f) * 10
            return "$hours hr $minutes min"
        }
    }
}