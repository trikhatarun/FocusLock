package com.trikh.focuslock.ui.schedule.primaryschedule

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import androidx.navigation.navArgs
import com.trikh.focuslock.R
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.databinding.ActivityPrimaryScheduleBinding
import com.trikh.focuslock.ui.MainActivity
import com.trikh.focuslock.ui.appblock.StartServiceReceiver
import com.trikh.focuslock.ui.schedule.BlockedAppsAdapter
import com.trikh.focuslock.ui.schedule.customschedule.CustomScheduleActivityArgs
import com.trikh.focuslock.ui.schedule.customschedule.CustomScheduleViewModel
import com.trikh.focuslock.utils.AutoFitGridLayoutManager
import com.trikh.focuslock.utils.Constants
import com.trikh.focuslock.utils.IconsUtils
import com.trikh.focuslock.widget.app_picker.AppInfo
import com.trikh.focuslock.widget.app_picker.AppPickerDialog
import com.trikh.focuslock.widget.arctoolbar.setAppBarLayout
import com.trikh.focuslock.widget.customdialog.CustomDialog
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_primary_schedule.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

class PrimaryScheduleActivity : AppCompatActivity(), AppPickerDialog.InteractionListener {


    private lateinit var blockedAppsAdapter: BlockedAppsAdapter
    private lateinit var binding: ActivityPrimaryScheduleBinding
    private lateinit var viewModel: PrimaryViewModel
    private var appListFlag = false
    private var type = Constants.DEFAULT_TYPE
    private lateinit var schedule: Schedule

    private val args: PrimaryScheduleActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_primary_schedule)
        viewModel = ViewModelProviders.of(this).get(PrimaryViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        type = getSharedPreferences(Constants.MY_PREF, 0).getString(
            Constants.TYPE,
            Constants.DEFAULT_TYPE
        )
        if (TextUtils.equals(type, Constants.POPUP_EDIT)) {
            schedule = args.schedule
            val active = schedule.active

            schedule.appInfoList = IconsUtils(this).getIconsFromPackageManager(schedule.appList!!)
            val appInfoList = schedule.appInfoList

            setTime(schedule.startTime, schedule.endTime)
            viewModel.applicationList.postValue(appInfoList)
            /*viewModel.level.postValue(schedule.level)
            when(schedule.level){
                1 -> level_one_rb.isChecked = true
                2 -> level_two_rb.isChecked = true
                3 -> level_three_rb.isChecked = true
            }*/
            binding.root.findViewWithTag<RadioButton>(schedule.level.toString()).isChecked = true
            Log.d("PrimarySchedule:", " $schedule")


        } else {
            val start = Calendar.getInstance()
            start.set(Calendar.HOUR_OF_DAY, 2)
            start.set(Calendar.MINUTE, 0)
            val end = Calendar.getInstance()
            end.set(Calendar.HOUR_OF_DAY, 10)
            end.set(Calendar.MINUTE, 0)
            setTime(start, end)
        }



        arcToolbar.setAppBarLayout(appbar)
        setSupportActionBar(toolbar)
        toolbar_title.text = getString(R.string.set_schedule)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Replace this code and get start and end from intents instead
        /***********************************/

        /**********************************/


        timePicker.setOnTouchListener { _, _ ->
            nestedScrollView.requestDisallowInterceptTouchEvent(true)
            false
        }

        viewModel.applicationList.observe(this, Observer {
            blockedAppsAdapter.updateList(it)
            blocked_apps_title.text = getString(R.string.blocked_apps, it.size)
            appListFlag = it.isNotEmpty()
        })


        viewModel.appPicker.observe(this, Observer {
            if (!it.hasBeenHandled) {
                AppPickerDialog(viewModel.applicationList.value!!, this)
                    .show(supportFragmentManager, "appPicker")
                it.getContentIfNotHandled()
            }
        })

        blocked_apps_rv.layoutManager = AutoFitGridLayoutManager(this, 48)
        blockedAppsAdapter = BlockedAppsAdapter(emptyList())
        blocked_apps_rv.adapter = blockedAppsAdapter
        blocked_apps_title.text = getString(R.string.blocked_apps, blockedAppsAdapter.itemCount)
    }

    private fun setTime(start: Calendar, end: Calendar) {
        timePicker.setTime(start, end)
        viewModel.setTime(start, end)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_option_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {

            R.id.saveSchedule -> {
                if (appListFlag) {

                    if (TextUtils.equals(type, Constants.POPUP_EDIT)) {

                        viewModel.updateSchedule(
                            schedule.id,
                            schedule.active!!
                        )?.subscribeBy {
                            setSchedule(schedule.startTime, Constants.DAILY_SCHEDULE)
                        }




                    } else {

                        viewModel.createSchedule()?.subscribeBy {

                            setSchedule(schedule.startTime, Constants.DAILY_SCHEDULE)
                        }



                    }
                    val serviceIntent = Intent(this, MainActivity::class.java)
                    startActivity(serviceIntent)
                } else {
                    if (!appListFlag) {
                        CustomDialog(
                            R.string.minimum_blocked_apps_msg,
                            {},
                            R.string.ok_text,
                            R.string.empty_string
                        ).show(supportFragmentManager, "")
                    }
                }
            }
            android.R.id.home -> {
                onBackPressed()
            }

        }
        return true
    }


    private fun setSchedule(calender: Calendar, type: Int) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, StartServiceReceiver::class.java)
        intent.putExtra(Constants.SCHEDULE_TYPE, type)
        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calender.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    override fun onConfirm(applicationList: List<AppInfo>) {
        viewModel.applicationList.value = applicationList
    }
}
