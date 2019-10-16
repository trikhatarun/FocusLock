package com.trikh.focuslock.ui.appblock

import android.app.ActivityManager
import android.app.Service
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.trikh.focuslock.Application
import com.trikh.focuslock.R
import com.trikh.focuslock.data.source.ScheduleRepository
import com.trikh.focuslock.utils.Constants.Companion.NOTIFICATION_CHANNEL_ID
import com.trikh.focuslock.utils.Constants.Companion.PACKAGE_NAME
import com.trikh.focuslock.utils.Constants.Companion.SERVICE_ID
import com.trikh.focuslock.utils.Constants.Companion.TIME_INTERVAL
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

class AppBlockService : Service() {
    private val compositeDisposable = CompositeDisposable()
    private var runningTime: Long = 0
    private lateinit var blockedPackages: List<String>
    private val usageStatsManager by lazy { getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager }
    private val activityManager by lazy { getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager }
    private val scheduleRepository = ScheduleRepository()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        start()
        return START_STICKY
    }

    private fun start() {
        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.app_blocked_message))
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(ContextCompat.getColor(Application.instance, R.color.colorPrimary))
            .build()
        startForeground(SERVICE_ID, notification)
        val runningTimeObservable = scheduleRepository.getRunningTime()

        scheduleRepository.getBlockedPackages().subscribeBy {
            blockedPackages = it
            runningTimeObservable.subscribe{time ->
                runningTime = if (blockedPackages.isEmpty()) {
                    0
                } else {
                    time
                }
                setTimeAndPackages(time = runningTime, packages = blockedPackages)
            }
        }.addTo(compositeDisposable)
    }

    private fun setTimeAndPackages(time: Long, packages: List<String>) {
        runningTime = time
        val cal = Calendar.getInstance()
        cal.timeInMillis = 600000

        blockedPackages = packages

        if (runningTime > 0) {
            setInterval()
        } else {
            stopSelf()
        }
    }

    private fun setInterval() {
        //checking is blocked app is opening
        Observable.interval(TIME_INTERVAL, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.computation())
            .map { getForegroundApp() }
            .filter { blockedPackages.contains(it) && it != packageName }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { p ->
                val homeIntent = Intent(Intent.ACTION_MAIN).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    addCategory(Intent.CATEGORY_HOME)
                }
                startActivity(homeIntent)

                val blockedAppIntent = Intent(this, BlockedAppActivity::class.java).apply {
                    putExtra(PACKAGE_NAME, p)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(blockedAppIntent)

                activityManager.killBackgroundProcesses(p)
            }.addTo(compositeDisposable)
        //timer for the smallest endTime
        Observable.timer(runningTime, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.computation())
            .subscribe {
                start()
                //ScheduleRepository().deleteInstantLock()
                //stopSelf()
            }.addTo(compositeDisposable)
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
