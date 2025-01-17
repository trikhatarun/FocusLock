package com.trikh.focuslock.ui.schedule

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.trikh.focuslock.R
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.ui.appblock.StartServiceReceiver
import com.trikh.focuslock.utils.Constants
import com.trikh.focuslock.widget.customdialog.CustomDialog
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

class ScheduleFragment : Fragment(), SchedulesAdapter.ScheduleInteractionListener {

    private lateinit var pref: SharedPreferences
    private lateinit var viewModelSchedule: ScheduleViewModel
    private lateinit var endTime: Calendar
    private var compositeDisposable = CompositeDisposable()
    private val scheduleAdapter = SchedulesAdapter(this)

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.toolbar_title?.text = getString(R.string.schedule)

        endTime = Calendar.getInstance()

        schedulesRv.isNestedScrollingEnabled = false
        schedulesRv.layoutManager = LinearLayoutManager(context)
        schedulesRv.adapter = scheduleAdapter

        instantLockFab.setOnClickListener {
            findNavController().navigate(ScheduleFragmentDirections.actionInstantLock())
            fabMenu.toggle(true)
        }

        scheduleFab.setOnClickListener {
            findNavController().navigate(ScheduleFragmentDirections.actionEditSchedule(null))
            fabMenu.toggle(true)
        }

        viewModelSchedule.scheduleList.observe(this, androidx.lifecycle.Observer {
            scheduleAdapter.setList(it)
        })

        viewModelSchedule.instantLockSchedule.observe(this, androidx.lifecycle.Observer {
            scheduleAdapter.setInstantLock(it)
        })
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.dispose()
    }

    override fun onScheduleAction(actionId: Long, schedule: Schedule) {
        when (actionId) {
            Constants.MENU_EDIT -> {
                if (schedule.level != -1) {
                    findNavController().navigate(
                        ScheduleFragmentDirections.actionEditPrimarySchedule(schedule)
                    )
                } else {
                    findNavController().navigate(
                        ScheduleFragmentDirections.actionEditSchedule(schedule)
                    )
                }

            }
            Constants.MENU_ENABLE -> {
                schedule.active = true
                viewModelSchedule.enableOrDisableSchedule(schedule).subscribe {
                    startService()
                }
            }
            Constants.MENU_DISABLE -> {
                schedule.active = false
                viewModelSchedule.enableOrDisableSchedule(schedule).subscribe {
                    startService()
                }
            }
            Constants.MENU_DELETE -> {
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
