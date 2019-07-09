package com.trikh.focuslock.widget.app_picker

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import kotlinx.android.parcel.Parcelize

data class AppInfo(val name: String, val icon: Drawable, var blocked: Boolean, val packageName: String): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readParcelable<Bitmap>(Bitmap::class.java.classLoader).toDrawable(Resources.getSystem()),
        parcel.readByte() != 0.toByte(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        //icon.toBitmap().writeToParcel(parcel, 0)
        //parcel.setDataPosition(flags)
        parcel.writeParcelable(icon.toBitmap(),flags)
        parcel.writeByte(if (blocked) 1 else 0)
        parcel.writeString(packageName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AppInfo> {
        override fun createFromParcel(parcel: Parcel): AppInfo {
            return AppInfo(parcel)
        }

        override fun newArray(size: Int): Array<AppInfo?> {
            return arrayOfNulls(size)
        }
    }
}