package com.trikh.focuslock.ui.appblock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class StartServiceReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val serviceIntent = Intent(context, AppBlockService::class.java)
        serviceIntent.putExtras(intent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }
}
