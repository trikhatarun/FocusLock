package com.trikh.focuslock.ui

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.trikh.focuslock.R
import com.trikh.focuslock.ui.appblock.StartServiceReceiver
import com.trikh.focuslock.ui.settings.SettingsFragment
import com.trikh.focuslock.utils.Constants
import com.trikh.focuslock.utils.extensions.hasUsageStatsPermission
import com.trikh.focuslock.widget.arctoolbar.setAppBarLayout
import com.trikh.focuslock.widget.customdialog.CustomDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

class MainActivity : AppCompatActivity(),
    SettingsFragment.OnFragmentInteractionListener {

    private var pref: SharedPreferences? = null

    override fun onFragmentInteraction(uri: Uri) {

    }

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pref = getSharedPreferences(Constants.MY_PREF, 0)
        val editor = pref!!.edit()
        editor.putString(Constants.TYPE, Constants.DEFAULT_TYPE)
        editor.apply()

        arcToolbar.setAppBarLayout(appbar)

        bottomNavigationBar.itemIconTintList = null
        bottomNavigationBar.setupWithNavController(findNavController(R.id.container))
    }

    override fun onResume() {
        super.onResume()
        if (!hasUsageStatsPermission) {
            CustomDialog(
                R.string.usage_permission_request_message,
                this::requestUsagePermission,
                R.string.grant,
                R.string.deny
            ).show(supportFragmentManager, null)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (Intent.ACTION_VIEW.equals(intent?.action)) {
            Toast.makeText(this, "Open via link", Toast.LENGTH_SHORT).show()
        }
    }

    fun onAddScheduleClick(v: View) {
        val editor = pref!!.edit()
        editor.putString(Constants.TYPE, Constants.DEFAULT_TYPE)
        editor.apply()
        findNavController(R.id.container).navigate(R.id.customSchedule)
        fabMenu.toggle(true)
    }

    fun onInstantLockClick(v: View) {
        val editor = pref!!.edit()
        editor.putString(Constants.TYPE, Constants.DEFAULT_TYPE)
        editor.apply()
        findNavController(R.id.container).navigate(R.id.instantLock)
        fabMenu.toggle(true)
    }

    override fun onBackPressed() {

    }

    fun startService() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, StartServiceReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent)
        setPrimaryScheduleActive()
    }

    fun setPrimaryScheduleActive() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, StartServiceReceiver::class.java)
        intent.putExtra("SetPrimaryScheduleActive", true)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        val cal = Calendar.getInstance()
        cal.add(Calendar.HOUR, 12)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pendingIntent)
    }

    private fun requestUsagePermission() {
        startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
    }
}
