package com.trikh.focuslock.ui.appblock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class StartServiceReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.e("onReceive:", "Start")
        val serviceIntent = Intent(context, AppBlockService::class.java)
        serviceIntent.putExtras(intent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
        Log.e("onReceive:", "Start")
    }
}
