package com.trikh.focuslock.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.trikh.focuslock.data.utils.BooleanListConverter
import com.trikh.focuslock.data.utils.CalendarTypeConverters
import com.trikh.focuslock.data.utils.ListConverters
import com.trikh.focuslock.widget.app_picker.AppInfo
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = "schedule")
data class Schedule(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "start_time") val startTime: Calendar,
    @ColumnInfo(name = "end_time") val endTime: Calendar,
    @Nullable
    @ColumnInfo(name = "selected_week_days") val selectedWeekDays: Array<Boolean>? = null,
    val level: Int? = -1,
    var active: Boolean? = null,
    val appList: List<String>?
) : Parcelable {

    @Ignore
    var appInfoList: ArrayList<AppInfo> = ArrayList()

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        CalendarTypeConverters.toCalender(parcel.readLong()),
        CalendarTypeConverters.toCalender(parcel.readLong()),
        BooleanListConverter.jsonToList(parcel.readString() as String),
        parcel.readInt(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        ListConverters.fetchList(parcel.createStringArrayList())
    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeLong(startTime.timeInMillis)
        parcel.writeLong(endTime.timeInMillis)
        parcel.writeString(selectedWeekDays?.let { BooleanListConverter.listToJson(it) })
        level?.let { parcel.writeInt(it) }
        parcel.writeValue(active)
        parcel.writeStringList(appList)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Schedule

        if (id != other.id) return false
        if (startTime != other.startTime) return false
        if (endTime != other.endTime) return false
        if (selectedWeekDays != null) {
            if (other.selectedWeekDays == null) return false
            if (!selectedWeekDays.contentEquals(other.selectedWeekDays)) return false
        } else if (other.selectedWeekDays != null) return false
        if (level != other.level) return false
        if (active != other.active) return false
        if (appList != other.appList) return false
        if (appInfoList != other.appInfoList) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + startTime.hashCode()
        result = 31 * result + endTime.hashCode()
        result = 31 * result + (selectedWeekDays?.contentHashCode() ?: 0)
        result = 31 * result + (level ?: 0)
        result = 31 * result + (active?.hashCode() ?: 0)
        result = 31 * result + (appList?.hashCode() ?: 0)
        result = 31 * result + appInfoList.hashCode()
        return result
    }

    companion object CREATOR : Parcelable.Creator<Schedule> {
        override fun createFromParcel(parcel: Parcel): Schedule {
            return Schedule(parcel)
        }

        override fun newArray(size: Int): Array<Schedule?> {
            return arrayOfNulls(size)
        }
    }
}