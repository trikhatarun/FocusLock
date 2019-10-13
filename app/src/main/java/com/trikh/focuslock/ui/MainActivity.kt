package com.trikh.focuslock.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.trikh.focuslock.R
import com.trikh.focuslock.ui.appblock.StartServiceReceiver
import com.trikh.focuslock.ui.settings.SettingsFragment
import com.trikh.focuslock.utils.BaseActivity
import com.trikh.focuslock.utils.extensions.hasUsageStatsPermission
import com.trikh.focuslock.widget.arctoolbar.setAppBarLayout
import com.trikh.focuslock.widget.customdialog.CustomDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

class MainActivity : BaseActivity(),
    SettingsFragment.OnFragmentInteractionListener {

    override fun onFragmentInteraction(uri: Uri) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        if (Intent.ACTION_VIEW == intent?.action) {
            Toast.makeText(this, "Open via link", Toast.LENGTH_SHORT).show()
        }
    }

    fun startService() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, StartServiceReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent)
        setPrimaryScheduleActive()
    }

    private fun setPrimaryScheduleActive() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, StartServiceReceiver::class.java).apply {
            putExtra("SetPrimaryScheduleActive", true)
        }
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        val cal = Calendar.getInstance()
        cal.add(Calendar.HOUR, 12)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pendingIntent)
    }
}
