package com.trikh.focuslock.widget.timepicker

import java.util.*

public interface OnSliderRangeMovedListener {
    fun onChange(start: Calendar, end: Calendar)
}