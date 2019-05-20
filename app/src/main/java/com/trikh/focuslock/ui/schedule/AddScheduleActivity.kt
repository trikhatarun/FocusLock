package com.trikh.focuslock.ui.schedule

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.trikh.focuslock.R
import com.trikh.focuslock.utils.AutoFitGridLayoutManager
import com.trikh.focuslock.widget.timepicker.OnSliderRangeMovedListener
import com.trikh.focuslock.databinding.ActivityAddScheduleBinding
import com.trikh.focuslock.widget.arctoolbar.setAppBarLayout
import kotlinx.android.synthetic.main.activity_add_schedule.*
import kotlinx.android.synthetic.main.toolbar.*
import java.text.SimpleDateFormat
import java.util.*

class AddScheduleActivity : AppCompatActivity() {

    private lateinit var blockedAppsAdapter: BlockedAppsAdapter
    private lateinit var binding: ActivityAddScheduleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_schedule)
        val viewModel = ViewModelProviders.of(this).get(AddScheduleViewModel::class.java)
        binding.viewModel = viewModel

        arcToolbar.setAppBarLayout(appbar)
        setSupportActionBar(toolbar)
        toolbar_title.text = getString(R.string.set_schedule)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        sleep_time_tv.text = timeFormat.format(time_picker.start.time)
        awake_time_tv.text = timeFormat.format(time_picker.end.time)

        time_picker.setOnChangeListener(object :
            OnSliderRangeMovedListener {
            override fun onChange(start: Calendar, end: Calendar) {
                sleep_time_tv.text = timeFormat.format(start.time)
                awake_time_tv.text = timeFormat.format(end.time)
            }
        })

        time_picker.setOnTouchListener { _, _ ->
            nestedScrollView.requestDisallowInterceptTouchEvent(true)
            false
        }

        blocked_apps_rv.layoutManager = AutoFitGridLayoutManager(this, 48)
        blockedAppsAdapter = BlockedAppsAdapter(emptyList())
        blocked_apps_rv.adapter = blockedAppsAdapter
        blocked_apps_title.text = getString(R.string.blocked_apps, blockedAppsAdapter.itemCount)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_schedule_menu, menu)
        return true
    }
}
