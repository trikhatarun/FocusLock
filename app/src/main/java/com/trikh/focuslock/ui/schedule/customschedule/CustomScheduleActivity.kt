package com.trikh.focuslock.ui.schedule.customschedule

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.trikh.focuslock.R
import com.trikh.focuslock.databinding.ActivityCustomScheduleBinding
import com.trikh.focuslock.utils.AutoFitGridLayoutManager
import androidx.lifecycle.Observer
import androidx.navigation.navArgs
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.ui.MainActivity
import com.trikh.focuslock.ui.appblock.StartServiceReceiver
import com.trikh.focuslock.ui.schedule.BlockedAppsAdapter
import com.trikh.focuslock.utils.Constants
import com.trikh.focuslock.utils.IconsUtils
import com.trikh.focuslock.widget.app_picker.AppInfo
import com.trikh.focuslock.widget.app_picker.AppPickerDialog
import com.trikh.focuslock.widget.arctoolbar.setAppBarLayout
import com.trikh.focuslock.widget.customdialog.CustomDialog
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_custom_schedule.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*


class CustomScheduleActivity : AppCompatActivity(), AppPickerDialog.InteractionListener {


    private lateinit var blockedAppsAdapter: BlockedAppsAdapter
    private lateinit var binding: ActivityCustomScheduleBinding
    private lateinit var viewModelCustom: CustomScheduleViewModel
    private var weekFlag = false
    private var appListFlag = false
    private var type = Constants.DEFAULT_TYPE
    private lateinit var schedule: Schedule

    private val args: CustomScheduleActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_custom_schedule)
        viewModelCustom = ViewModelProviders.of(this).get(CustomScheduleViewModel::class.java)
        binding.viewModel = viewModelCustom
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
            viewModelCustom.applicationList.postValue(appInfoList)
            viewModelCustom.checkedIds.postValue(schedule.selectedWeekDays)
            setWeekDays(schedule.selectedWeekDays!!)


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


        viewModelCustom.checkedIds.observe(this, Observer {
            var flag = false
            Log.e(
                "CustomScheduleActivity:",
                "selectedWeeks: ${it[0]} ${it[1]} ${it[2]} ${it[3]} ${it[4]} ${it[5]} ${it[6]} "
            )
            for (i in 0 until it.size) {
                if (it[i]) {
                    flag = true
                }
            }
            weekFlag = flag
        })

        viewModelCustom.applicationList.observe(this, Observer {
            if (it.isNotEmpty()) {
                appListFlag = true
            }
            blockedAppsAdapter.updateList(it)
            blocked_apps_title.text = getString(R.string.blocked_apps, it.size)
        })

        viewModelCustom.appPicker.observe(this, Observer {
            if (!it.hasBeenHandled) {
                AppPickerDialog(viewModelCustom.applicationList.value!!, this)
                    .show(supportFragmentManager, "appPicker")
                it.getContentIfNotHandled()
            }
        })



        blocked_apps_rv.layoutManager = AutoFitGridLayoutManager(this, 48)
        blockedAppsAdapter = BlockedAppsAdapter(emptyList())
        blocked_apps_rv.adapter = blockedAppsAdapter
        blocked_apps_title.text = getString(R.string.blocked_apps, blockedAppsAdapter.itemCount)
    }


    private fun setWeekDays(ids: Array<Boolean>) {
        sundayCb.isChecked = ids[0]
        mondayCb.isChecked = ids[1]
        tuesdayCb.isChecked = ids[2]
        wednesdayCb.isChecked = ids[3]
        thursdayCb.isChecked = ids[4]
        fridayCb.isChecked = ids[5]
        saturdayCb.isChecked = ids[6]
    }


    private fun setTime(start: Calendar, end: Calendar) {
        timePicker.setTime(start, end)
        viewModelCustom.setTime(start, end)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {

            R.id.saveSchedule -> {
                if (weekFlag and appListFlag) {

                    if (TextUtils.equals(type, Constants.POPUP_EDIT)) {

                        viewModelCustom.updateSchedule(
                            schedule.id,
                            schedule.level!!,
                            schedule.active!!
                        )
                        viewModelCustom.startTime.value?.let { it2 ->
                            viewModelCustom.checkedIds.value?.let { it1 ->
                                setCustomScheduleAlarm(
                                    it2,
                                    Constants.CUSTOM_SCHEDULE,
                                    it1,
                                    schedule.id
                                )
                            }
                        }


                    } else {

                        viewModelCustom.createSchedule()?.subscribeBy {
                            if (it > 0 ){
                                viewModelCustom.scheduleRepository.getLastSchedule().subscribeBy {
                                    viewModelCustom.startTime.value?.let { it2 ->
                                        viewModelCustom.checkedIds.value?.let { it1 ->
                                            setCustomScheduleAlarm(
                                                it2,
                                                Constants.CUSTOM_SCHEDULE,
                                                it1,
                                                it
                                            )
                                        }
                                    }
                                }
                            }
                        }



                    }
                    val serviceIntent = Intent(this, MainActivity::class.java)
                    startActivity(serviceIntent)
                } else {
                    if (!weekFlag) {
                        CustomDialog(
                            R.string.minimum_weekday_msg,
                            {},
                            R.string.ok_text,
                            R.string.empty_string
                        ).show(supportFragmentManager, "")
                    } else if (!appListFlag) {
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


    fun setCustomScheduleAlarm(
        calender: Calendar,
        type: Int,
        selectedWeekDayTime: Array<Boolean>,
        requestCode: Int
    ) {

        val intent = Intent(this, StartServiceReceiver::class.java)
        intent.putExtra(Constants.SCHEDULE_TYPE, type)
        val pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        for (index in 0 until selectedWeekDayTime.size) {

            if (selectedWeekDayTime[index]) {
                calender.set(Calendar.DAY_OF_WEEK, index + 1)
                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calender.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
            }

        }


    }


    override fun onConfirm(applicationList: List<AppInfo>) {
        viewModelCustom.applicationList.value = applicationList
    }
}
