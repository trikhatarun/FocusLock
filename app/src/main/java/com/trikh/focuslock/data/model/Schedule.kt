package com.trikh.focuslock.data.model

import java.util.*

data class Schedule(
    val id: Int,
    val startTime: Calendar,
    val endTime: Calendar,
    val level: Int,
    val active: Boolean
)