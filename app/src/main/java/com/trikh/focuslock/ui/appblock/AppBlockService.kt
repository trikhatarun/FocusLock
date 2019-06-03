package com.trikh.focuslock.ui.appblock

import android.app.ActivityManager
import android.app.Service
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.trikh.focuslock.R
import com.trikh.focuslock.data.model.InstantLockSchedule
import com.trikh.focuslock.data.source.ScheduleRepository
import com.trikh.focuslock.utils.Constants.Companion.DAILY_SCHEDULE
import com.trikh.focuslock.utils.Constants.Companion.INSTANT_LOCK
import com.trikh.focuslock.utils.Constants.Companion.NOTIFICATION_CHANNEL_ID
import com.trikh.focuslock.utils.Constants.Companion.PACKAGE_NAME
import com.trikh.focuslock.utils.Constants.Companion.SCHEDULE
import com.trikh.focuslock.utils.Constants.Companion.SCHEDULE_TYPE
import com.trikh.focuslock.utils.Constants.Companion.SERVICE_ID
import com.trikh.focuslock.utils.Constants.Companion.TIME_INTERVAL
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class AppBlockService : Service() {
    private val compositeDisposable = CompositeDisposable()
    private var runningTime: Long = 0
    private lateinit var blockedPackages: List<String>
    private val usageStatsManager by lazy { getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager }
    private val activityManager by lazy { getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.app_blocked_message))
            .build()
        startForeground(SERVICE_ID, notification)

        when (intent?.getIntExtra(SCHEDULE_TYPE, -1)) {
            INSTANT_LOCK -> {
                val schedule = intent.getParcelableExtra<InstantLockSchedule>(SCHEDULE)
                runningTime = schedule.endTime - System.currentTimeMillis()
                blockedPackages = schedule.blockedApps
            }
            DAILY_SCHEDULE -> {
                // fetch all schedule to be started now
            }
            else -> stopSelf()
        }

        Observable.interval(TIME_INTERVAL, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.computation())
            .map { getForegroundApp() }
            .filter { blockedPackages.contains(it) && it != packageName }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { p ->
                val i = Intent(Intent.ACTION_MAIN)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                i.addCategory(Intent.CATEGORY_HOME)
                this.startActivity(i)
                val blockedAppIntent = Intent(this, BlockedAppActivity::class.java)
                blockedAppIntent.putExtra(PACKAGE_NAME, p)
                blockedAppIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(blockedAppIntent)
                activityManager.killBackgroundProcesses(p)
            }.addTo(compositeDisposable)

        Observable.timer(runningTime, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.computation())
            .subscribe {
                ScheduleRepository().deleteInstantLock()
                stopSelf()
            }.addTo(compositeDisposable)
        return START_NOT_STICKY;
    }

    private fun getForegroundApp(): String {
        var foregroundApp = ""
        val time = System.currentTimeMillis()
        val usageEvents = usageStatsManager.queryEvents(time - 10000, time)
        val event = UsageEvents.Event()
        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event)
            if (event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                foregroundApp = event.packageName
            }
        }
        return foregroundApp
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
