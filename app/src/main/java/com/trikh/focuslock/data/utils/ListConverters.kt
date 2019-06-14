package com.trikh.focuslock.data.utils

import android.util.Log
import com.google.gson.Gson

class ListConverters {
    companion object {

        fun listToString(value: List<String>): String {
            val st = Gson().toJson(value) as String
            Log.e("ListConverter: ", " String: $st")
            return st
        }


        fun stringToList(value: String): List<String> {
            Log.e("ListConverter: ", " String TO Convert: ${value}")
            val objects = Gson().fromJson(value, Array<String>::class.java)
            Log.e("ListConverter: ", " AppList: ${objects}")
            return objects.asList()
        }


        fun listToArray(appList: List<String>): Array<String> {
            var array = arrayOfNulls<String>(appList.size)
            for (i in 0 until appList.size) {
                array[i] = appList[i]
            }
            Log.e("ListConverter: ", " Array: $array")
            return array as Array<String>
        }

        fun arrayToList(array: Array<String>): List<String> {
            var list = ArrayList<String>()
            for (i in 0 until array.size) {
                 list[i] = array[i]
            }
            Log.e("ListConverter: ", " AppList: $list")
            return list
        }

        fun fetchList(appList: List<String>): List<String> {
            Log.e("ListConverter: ", " AppList: $appList")
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