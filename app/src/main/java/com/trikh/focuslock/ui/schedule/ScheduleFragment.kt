package com.trikh.focuslock.ui.schedule

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.trikh.focuslock.R
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.ui.schedule.customschedule.CustomScheduleActivity
import com.trikh.focuslock.utils.Constants
import com.trikh.focuslock.widget.customdialog.CustomDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.android.synthetic.main.toolbar.*
import java.lang.RuntimeException
import java.util.*
import kotlin.collections.ArrayList

class ScheduleFragment : Fragment(),
    ScheduleAdapter.PopupCallBacks {


    private lateinit var pref : SharedPreferences


    private lateinit var viewModelCustom: ScheduleViewModel


    private var listener: OnFragmentInteractionListener? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModelCustom = ViewModelProviders.of(this).get(ScheduleViewModel::class.java)
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        pref = context!!.getSharedPreferences(Constants.MY_PREF, 0)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("${context?.packageName} must implement interaction listener") as Throwable
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.fabMenu?.visibility = View.VISIBLE
        activity?.toolbar_title?.text = getString(R.string.schedule)
        /*activity?.mainTv1?.visibility = View.GONE
        activity?.mainTv2?.visibility = View.GONE*/

        schedulesRv.isNestedScrollingEnabled = false
        schedulesRv.adapter =
            ScheduleAdapter(getMockSchedules(), this)
        viewModelCustom.scheduleList.observe(this, androidx.lifecycle.Observer { it ->

            (schedulesRv.adapter as ScheduleAdapter).setList(it)

        })


    }

    override fun onDetach() {
        super.onDetach()
        listener = null
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
        var schedule = viewModelCustom.scheduleList.value!![adpaterPos]
        //intent.putExtra("scheduleId", scheduleId)
        when(type){
            Constants.POPUP_EDIT -> {
                val editor = pref!!.edit()
                editor.putString(Constants.TYPE, Constants.POPUP_EDIT)
                editor.apply()
                Log.e("Fragment: ","Active: ${schedule.active}")
                findNavController().navigate(ScheduleFragmentDirections.actionEditSchedule(schedule))
                /*Log.e("Fragment: ","Active: ${schedule.active}")
                val intent  = Intent(context, CustomScheduleActivity::class.java)
                intent.putExtra("type",type)
                val bundle = Bundle()
                bundle.putParcelable("schedule", schedule)
                //intent.putParcelableArrayListExtra("appInfoList",  schedule.appInfoList)
                intent.putExtra("bundle", bundle)
                context?.startActivity(intent)*/
            }
            Constants.POPUP_ENABLE -> {
                schedule.active = true
                Log.e("Fragment: "," $schedule")
                viewModelCustom.enableOrDisableSchedule(schedule)
            }
            Constants.POPUP_DISABLE -> {
                schedule.active = false
                Log.e("Fragment: "," $schedule")
                viewModelCustom.enableOrDisableSchedule(schedule)
            }
            Constants.POPUP_DELETE -> {
                CustomDialog(R.string.remove_schedule,
                    {viewModelCustom.removeSchedule(schedule.id)},
                    R.string.delete_text,
                    R.string.cancel_text).
                    show(fragmentManager,"")

            }
        }
    }

    interface OnFragmentInteractionListener {

    }
}
