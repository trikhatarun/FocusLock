package com.trikh.focuslock.data.model

import android.content.Context
import android.content.pm.PackageManager
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.trikh.focuslock.data.utils.*
import com.trikh.focuslock.widget.app_picker.AppInfo
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = "schedule")
data class Schedule(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "start_time") val startTime: Calendar,
    @ColumnInfo(name = "end_time") val endTime: Calendar,
    @ColumnInfo(name = "selected_week_days") val selectedWeekDays: Array<Boolean>? = null,
    val level: Int? = -1,
    var active: Boolean? = null,
    val appList: List<String>?
) : Parcelable {

    @Ignore
    var appInfoList: ArrayList<AppInfo> = ArrayList()


    /* constructor(
         id: Int,
         startTime: Calendar,
         endTime: Calendar,
         selectedWeekDays: Array<Boolean>?,
         level: Int?,
         active: Boolean?,
         appList: ArrayList<AppInfo>
     ) : this(id, startTime, endTime, selectedWeekDays, level, active) {
         this.appList = appList
     }

     fun setAppList(list: List<Application>){
         val instance= com.trikh.focuslock.Application.instance
         var appList:ArrayList<AppInfo> = ArrayList()
         list.forEach {
             val appInfo = instance.packageManager.getApplicationInfo(it.packageName, 0)
             appList.add(
                 AppInfo(
                     name = instance.packageManager.getApplicationLabel(appInfo).toString(),
                     packageName = it.packageName,
                     icon = appInfo.loadIcon(instance.packageManager),
                     blocked = true))
         }
     }*/
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        CalendarTypeConverters.toCalender(parcel.readLong()),
        CalendarTypeConverters.toCalender(parcel.readLong()),
        BooleanListConverter.jsonToList(parcel.readString() as String),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        ListConverters.fetchList(parcel.createStringArrayList())
        //ListConverters.stringToList(parcel.readString() as String)
        //ListConverters.stringToList(parcel.readString())
    ) {
        //this.appInfoList = ArrayList()
        //parcel.readList(this.appInfoList,AppInfo::class.java.classLoader)
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeLong(startTime.timeInMillis)
        parcel.writeLong(endTime.timeInMillis)
        parcel.writeString(selectedWeekDays?.let { BooleanListConverter.listToJson(it) })
        level?.let { parcel.writeInt(it) }
        parcel.writeByte(1)
        parcel.writeStringList(appList)
        //parcel.writeString(ListConverters.listToString(appList!!))
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Schedule> {
        override fun createFromParcel(parcel: Parcel): Schedule {
            return Schedule(parcel)
        }

        override fun newArray(size: Int): Array<Schedule?> {
            return arrayOfNulls(size)
        }

    }


    /*constructor(parcel: Parcel) : this(
        parcel.readInt(),
        TODO("startTime"),
        TODO("endTime"),
        TODO("selectedWeekDays"),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.createStringArrayList()

    ) {
        parcel.readArrayList(AppInfo::class.java.classLoader) as ArrayList<AppInfo>
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeValue(level)
        parcel.writeValue(active)
        parcel.writeStringList(appList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Schedule> {
        override fun createFromParcel(parcel: Parcel): Schedule {
            return Schedule(parcel)
        }

        override fun newArray(size: Int): Array<Schedule?> {
            return arrayOfNulls(size)
        }
    }*/


}