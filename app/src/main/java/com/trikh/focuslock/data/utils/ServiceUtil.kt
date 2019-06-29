package com.trikh.focuslock.data.utils

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
                if ( System.currentTimeMillis() in it.startTime.timeInMillis until it.endTime.timeInMillis ) {

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