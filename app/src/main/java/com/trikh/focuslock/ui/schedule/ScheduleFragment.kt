package com.trikh.focuslock.ui.schedule

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.appcompat.view.menu.MenuBuilder
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.trikh.focuslock.Application
import com.trikh.focuslock.R
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.utils.AutoFitGridLayoutManager
import com.trikh.focuslock.utils.Constants
import com.trikh.focuslock.utils.IconsUtils
import com.trikh.focuslock.utils.TimeDurationUtils
import com.trikh.focuslock.widget.app_picker.AppInfo
import com.trikh.focuslock.widget.customdialog.CustomDialog
import com.trikh.focuslock.widget.customschedulepopup.CustomSchedulePopup
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.android.synthetic.main.instant_lock_schedule.*
import kotlinx.android.synthetic.main.schedule_layout.view.*
import kotlinx.android.synthetic.main.toolbar.*
import java.lang.RuntimeException
import java.util.*
import kotlin.collections.ArrayList

class ScheduleFragment : Fragment(),
    ScheduleAdapter.PopupCallBacks {


    private lateinit var pref: SharedPreferences


    private lateinit var viewModelschedule: ScheduleViewModel

    private lateinit var endTime: Calendar

    private var check = false


    //private var listener: OnFragmentInteractionListener? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModelschedule = ViewModelProviders.of(this).get(ScheduleViewModel::class.java)
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        pref = context!!.getSharedPreferences(Constants.MY_PREF, 0)
        /*if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("${context.packageName} must implement interaction listener") as Throwable
        }*/
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.fabMenu?.visibility = View.VISIBLE
        activity?.instantLockFab?.visibility = View.VISIBLE
        activity?.toolbar_title?.text = getString(R.string.schedule)
        /*activity?.mainTv1?.visibility = View.GONE
        activity?.mainTv2?.visibility = View.GONE*/
        endTime = Calendar.getInstance()
        instantLock.visibility = View.GONE
        schedulesRv.isNestedScrollingEnabled = false
        schedulesRv.adapter =
            ScheduleAdapter(emptyList(), this)
        viewModelschedule.scheduleList.observe(this, androidx.lifecycle.Observer {

            (schedulesRv.adapter as ScheduleAdapter).setList(it)

        })




        viewModelschedule.instantLockSchedule.observe(this, androidx.lifecycle.Observer {
            val instantSchedule = it
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
                                        { viewModelschedule.removeInstantLockSchedule() },
                                        R.string.delete_text,
                                        R.string.cancel_text
                                    ).show(fragmentManager, "")
                                }
                            }
                        }
                    }, it.id).show()
                }*/
                //*****************************************************//
            }
            else {
                instantLock.visibility = View.GONE
            }

        })
        if (check) {
            time.text = TimeDurationUtils.calculateDuration(Calendar.getInstance(), endTime)
        }


    }

    override fun onDetach() {
        super.onDetach()
        //listener = null
    }

    override fun onResume() {
        super.onResume()
        viewModelschedule.getInstantLockCount().subscribe {
            if (it<=0){
                instantLock.visibility = View.GONE
            }
        }
    }

    @VisibleForTesting
    fun getMockSchedules(): ArrayList<Schedule> {
        val list = ArrayList<Schedule>()
        val appList: List<String> = emptyList()
        val startTime = Calendar.getInstance()
        val endTime = Calendar.getInstance()
        endTime.add(Calendar.HOUR, 8)
        val schedule =
            Schedule(101, startTime, endTime, level = 3, active = false, appList = appList)
        list.add(schedule)
        return list
    }

    override fun onItemClicked(type: String, adpaterPos: Int) {
        val schedule = viewModelschedule.scheduleList.value!![adpaterPos]
        //intent.putExtra("scheduleId", scheduleId)
        when (type) {
            Constants.POPUP_EDIT -> {
                val editor = pref.edit()
                editor.putString(Constants.TYPE, Constants.POPUP_EDIT)
                editor.apply()
                Log.e("Fragment: ", "Active: ${schedule.active}")
                findNavController().navigate(ScheduleFragmentDirections.actionEditSchedule(schedule))

            }
            Constants.POPUP_ENABLE -> {
                schedule.active = true
                Log.e("Fragment: ", " $schedule")
                viewModelschedule.enableOrDisableSchedule(schedule)
            }
            Constants.POPUP_DISABLE -> {
                schedule.active = false
                Log.e("Fragment: ", " $schedule")
                viewModelschedule.enableOrDisableSchedule(schedule)
            }
            Constants.POPUP_DELETE -> {
                CustomDialog(
                    R.string.remove_schedule,
                    { viewModelschedule.removeSchedule(schedule.id) },
                    R.string.delete_text,
                    R.string.cancel_text
                ).show(fragmentManager, "")

            }
        }
    }

    interface OnFragmentInteractionListener {

    }
}
