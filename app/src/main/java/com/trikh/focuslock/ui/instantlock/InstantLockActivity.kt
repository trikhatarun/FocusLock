package com.trikh.focuslock.ui.instantlock

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.trikh.focuslock.R
import com.trikh.focuslock.databinding.ActivityInstantLockBinding
import com.trikh.focuslock.ui.appblock.AppBlockService
import com.trikh.focuslock.ui.schedule.BlockedAppsAdapter
import com.trikh.focuslock.utils.AutoFitGridLayoutManager
import com.trikh.focuslock.utils.BaseActivity
import com.trikh.focuslock.utils.Constants.Companion.INSTANT_LOCK
import com.trikh.focuslock.utils.Constants.Companion.SCHEDULE_TYPE
import com.trikh.focuslock.utils.extensions.hasUsageStatsPermission
import com.trikh.focuslock.widget.app_picker.AppInfo
import com.trikh.focuslock.widget.app_picker.AppPickerDialog
import com.trikh.focuslock.widget.arctoolbar.setAppBarLayout
import com.trikh.focuslock.widget.customdialog.CustomDialog
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_primary_schedule.*
import kotlinx.android.synthetic.main.toolbar.*

class InstantLockActivity : BaseActivity(), AppPickerDialog.InteractionListener {

    private lateinit var binding: ActivityInstantLockBinding
    private lateinit var viewModel: InstantLockViewModel
    private lateinit var blockedAppsAdapter: BlockedAppsAdapter
    private var hours: Int? = null
    private var minutes: Int? = null
    private var blockedApps: Int? = null

    private lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_instant_lock)

        viewModel = ViewModelProviders.of(this).get(InstantLockViewModel::class.java)
        binding.viewModel = viewModel

        binding.lifecycleOwner = this
        root = binding.root

        blocked_apps_rv.layoutManager = AutoFitGridLayoutManager(this, 48)
        blockedAppsAdapter = BlockedAppsAdapter(emptyList())
        blocked_apps_rv.adapter = blockedAppsAdapter
        blocked_apps_title.text = getString(R.string.blocked_apps, blockedAppsAdapter.itemCount)

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
            root.findViewWithTag<TextView>(temp.toString())
                .setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))

            for (i in 10..80 step 10) {
                if (i != temp) {
                    root.findViewWithTag<TextView>(i.toString())
                        .setTextColor(ContextCompat.getColor(this, R.color.colorSecondaryText))
                }
            }
        })

        viewModel.minutes.observe(this, Observer {
            minutes = it.times(10)
            root.findViewWithTag<TextView>(it.toString())
                .setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            for (i in 0..5) {
                if (i != it) {
                    root.findViewWithTag<TextView>(i.toString())
                        .setTextColor(ContextCompat.getColor(this, R.color.colorSecondaryText))
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
                if (hasUsageStatsPermission) {
                    if ((time!! >= 10) and (blockedApps!! > 0)) {

                        viewModel.createInstantLockSchedule()?.subscribeBy {
                            val serviceIntent = Intent(this, AppBlockService::class.java).apply {
                                putExtra(SCHEDULE_TYPE, INSTANT_LOCK)
                            }
                            startService(serviceIntent)
                            finish()
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
                    requestUsagePermission()
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
