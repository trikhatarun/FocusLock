package com.trikh.focuslock.ui.schedule.primaryschedule

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.navArgs
import com.trikh.focuslock.R
import com.trikh.focuslock.databinding.ActivityPrimaryScheduleBinding
import com.trikh.focuslock.utils.IconsUtils
import com.trikh.focuslock.widget.app_picker.AppInfo
import com.trikh.focuslock.widget.app_picker.AppPickerDialog
import com.trikh.focuslock.widget.arctoolbar.setAppBarLayout
import kotlinx.android.synthetic.main.activity_primary_schedule.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

class PrimaryScheduleActivity : AppCompatActivity(), AppPickerDialog.InteractionListener {

    private lateinit var binding: ActivityPrimaryScheduleBinding
    private lateinit var viewModel: PrimaryViewModel
    private val args by navArgs<PrimaryScheduleActivityArgs>()
    private val schedule by lazy { args.schedule }

    override

    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_primary_schedule)
        viewModel = ViewModelProviders.of(this).get(PrimaryViewModel::class.java)

        arcToolbar.setAppBarLayout(appbar)
        setSupportActionBar(toolbar)
        toolbar_title.text = getString(R.string.set_schedule)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        schedule.let {
            if (it != null) {
                it.appInfoList = IconsUtils(this).getIconsFromPackageManager(it.appList!!)
                val appInfoList = it.appInfoList

                setTime(it.startTime, it.endTime)
                viewModel.applicationList.postValue(appInfoList)
                viewModel.level.postValue(it.level)
            }
        }
    }

    private fun setTime(start: Calendar, end: Calendar) {
        viewModel.setTime(start, end)
        //timePicker.setTime(viewModel.getSleepTime(start.time, schedule.level), end)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_option_menu, menu)
        return true
    }

    override fun onConfirm(applicationList: List<AppInfo>) {
        viewModel.applicationList.value = applicationList
    }
}
