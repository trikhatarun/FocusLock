package com.trikh.focuslock.ui.onboarding.splash

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.service.media.MediaBrowserService
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.trikh.focuslock.R
import com.trikh.focuslock.ui.appblock.StartServiceReceiver
import com.trikh.focuslock.utils.Constants
import java.util.*
import kotlin.concurrent.schedule

class SplashFragment : Fragment() {

    private var onBoarding = Constants.DEFAULT_ON_BOARDING
    private lateinit var root: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        onBoarding = context!!.getSharedPreferences(Constants.MY_PREF, 0)
            .getBoolean(Constants.ON_BOARDING, Constants.DEFAULT_ON_BOARDING)

        when (onBoarding) {
            true -> {
                activity?.theme?.applyStyle(R.style.AppTheme, true)
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToSlidesFragment())
            }
            false -> {
                activity?.theme?.applyStyle(R.style.AppTheme, true)
                startService()
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToNavGraph())
            }
        }


        return super.onCreateView(inflater,container, savedInstanceState)
    }

    fun startService() {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, StartServiceReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0 , intent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent)
        //setPrimaryScheduleActive()
    }
}