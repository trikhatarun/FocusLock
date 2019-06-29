package com.trikh.focuslock.ui.onboarding

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.trikh.focuslock.R
import com.trikh.focuslock.ui.appblock.StartServiceReceiver
import com.trikh.focuslock.ui.onboarding.blockapps.BlockedAppsFragment
import com.trikh.focuslock.utils.Constants
import java.util.*

class OnboardingActivity : AppCompatActivity(), BlockedAppsFragment.InteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
    }

    fun setSchedule(calender: Calendar, type: Int) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, StartServiceReceiver::class.java)
        intent.putExtra(Constants.SCHEDULE_TYPE, type)
        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calender.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
    }
}
