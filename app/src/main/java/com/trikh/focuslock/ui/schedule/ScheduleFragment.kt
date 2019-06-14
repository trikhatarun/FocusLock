package com.trikh.focuslock.ui.schedule

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.trikh.focuslock.R
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.widget.customdialog.CustomDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.android.synthetic.main.toolbar.*
import java.lang.RuntimeException
import java.util.*
import kotlin.collections.ArrayList

class ScheduleFragment : Fragment(), ScheduleAdapter.PopupCallBacks {


    private lateinit var viewModel: ScheduleViewModel

    private var listener: OnFragmentInteractionListener? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(ScheduleViewModel::class.java)
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("${context?.packageName} must implement interaction listener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.fabMenu?.visibility = View.VISIBLE
        activity?.toolbar_title?.text = getString(R.string.schedule)
        /*activity?.mainTv1?.visibility = View.GONE
        activity?.mainTv2?.visibility = View.GONE*/

        schedulesRv.isNestedScrollingEnabled = false
        schedulesRv.adapter = ScheduleAdapter(getMockSchedules(), this)
        viewModel.scheduleList.observe(this, androidx.lifecycle.Observer { it ->

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

    override fun onPopupItemClicked(type: Int, adpaterPos: Int) {
        var schedule = viewModel.scheduleList.value!![adpaterPos]
        //intent.putExtra("scheduleId", scheduleId)
        when(type){
            1 -> {
                Log.e("Fragment: ","AppList: ${schedule.appList}")
                val intent  = Intent(context, CustomScheduleActivity::class.java)
                intent.putExtra("type",type)
                var bundle = Bundle()
                bundle.putParcelable("schedule", schedule)
                //intent.putParcelableArrayListExtra("appInfoList",  schedule.appInfoList)
                intent.putExtra("bundle", bundle)
                context?.startActivity(intent)
            }
            2 -> {
                schedule.active = true
                Log.e("Fragment: "," $schedule")
                viewModel.enableOrDisableSchedule(schedule)
            }
            3 -> {
                schedule.active = false
                Log.e("Fragment: "," $schedule")
                viewModel.enableOrDisableSchedule(schedule)
            }
            4 -> {
                CustomDialog(R.string.remove_schedule,
                    {viewModel.removeSchedule(schedule.id)},
                    R.string.dialog_yes_button,
                    R.string.dialog_no_button).
                        show(fragmentManager,"")

            }
        }

    }

    interface OnFragmentInteractionListener {

    }
}
