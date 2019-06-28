package com.trikh.focuslock.data.utils

import java.util.*

class RunningTime {
    companion object {
        fun getRunningTime(list: List<Calendar>): Long {
            val systemTime = System.currentTimeMillis()
            var smallestRunningTime = list[0].timeInMillis
            list.forEach {
                val runningTime = it.timeInMillis - systemTime

                if (runningTime > 0) {

                    if (runningTime < smallestRunningTime) {
                        smallestRunningTime = runningTime
                    }

                }
            }

            return smallestRunningTime
        }
    }
}