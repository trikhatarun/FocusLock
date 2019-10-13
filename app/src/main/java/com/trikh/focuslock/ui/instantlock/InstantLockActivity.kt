package com.trikh.focuslock.ui.instantlock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.navArgs
import com.trikh.focuslock.R
import com.trikh.focuslock.data.model.InstantLockSchedule
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.databinding.ActivityInstantLockBinding
import com.trikh.focuslock.ui.appblock.AppBlockService
import com.trikh.focuslock.ui.appblock.StartServiceReceiver
import com.trikh.focuslock.ui.schedule.BlockedAppsAdapter
import com.trikh.focuslock.ui.schedule.customschedule.CustomScheduleActivityArgs
import com.trikh.focuslock.utils.AutoFitGridLayoutManager
import com.trikh.focuslock.utils.Constants
import com.trikh.focuslock.utils.Constants.Companion.INSTANT_LOCK
import com.trikh.focuslock.utils.Constants.Companion.SCHEDULE
import com.trikh.focuslock.utils.Constants.Companion.SCHEDULE_TYPE
import com.trikh.focuslock.utils.IconsUtils
import com.trikh.focuslock.utils.extensions.hasUsageStatsPermission
import com.trikh.focuslock.widget.app_picker.AppInfo
import com.trikh.focuslock.widget.app_picker.AppPickerDialog
import com.trikh.focuslock.widget.arctoolbar.setAppBarLayout
import com.trikh.focuslock.widget.customdialog.CustomDialog
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_primary_schedule.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

class InstantLockActivity : AppCompatActivity(), AppPickerDialog.InteractionListener {

    private lateinit var binding: ActivityInstantLockBinding
    private lateinit var viewModel: InstantLockViewModel
    private lateinit var blockedAppsAdapter: BlockedAppsAdapter
    private var hours: Int? = null
    private var minutes: Int? = null
    private var blockedApps: Int? = null

    private var type = Constants.DEFAULT_TYPE
    private lateinit var root: View

    private lateinit var instantLockSchedule: InstantLockSchedule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_instant_lock)
        viewModel = ViewModelProviders.of(this).get(InstantLockViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        root = binding.root

        type = getSharedPreferences(Constants.MY_PREF, 0).getString(
            Constants.TYPE,
            Constants.DEFAULT_TYPE
        )
        if (TextUtils.equals(type, Constants.POPUP_INSTANT_EDIT)) {
            //uncomment the below code to enable edit feature of Instant Lock Schedule
            //********************************************************//
            /*instantLockSchedule = args.instantLockSchedule
            val cal = Calendar.getInstance()
            cal.timeInMillis = instantLockSchedule.endTime
            hours = cal.get(Calendar.HOUR)
            minutes = cal.get(Calendar.MINUTE)
            viewModel.hours.postValue(hours)
            viewModel.minutes.postValue(minutes!!.div(10))
            viewModel.applicationList.postValue(
                IconsUtils(this).getIconsFromPackageManager(
                    instantLockSchedule.blockedApps
                )
            )*/
            //*******************************************************//

        } else {

            blocked_apps_rv.layoutManager = AutoFitGridLayoutManager(this, 48)
            blockedAppsAdapter = BlockedAppsAdapter(emptyList())
            blocked_apps_rv.adapter = blockedAppsAdapter
            blocked_apps_title.text = getString(R.string.blocked_apps, blockedAppsAdapter.itemCount)
        }

        arcToolbar.setAppBarLayout(appbar)
        setSupportActionBar(toolbar)

        toolbar_title.text = getString(R.string.instant_lock)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.appPicker.observe(this, Observer {
            if (!it.hasBeenHandled) {
                AppPickerDialog(viewModel.applicationList.value!!, this)
                    .show(supportFragmentManager, "appPicker")
            }
        })


        viewModel.hours.observe(this, Observer {
            hours = it
            val temp = (it + 1) * 10
            Log.d("Temp: ", "$temp")
            root.findViewWithTag<TextView>(temp.toString())
                .setTextColor(resources.getColor(R.color.colorPrimary))

            for (i in 10..80 step 10) {
                if (i != temp) {
                    root.findViewWithTag<TextView>(i.toString()).setTextColor(resources.getColor(R.color.colorSecondaryText))
                }
            }
        })

        viewModel.minutes.observe(this, Observer {
            minutes = it.times(10)
            root.findViewWithTag<TextView>(it.toString())
                .setTextColor(resources.getColor(R.color.colorPrimary))
            for (i in 0..5) {
                if (i != it) {
                    Log.d("Temp: ", "$i")
                    root.findViewWithTag<TextView>(i.toString())
                        .setTextColor(resources.getColor(R.color.colorSecondaryText))
                }
            }
        })


        viewModel.applicationList.observe(this, Observer {
            blockedApps = it.size
            blockedAppsAdapter.updateList(it)
            blocked_apps_title.text = getString(R.string.blocked_apps, it.size)
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.saveSchedule -> {
                val time = hours?.times(60)?.plus(minutes!!)
                Log.d("InstantLockActivity: ", "Time: $time")
                if (hasUsageStatsPermission) {

                    if ((time!!.compareTo(10) >= 0) and (blockedApps!!.compareTo(0) > 0)) {

                        if (TextUtils.equals(type, Constants.POPUP_EDIT)) {

                            //add code to update Instant Lock Schedule
                            //****************************************//
                            //val schedule = viewModel.updateInstantLockSchedule()
                            //****************************************//


                        } else {


                            viewModel.createInstantLockSchedule()?.subscribeBy {
                                val serviceIntent = Intent(this, AppBlockService::class.java)
                                serviceIntent.putExtra(SCHEDULE_TYPE, INSTANT_LOCK)
                                //serviceIntent.putExtra(SCHEDULE, schedule)
                                startService(serviceIntent)
                                finish()
                            }


                        }

                    } else {
                        if (time < 10) {
                            CustomDialog(
                                R.string.minimum_time_msg,
                                {},
                                R.string.ok_text,
                                R.string.empty_string
                            ).show(supportFragmentManager, "")
                        } else if (blockedApps!! <= 0) {
                            CustomDialog(
                                R.string.minimum_blocked_apps_msg,
                                {},
                                R.string.ok_text,
                                R.string.empty_string
                            ).show(supportFragmentManager, "")
                        }
                    }

                } else {

                }
            }
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }

    private fun startAlarm(calender: Calendar, type: Int) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, StartServiceReceiver::class.java)
        intent.putExtra(SCHEDULE_TYPE, type)
        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calender.timeInMillis, pendingIntent)
    }

    override fun onConfirm(applicationList: List<AppInfo>) {
        viewModel.applicationList.value = applicationList
    }
}
