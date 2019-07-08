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
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import androidx.navigation.ui.setupWithNavController
import com.trikh.focuslock.R
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.ui.appblock.StartServiceReceiver
import com.trikh.focuslock.ui.schedule.ScheduleFragment
import com.trikh.focuslock.ui.schedule.customschedule.CustomScheduleActivityArgs

import com.trikh.focuslock.ui.settings.SettingsFragment
import com.trikh.focuslock.utils.Constants
import com.trikh.focuslock.utils.extensions.hasUsageStatsPermission
import com.trikh.focuslock.widget.arctoolbar.setAppBarLayout
import com.trikh.focuslock.widget.customdialog.CustomDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), ScheduleFragment.OnFragmentInteractionListener,
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
        if (!hasUsageStatsPermission){
            CustomDialog(R.string.usage_permission_request_message,this::requestUsagePermission,R.string.grant,R.string.deny).show(supportFragmentManager,null)
        }
    }

    override fun onNewIntent(intent: Intent?) {
            super.onNewIntent(intent)
        if (Intent.ACTION_VIEW.equals(intent?.action)){
            Toast.makeText(this,"Open via link",Toast.LENGTH_SHORT).show()

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
        val pendingIntent = PendingIntent.getBroadcast(this, 5, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent)
        setPrimaryScheduleActive()
    }

    fun setPrimaryScheduleActive(){
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, StartServiceReceiver::class.java)
        intent.putExtra("SetPrimaryScheduleActive", true)
        val pendingIntent = PendingIntent.getBroadcast(this, 22, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH,1)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pendingIntent)
    }




    private fun requestUsagePermission(){
        startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
    }
}
