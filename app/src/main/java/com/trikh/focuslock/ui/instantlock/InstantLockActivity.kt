package com.trikh.focuslock.ui.instantlock

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.trikh.focuslock.R
import com.trikh.focuslock.data.model.InstantLockSchedule
import com.trikh.focuslock.databinding.ActivityInstantLockBinding
import com.trikh.focuslock.ui.appblock.AppBlockService
import com.trikh.focuslock.ui.appblock.StartServiceReceiver
import com.trikh.focuslock.ui.schedule.BlockedAppsAdapter
import com.trikh.focuslock.utils.AutoFitGridLayoutManager
import com.trikh.focuslock.utils.Constants.Companion.BLOCKED_PACKAGE_LIST
import com.trikh.focuslock.utils.Constants.Companion.INSTANT_LOCK
import com.trikh.focuslock.utils.Constants.Companion.SCHEDULE
import com.trikh.focuslock.utils.Constants.Companion.SCHEDULE_TYPE
import com.trikh.focuslock.widget.app_picker.AppInfo
import com.trikh.focuslock.widget.app_picker.AppPickerDialog
import com.trikh.focuslock.widget.arctoolbar.setAppBarLayout
import kotlinx.android.synthetic.main.activity_add_schedule.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

class InstantLockActivity : AppCompatActivity(), AppPickerDialog.InteractionListener {

    private lateinit var binding: ActivityInstantLockBinding
    private lateinit var viewModel: InstantLockViewModel
    private lateinit var blockedAppsAdapter: BlockedAppsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_instant_lock)
        viewModel = ViewModelProviders.of(this).get(InstantLockViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        arcToolbar.setAppBarLayout(appbar)
        setSupportActionBar(toolbar)
        toolbar_title.text = getString(R.string.instant_lock)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.appPicker.observe(this, Observer {
            if (!it.hasBeenHandled) {
                AppPickerDialog(
                        viewModel.applicationList.value!!,
                        this
                ).show(supportFragmentManager, "appPicker")
                it.getContentIfNotHandled()
            }
        })

        viewModel.applicationList.observe(this, Observer {
            blockedAppsAdapter.updateList(it)
            blocked_apps_title.text = getString(R.string.blocked_apps, it.size)
        })

        blocked_apps_rv.layoutManager = AutoFitGridLayoutManager(this, 48)
        blockedAppsAdapter = BlockedAppsAdapter(emptyList())
        blocked_apps_rv.adapter = blockedAppsAdapter
        blocked_apps_title.text = getString(R.string.blocked_apps, blockedAppsAdapter.itemCount)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.saveSchedule -> {
                val schedule = viewModel.createInstantLockSchedule()
                val serviceIntent = Intent(this, AppBlockService::class.java)
                serviceIntent.putExtra(SCHEDULE, schedule)
                startService(serviceIntent)
                finish()
            }
        }
        return true
    }

    private fun startAlarm(calender: Calendar, type: Int) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, StartServiceReceiver::class.java)
        //intent.putExtra(SCHEDULE_TYPE, )
        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calender.timeInMillis, pendingIntent)
    }

    override fun onConfirm(applicationList: List<AppInfo>) {
        viewModel.applicationList.value = applicationList
    }
}
