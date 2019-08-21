package com.trikh.focuslock.ui.appblock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.trikh.focuslock.data.source.ScheduleRepository
import io.reactivex.rxkotlin.subscribeBy

class StartServiceReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val serviceIntent = Intent(context, AppBlockService::class.java)
        serviceIntent.putExtras(intent)
        val active = intent.getBooleanExtra("SetPrimaryScheduleActive", false)
        if (active) {

            setPrimaryScheduleActive(context, serviceIntent)

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent)
            } else {
                context.startService(serviceIntent)
            }
        }
    }

    private fun setPrimaryScheduleActive(context: Context, serviceIntent: Intent) {
        val repository = ScheduleRepository()

        repository.setPrimaryScheduleActive().subscribeBy {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent)
            } else {
                context.startService(serviceIntent)
            }
        }
    }
}
