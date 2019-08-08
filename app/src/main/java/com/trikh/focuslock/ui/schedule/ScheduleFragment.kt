package com.trikh.focuslock.ui.schedule

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.trikh.focuslock.Application
import com.trikh.focuslock.R
import com.trikh.focuslock.ui.appblock.StartServiceReceiver
import com.trikh.focuslock.utils.AutoFitGridLayoutManager
import com.trikh.focuslock.utils.Constants
import com.trikh.focuslock.utils.IconsUtils
import com.trikh.focuslock.utils.TimeDurationUtils
import com.trikh.focuslock.widget.customdialog.CustomDialog
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.android.synthetic.main.instant_lock_schedule.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

class ScheduleFragment : Fragment(),
    ScheduleAdapter.PopupCallBacks {

    private lateinit var pref: SharedPreferences
    private lateinit var viewModelSchedule: ScheduleViewModel
    private lateinit var endTime: Calendar
    private var check = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModelSchedule = ViewModelProviders.of(this).get(ScheduleViewModel::class.java)
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        pref = context!!.getSharedPreferences(Constants.MY_PREF, 0)
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.fabMenu?.visibility = View.VISIBLE
        activity?.instantLockFab?.visibility = View.VISIBLE
        activity?.toolbar_title?.text = getString(R.string.schedule)
        endTime = Calendar.getInstance()
        instantLock.visibility = View.GONE
        schedulesRv.isNestedScrollingEnabled = false
        schedulesRv.adapter =
            ScheduleAdapter(emptyList(), this)
        viewModelSchedule.scheduleList.observe(this, androidx.lifecycle.Observer {
            (schedulesRv.adapter as ScheduleAdapter).setList(it)
        })




        viewModelSchedule.instantLockSchedule.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                instantLock.visibility = View.VISIBLE
                activity?.instantLockFab?.visibility = View.GONE
                val startTime = Calendar.getInstance()
                val appInfoList = IconsUtils(context).getIconsFromPackageManager(it.blockedApps)
                blockedAppsRv.layoutManager = AutoFitGridLayoutManager(Application.instance, 48)
                blockedAppsRv.adapter = BlockedAppsAdapter(appInfoList)
                blockedListTv.text =
                    Application.instance.getString(R.string.blocked_apps, appInfoList.size)
                endTime.timeInMillis = it.endTime
                check = true


                time.text = TimeDurationUtils.calculateDuration(startTime, endTime)


                //uncomment the below code for showing popup menu to enable edit and delete on Instant Lock schedule
                //****************************************************//
                /*moreOptions.setOnClickListener {
                    val builder = MenuBuilder(context)
                    MenuInflater(context).inflate(R.menu.custom_schedule_popup_menu, builder)

                        builder.rootMenu.removeItem(R.id.enable)

                        builder.rootMenu.removeItem(R.id.disable)

                    CustomSchedulePopup(context!!, builder, moreOptions, object : ScheduleAdapter.PopupCallBacks{
                        override fun onItemClicked(type: String, adpaterPos: Int) {
                            when(type){
                                Constants.POPUP_EDIT -> {
                                    val editor = pref.edit()
                                    editor.putString(Constants.TYPE, Constants.POPUP_INSTANT_EDIT)
                                    editor.apply()
                                    findNavController().navigate(ScheduleFragmentDirections.actionScheduleFragmentToInstantLockActivity(instantSchedule))
                                }
                                Constants.POPUP_DELETE -> {
                                    CustomDialog(
                                        R.string.remove_schedule,
                                        { viewModelSchedule.removeInstantLockSchedule() },
                                        R.string.delete_text,
                                        R.string.cancel_text
                                    ).show(fragmentManager, "")
                                }
                            }
                        }
                    }, it.id).show()
                }*/
                //*****************************************************//
            } else {
                instantLock.visibility = View.GONE
            }

        })
        if (check) {
            time.text = TimeDurationUtils.calculateDuration(Calendar.getInstance(), endTime)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModelSchedule.getInstantLockCount().subscribeBy {
            if (it <= 0) {
                instantLock.visibility = View.GONE
            }
        }
    }

    override fun onItemClicked(type: String, adpaterPos: Int) {
        val schedule = viewModelSchedule.scheduleList.value!![adpaterPos]
        when (type) {
            Constants.POPUP_EDIT -> {
                val editor = pref.edit()
                editor.putString(Constants.TYPE, Constants.POPUP_EDIT)
                editor.apply()
                if (adpaterPos == 0) {
                    findNavController().navigate(
                        ScheduleFragmentDirections.actionEditPrimarySchedule(
                            schedule
                        )
                    )
                } else {
                    findNavController().navigate(
                        ScheduleFragmentDirections.actionEditSchedule(
                            schedule
                        )
                    )
                }

            }
            Constants.POPUP_ENABLE -> {
                schedule.active = true
                Log.e("Fragment: ", " $schedule")
                viewModelSchedule.enableOrDisableSchedule(schedule).subscribe {
                    startService()
                }
            }
            Constants.POPUP_DISABLE -> {
                schedule.active = false
                Log.e("Fragment: ", " $schedule")
                viewModelSchedule.enableOrDisableSchedule(schedule).subscribe {
                    startService()
                }
            }
            Constants.POPUP_DELETE -> {
                CustomDialog(
                    R.string.remove_schedule,
                    { viewModelSchedule.removeSchedule(schedule.id) },
                    R.string.delete_text,
                    R.string.cancel_text
                ).show(fragmentManager, "")

            }
        }
    }

    private fun startService() {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, StartServiceReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, 5, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent)
    }
}
