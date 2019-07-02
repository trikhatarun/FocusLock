package com.trikh.focuslock.data.utils

import android.util.Log
import com.trikh.focuslock.data.model.Schedule
import java.util.*
import kotlin.collections.ArrayList

class ServiceUtil {
    companion object {

        @JvmStatic
        fun getRunningTime(list: List<Calendar>): Long {
            val systemTime = System.currentTimeMillis()
            var smallestRunningTime = 0L

            list.forEach {
                if (it.timeInMillis > smallestRunningTime){
                    smallestRunningTime = it.timeInMillis
                }
            }

            //var previousTime = list[0].timeInMillis
            list.forEach {
                //val runningTime = it.timeInMillis - systemTime

                if (it.timeInMillis in systemTime until smallestRunningTime) {

                        //previousTime = runningTime
                        smallestRunningTime = it.timeInMillis


                }
            }

            return smallestRunningTime
        }

        @JvmStatic
        fun getAllBlockedPackages(allPackages: List<Schedule>): ArrayList<String> {
            val blockedPackageList = ArrayList<String>()
            allPackages.forEach {
                val cal = Calendar.getInstance()
                val day = cal.get(Calendar.DAY_OF_WEEK) - 1

                val weekDay = it.selectedWeekDays?.get(day) != null
                Log.d("getAllBlockedPackages","weekDay: $weekDay")
                if ( (System.currentTimeMillis() in it.startTime.timeInMillis until it.endTime.timeInMillis) && (weekDay)) {

                    it.appList?.forEach {
                        if (!blockedPackageList.contains(it)) {
                            blockedPackageList.add(it)
                        }
                    }

                }
            }

            return blockedPackageList

        }


    }
}