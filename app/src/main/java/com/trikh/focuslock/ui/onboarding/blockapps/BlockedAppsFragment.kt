package com.trikh.focuslock.ui.onboarding.blockapps

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.trikh.focuslock.R
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.databinding.FragmentBlockedAppsBinding
import com.trikh.focuslock.ui.MainActivity
import com.trikh.focuslock.ui.schedule.BlockedAppsAdapter
import com.trikh.focuslock.utils.AutoFitGridLayoutManager
import com.trikh.focuslock.utils.Constants
import com.trikh.focuslock.utils.TimeUtils
import com.trikh.focuslock.widget.app_picker.AppInfo
import com.trikh.focuslock.widget.app_picker.AppPickerDialog
import com.trikh.focuslock.widget.customdialog.CustomDialog
import kotlinx.android.synthetic.main.fragment_blocked_apps.view.*
import kotlinx.android.synthetic.main.fragment_blocked_apps.view.setSchedule
import kotlin.collections.ArrayList


class BlockedAppsFragment : Fragment(), AppPickerDialog.InteractionListener,
    LevelsAdapter.LevelCallBacks {


    private lateinit var blockedAppsAdapter: BlockedAppsAdapter
    private lateinit var levelsAdapter: LevelsAdapter
    private lateinit var viewModel: BlockAppsViewModel
    private val args: BlockedAppsFragmentArgs by navArgs()

    private lateinit var binding: FragmentBlockedAppsBinding
    private lateinit var root: View
    private var listFlag = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProviders.of(this).get(BlockAppsViewModel::class.java)
        onLevelChanged(1)

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_blocked_apps, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        root = binding.root

        viewModel.appPicker.observe(this, Observer {
            if (!it.hasBeenHandled) {
                AppPickerDialog(viewModel.applicationList.value!!, this)
                    .show(fragmentManager, "appPicker")
                it.getContentIfNotHandled()
            }
        })

        viewModel.applicationList.observe(this, Observer {
            blockedAppsAdapter.updateList(it)
            listFlag = it.isNotEmpty()
            //blocked_apps_title.text = getString(R.string.blocked_apps, it.size)
        })

        root = initLevelAdapter(root)

        root.blocked_apps_rv.layoutManager = AutoFitGridLayoutManager(container!!.context, 48)
        blockedAppsAdapter = BlockedAppsAdapter(emptyList())
        root.blocked_apps_rv.adapter = blockedAppsAdapter

        root.setSchedule.setOnClickListener {
            createPrimarySchedule()
        }
        //blocked_apps_title.text = getString(R.string.blocked_apps, blockedAppsAdapter.itemCount)
        return root
    }

    private fun initLevelAdapter(root: View): View {
        val list = ArrayList<String>()
        list.add(context!!.resources.getString(R.string.level_1))
        list.add(context!!.resources.getString(R.string.level_2))
        list.add(context!!.resources.getString(R.string.level_3))
        levelsAdapter = LevelsAdapter(list, this)
        root.levelsRv.adapter = levelsAdapter
        return root
    }

    override fun onConfirm(applicationList: List<AppInfo>) {
        viewModel.applicationList.value = applicationList
    }


    override fun onLevelChanged(level: Int) {
        Log.d("BlocksAppsFragment:", "levels: $level")
        val startTime = args.stringStartTime
        val endTime = args.stringEndTime
        val sleepTime = TimeUtils.getSleepTime(startTime.time, level)
        val awakeTIme = TimeUtils.getAwakeTime(endTime.time, level)
        val blockText = getString(R.string.on_boarding_level_message, sleepTime, awakeTIme)
        viewModel.level.postValue(level)
        viewModel.appBlockText.postValue(blockText)

    }

    private fun createPrimarySchedule() {
        if (listFlag) {
            val list: ArrayList<String> = ArrayList()
            viewModel.applicationList.value?.forEach {
                list.add(it.packageName)
            }
            Log.d("BlockedAppsFragment:", " list Size: ${list.size}")


            val schedule = Schedule(
                level = viewModel.level.value,
                endTime = args.stringEndTime,
                startTime = args.stringStartTime,
                active = true,
                selectedWeekDays = arrayOf(true, true, true, true, true, true, true),
                appList = list
            )
            Log.d(
                "BlockedAppsFragment:",
                "Level: ${schedule.level} endTime: ${schedule.endTime} startTime: ${schedule.startTime} active: ${schedule.active} appList: ${schedule.appList!!.size}"
            )
            viewModel.scheduleRepository.addSchedule(schedule)
            val pref = context!!.getSharedPreferences(Constants.MY_PREF, 0)
            val editor = pref!!.edit()
            editor.putBoolean(Constants.ON_BOARDING, false)
            editor.apply()
            //startActivity(Intent(context, MainActivity::class.java))
            findNavController().navigate(BlockedAppsFragmentDirections.actionHomeToNavGraph())
        } else {
            CustomDialog(
                R.string.minimum_blocked_apps_msg,
                {},
                R.string.ok_text,
                R.string.empty_string
            ).show(fragmentManager, "")
        }

    }
}