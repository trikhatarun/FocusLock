package com.trikh.focuslock.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.content.getSystemService
import com.trikh.focuslock.ui.appblock.StartServiceReceiver
import java.util.*

class AlarmManager(private val context: Context) {
    fun setScheduleAlarm(time: Calendar, requestCode: Int, type: Int) {
        context.let { context ->
            val intent = Intent(context, StartServiceReceiver::class.java)
            intent.putExtra(Constants.SCHEDULE_TYPE, type)
            val pendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

            val alarmManager = context.getSystemService<AlarmManager>()
            alarmManager?.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                time.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
    }
}