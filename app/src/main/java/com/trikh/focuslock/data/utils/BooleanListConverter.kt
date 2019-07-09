package com.trikh.focuslock.data.utils

import androidx.room.TypeConverter
import com.google.gson.Gson

class BooleanListConverter{
    companion object{
        @JvmStatic
        @TypeConverter
        fun listToJson(value: Array<Boolean>): String{
            return Gson().toJson(value)
        }

        @JvmStatic
        @TypeConverter
        fun jsonToList(value: String): Array<Boolean>{
            val objects =  Gson().fromJson(value, Array<Boolean>::class.java) as Array<Boolean>
            return objects
        }

    }
}