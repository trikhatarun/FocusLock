package com.trikh.focuslock.utils

interface Constants {
    companion object {
        const val NOTIFICATION_CHANNEL_ID = "app_block_channel"
        const val SERVICE_ID = 1
        const val TIME_INTERVAL: Long = 1000
        const val PACKAGE_NAME = "package_info"
        const val SCHEDULE_TYPE = "schedule_type"
        const val INSTANT_LOCK = 1
        const val SCHEDULE = 2
        const val DAILY_SCHEDULE = 100
        const val CUSTOM_SCHEDULE = 3
        const val FEEDBACK_EMAIL = "trikha.tarun01@gmail.com"
        const val PLAIN_TEXT = "text/plain"
        const val SHARE_FOCUS_LOCK = "Share Focus Lock"

        const val MY_PREF = "focus_lock_pref"

        const val ON_BOARDING = "on_Boarding"
        const val DEFAULT_ON_BOARDING = true

        const val MENU_EDIT: Long = 1
        const val MENU_DELETE: Long = 2
        const val MENU_ENABLE: Long = 3
        const val MENU_DISABLE: Long = 4
    }
}