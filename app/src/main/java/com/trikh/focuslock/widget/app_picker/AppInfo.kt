package com.trikh.focuslock.widget.app_picker

import android.graphics.drawable.Drawable

data class AppInfo(val name: String, val icon: Drawable, var blocked: Boolean, val packageName: String)