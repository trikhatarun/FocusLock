package com.trikh.focuslock.ui.onboarding.timepicker


import android.app.usage.UsageEvents
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.TimePicker
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.trikh.focuslock.R
import com.trikh.focuslock.databinding.FragmentTimePickerBinding
import com.trikh.focuslock.utils.TimeDurationUtils
import com.trikh.focuslock.widget.customdialog.CustomDialog
import kotlinx.android.synthetic.main.activity_custom_schedule.*
import kotlinx.android.synthetic.main.fragment_time_picker.view.*
import kotlinx.android.synthetic.main.fragment_time_picker.view.nestedScrollView
import kotlinx.android.synthetic.main.fragment_time_picker.view.timePicker
import kotlinx.android.synthetic.main.instant_lock_schedule.view.*
import java.util.*
import kotlin.math.min

class TimePickerFragment : Fragment() {

    private lateinit var timePickerViewModel: TimePickerViewModel

    private lateinit var binding: FragmentTimePickerBinding




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_time_picker, container, false)
        timePickerViewModel = ViewModelProviders.of(this).get(TimePickerViewModel::class.java)
        binding.viewModel = timePickerViewModel
        binding.lifecycleOwner = this
        val root = binding.root
        root.timePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
            Log.d("TimePickerFragment:", " Hours: $hourOfDay Minutes: $minute")
            timePickerViewModel.hours.postValue(hourOfDay)
            timePickerViewModel.minutes.postValue(minute)
        }




        root.timePicker.setOnTouchListener { _, _ ->
            root.nestedScrollView.requestDisallowInterceptTouchEvent(true)
            false
        }


        timePickerViewModel.sleepHours.observe(this, androidx.lifecycle.Observer {

            root.findViewWithTag<TextView>(it.toString()).setTextColor(resources.getColor(R.color.colorPrimary))
            for (i in 0..12){
                if (i!= it){
                    root.findViewWithTag<TextView>(i.toString()).setTextColor(resources.getColor(R.color.colorSecondaryText))
                }
            }
            var minutes = 0
            var hours = 4
            when (it % 2) {
                0 -> {
                    hours = (it / 2) + 4
                    minutes = 0
                }
                else -> {
                    hours = ((it - 1) / 2) + 4
                    minutes = 30

                }

            }

            timePickerViewModel.blockText.postValue(
                getString(
                    R.string.selected_sleep_time,
                    hours,
                    minutes
                )
            )
            Log.d("SleepHours:", "it: $it  hours:$hours  minutes: $minutes")

        })




        /*root.hourSeekBar.setOnSeekbarFinalValueListener {
            val minutes = when(((it.toFloat().times(10F)).toInt()) % 10){
                5 -> 30
                else -> 0
            }
            val hours = it.toInt()
            Log.e("Minutes", "$minutes  hours: $hours")

            timePickerViewModel.blockText.postValue(getString(R.string.selected_sleep_time, hours, minutes))
            timePickerViewModel.sleepHours.postValue(hours)
            timePickerViewModel.sleepMinutes.postValue(minutes)
        }*/

        root.nextBtn.setOnClickListener {
            onButtonClicked()
        }


        return root
    }



    private fun onButtonClicked() {
        if (timePickerViewModel.sleepHours.value!! < 0) {
            CustomDialog(
                R.string.minimum_sleep_time_msg,
                {},
                noButtonText = R.string.empty_string,
                yesButtonText = R.string.ok_text
            ).show(fragmentManager, "")
        } else {
            Log.e(
                "TimePickerFragment:",
                "${timePickerViewModel.hours.value}  ${timePickerViewModel.minutes.value}  ${timePickerViewModel.sleepHours.value}"
            )
            val startTime = timePickerViewModel.getStartTime()
            val endTime = timePickerViewModel.getSleepTime()


            Log.d("endTime", "$endTime")


            findNavController().navigate(
                TimePickerFragmentDirections.actionTimePickerFragmentToBlockedAppsFragment(
                    startTime,
                    endTime
                )
            )
            val duration = TimeDurationUtils.calculateDuration(startTime, endTime)
            Log.d("TimePickerViewModel:", " duration: $duration")
        }
    }
enum class slider{


}

}


