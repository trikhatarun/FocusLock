package com.trikh.focuslock.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "instant_lock")
class InstantLockSchedule(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val endTime: Long,
    val blockedApps: List<String>
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readLong(),
        parcel.createStringArrayList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeLong(endTime)
        parcel.writeStringList(blockedApps)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InstantLockSchedule> {
        override fun createFromParcel(parcel: Parcel): InstantLockSchedule {
            return InstantLockSchedule(parcel)
        }

        override fun newArray(size: Int): Array<InstantLockSchedule?> {
            return arrayOfNulls(size)
        }
    }
}