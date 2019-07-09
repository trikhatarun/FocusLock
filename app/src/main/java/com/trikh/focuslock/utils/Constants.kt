package com.trikh.focuslock.utils

interface Constants {
    companion object {
        const val NOTIFICATION_CHANNEL_ID = "app_block_channel"
        const val SERVICE_ID = 1
        const val TIME_INTERVAL: Long = 1000
        const val PACKAGE_NAME = "package_info"
        const val SCHEDULE_TYPE = "schedule_type"
        const val INSTANT_LOCK = 1
        const val DAILY_SCHEDULE = 2
        const val CUSTOM_SCHEDULE = 3
        const val SCHEDULE = "schedule"
        const val FEEDBACK_EMAIL = "trikha.tarun01@gmail.com"
        const val PLAIN_TEXT = "text/plain"
        const val SHARE_FOCUS_LOCK = "Share Focus Lock"

        const val END_TIME = "End Time"
        const val START_TIME = "Start Time"
        const val TYPE = "type"
        const val MY_PREF = "focus_lock_pref"
        const val POPUP_EDIT = "edit"
        const val POPUP_DISABLE = "disable"
        const val POPUP_ENABLE = "enable"
        const val POPUP_DELETE = "delete"
        const val DEFAULT_TYPE = "no_type"
        const val POPUP_INSTANT_EDIT = "instant_edit"

        const val ON_BOARDING = "on_Boarding"
        const val DEFAULT_ON_BOARDING = true
    }
}