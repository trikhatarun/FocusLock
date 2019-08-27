package com.trikh.focuslock.ui.schedule.primaryschedule

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.navArgs
import com.trikh.focuslock.R
import com.trikh.focuslock.databinding.ActivityPrimaryScheduleBinding
import com.trikh.focuslock.ui.MainActivity
import com.trikh.focuslock.ui.schedule.BlockedAppsAdapter
import com.trikh.focuslock.utils.AlarmManager
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

    private lateinit var binding: ActivityPrimaryScheduleBinding
    private lateinit var viewModel: PrimaryViewModel
    private lateinit var blockedAppsAdapter: BlockedAppsAdapter
    private val args by navArgs<PrimaryScheduleActivityArgs>()
    private val schedule by lazy { args.schedule }

    override

    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_primary_schedule)
        binding.lifecycleOwner = this

        viewModel = ViewModelProviders.of(this).get(PrimaryViewModel::class.java)

        blockedAppsAdapter = BlockedAppsAdapter(emptyList())

        arcToolbar.setAppBarLayout(appbar)
        setSupportActionBar(toolbar)
        toolbar_title.text = getString(R.string.set_schedule)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        timePicker.setOnTouchListener { _, _ ->
            nestedScrollView.requestDisallowInterceptTouchEvent(true)
            false
        }

        schedule.let {
            if (it != null) {
                it.appInfoList = IconsUtils(this).getIconsFromPackageManager(it.appList!!)
                val appInfoList = it.appInfoList

                setTime(it.startTime, it.endTime)
                viewModel.applicationList.postValue(appInfoList)
                viewModel.level.postValue(it.level)
                binding.viewModel = viewModel
            }
        }

        blocked_apps_rv.layoutManager = AutoFitGridLayoutManager(this, 48)
        blockedAppsAdapter = BlockedAppsAdapter(emptyList())
        blocked_apps_rv.adapter = blockedAppsAdapter
        blocked_apps_title.text = getString(R.string.blocked_apps, blockedAppsAdapter.itemCount)

        setupObservables()
    }

    private fun setupObservables() {
        viewModel.applicationList.observe(this, Observer {
            blockedAppsAdapter.updateList(it)
            blocked_apps_title.text = getString(R.string.blocked_apps, it.size)
        })
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
                    viewModel.updateSchedule(
                        schedule.id,
                        schedule?.active!!
                    )?.subscribeBy {
                        AlarmManager(this).setScheduleAlarm(schedule?.startTime!!)
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

    override fun onConfirm(applicationList: List<AppInfo>) {
        viewModel.applicationList.value = applicationList
    }
}
