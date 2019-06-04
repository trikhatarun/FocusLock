package com.trikh.focuslock.ui.schedule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.trikh.focuslock.R
import com.trikh.focuslock.databinding.ActivityCustomScheduleBinding
import com.trikh.focuslock.utils.AutoFitGridLayoutManager
import com.trikh.focuslock.widget.arctoolbar.setAppBarLayout
import kotlinx.android.synthetic.main.activity_add_schedule.blocked_apps_rv
import kotlinx.android.synthetic.main.activity_add_schedule.blocked_apps_title
import kotlinx.android.synthetic.main.activity_add_schedule.nestedScrollView
import kotlinx.android.synthetic.main.activity_add_schedule.timePicker
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

class CustomScheduleActivity : AppCompatActivity() {

    private lateinit var blockedAppsAdapter: BlockedAppsAdapter
    private lateinit var binding: ActivityCustomScheduleBinding
    private lateinit var viewModel: AddScheduleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_custom_schedule)
        viewModel = ViewModelProviders.of(this).get(AddScheduleViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        arcToolbar.setAppBarLayout(appbar)
        setSupportActionBar(toolbar)
        toolbar_title.text = getString(R.string.set_schedule)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Replace this code and get start and end from intents instead
        /***********************************/
        val start = Calendar.getInstance()
        start.set(Calendar.HOUR_OF_DAY, 2)
        start.set(Calendar.MINUTE, 0)
        val end = Calendar.getInstance()
        end.set(Calendar.HOUR_OF_DAY, 10)
        end.set(Calendar.MINUTE, 0)
        setTime(start, end)
        /**********************************/


        timePicker.setOnTouchListener { _, _ ->
            nestedScrollView.requestDisallowInterceptTouchEvent(true)
            false
        }

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


}