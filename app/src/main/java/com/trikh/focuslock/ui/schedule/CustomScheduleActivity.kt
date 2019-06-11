package com.trikh.focuslock.ui.schedule

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.trikh.focuslock.R
import com.trikh.focuslock.databinding.ActivityCustomScheduleBinding
import com.trikh.focuslock.utils.AutoFitGridLayoutManager
import androidx.lifecycle.Observer
import com.trikh.focuslock.ui.MainActivity
import com.trikh.focuslock.utils.Constants.Companion.INSTANT_LOCK
import com.trikh.focuslock.utils.Constants.Companion.SCHEDULE
import com.trikh.focuslock.utils.Constants.Companion.SCHEDULE_TYPE
import com.trikh.focuslock.ui.appblock.AppBlockService
import com.trikh.focuslock.utils.Constants
import com.trikh.focuslock.widget.app_picker.AppInfo
import com.trikh.focuslock.widget.app_picker.AppPickerDialog
import com.trikh.focuslock.widget.arctoolbar.setAppBarLayout
import kotlinx.android.synthetic.main.activity_add_schedule.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

class CustomScheduleActivity : AppCompatActivity(), AppPickerDialog.InteractionListener {


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


        viewModel.checkedIds.observe(this, Observer {
            Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
        })

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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {

            R.id.saveSchedule -> {
                viewModel.createSchedule()
                Toast.makeText(this, viewModel.checkedIds.value.toString(), Toast.LENGTH_LONG)
                    .show()
                val serviceIntent = Intent(this, MainActivity::class.java)
                startActivity(serviceIntent)
            }

        }
        return true
    }


    override fun onConfirm(applicationList: List<AppInfo>) {
        viewModel.applicationList.value = applicationList
    }
}
