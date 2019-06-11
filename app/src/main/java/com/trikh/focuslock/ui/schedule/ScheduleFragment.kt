package com.trikh.focuslock.ui.schedule

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TimeUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.trikh.focuslock.R
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.widget.app_picker.AppInfo
import com.trikh.focuslock.widget.app_picker.AppPickerDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_schedule.*
import java.lang.RuntimeException
import java.util.*
import java.util.logging.Level
import kotlin.collections.ArrayList

class ScheduleFragment : Fragment() {
    private lateinit var viewModel: AddScheduleViewModel

    private var listener: OnFragmentInteractionListener? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(AddScheduleViewModel::class.java)
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
        /*activity?.mainTv1?.visibility = View.GONE
        activity?.mainTv2?.visibility = View.GONE*/

        schedulesRv.isNestedScrollingEnabled = false
        schedulesRv.adapter = ScheduleAdapter(getMockSchedules())
        viewModel.scheduleList.observe(this, androidx.lifecycle.Observer { it ->
            schedulesRv.adapter = ScheduleAdapter(it)

            if (it.isNotEmpty()) {
                it[it.size - 1].let {
                    Log.e(
                        "Schedule Fragment : ",
                        "last Package Name: ${it.appList.last()}"
                    )
                }
            }
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

    interface OnFragmentInteractionListener {

    }
}
