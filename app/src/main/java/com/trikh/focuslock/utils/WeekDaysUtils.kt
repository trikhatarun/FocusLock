package com.trikh.focuslock.utils

import kotlinx.android.synthetic.main.schedule_layout.view.*
import java.util.*

object WeekDaysUtils {

    fun getNextWeek(selectedWeekDays: Array<Boolean>, endTime: Calendar): String {
        val cal: Calendar = Calendar.getInstance()
        val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)

        if (cal.timeInMillis <= endTime.timeInMillis) {
            return "Today"
        } else {
            var flag = false
            var weekDay: Int? = null
            for (i in dayOfWeek..7) {
                if (selectedWeekDays?.get(i - 1)!!) {
                    flag = true
                    weekDay = i - 1
                    break
                }
            }
            if (!flag) {
                for (i in 1..dayOfWeek) {
                    if (selectedWeekDays?.get(i - 1)!!) {
                        flag = true
                        weekDay = i - 1
                        break
                    }
                }
            }
            if (flag) {
                when (weekDay) {
                    0 -> {
                        return "Sunday"
                    }
                    1 -> {
                        return "Monday"
                    }
                    2 -> {
                        return "Tuesday"
                    }
                    3 -> {
                        return "Wednesday"
                    }
                    4 -> {
                        return "Thursday"
                    }
                    5 -> {
                        return "Friday"
                    }
                    else -> {
                        return "Saturday"
                    }
                }
            } else {
                return "No Repeat"
            }
        }

    }
}