package com.trikh.focuslock.ui.schedule.primaryschedule

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import com.trikh.focuslock.R
import com.trikh.focuslock.databinding.ActivityAddScheduleBinding
import com.trikh.focuslock.ui.schedule.BlockedAppsAdapter
import com.trikh.focuslock.ui.schedule.customschedule.CustomScheduleViewModel
import com.trikh.focuslock.utils.AutoFitGridLayoutManager
import com.trikh.focuslock.widget.app_picker.AppInfo
import com.trikh.focuslock.widget.app_picker.AppPickerDialog
import com.trikh.focuslock.widget.arctoolbar.setAppBarLayout
import kotlinx.android.synthetic.main.activity_add_schedule.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

class AddScheduleActivity : AppCompatActivity(), AppPickerDialog.InteractionListener{


    private lateinit var blockedAppsAdapter: BlockedAppsAdapter
    private lateinit var binding: ActivityAddScheduleBinding
    private lateinit var viewModel: CustomScheduleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_schedule)
        viewModel = ViewModelProviders.of(this).get(CustomScheduleViewModel::class.java)
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

        viewModel.applicationList.observe(this, Observer {
            blockedAppsAdapter.updateList(it)
            blocked_apps_title.text = getString(R.string.blocked_apps, it.size)
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

    override fun onConfirm(applicationList: List<AppInfo>) {
        viewModel.applicationList.value = applicationList
    }
}
