package com.trikh.focuslock.utils.extensions

import android.content.res.Resources
import java.util.*

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Calendar.roundMinutesToFive: Calendar
    get() {
        val minutes = this.get(Calendar.MINUTE)
        val roundedMinutes = Math.round(minutes / 5f) * 5
        set(Calendar.MINUTE, roundedMinutes)
        return this
    }

val Long.addOneDay: Long
    get() {
        return this.plus(86400000)
    }