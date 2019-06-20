package com.trikh.focuslock.ui.onboarding.timepicker


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.trikh.focuslock.R
import com.trikh.focuslock.databinding.FragmentTimePickerBinding
import com.trikh.focuslock.utils.TimeDurationUtils
import com.trikh.focuslock.widget.customdialog.CustomDialog
import kotlinx.android.synthetic.main.fragment_time_picker.view.*
import java.util.*

class TimePickerFragment : Fragment() {
     private lateinit var timePickerViewModel: TimePickerViewModel

    private lateinit var binding: FragmentTimePickerBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_time_picker, container, false)
        timePickerViewModel = ViewModelProviders.of(this).get(TimePickerViewModel::class.java)
        binding.viewModel = timePickerViewModel
        binding.lifecycleOwner = this
        val root = binding.root
        root.timePicker.setOnTimeChangedListener(TimePicker.OnTimeChangedListener { view, hourOfDay, minute ->
            Log.d("TimePickerFragment:"," Hours: $hourOfDay Minutes: $minute")
            timePickerViewModel.hours.postValue(hourOfDay)
            timePickerViewModel.minutes.postValue(minute)
        })

        timePickerViewModel.sleepHours.observe(this, Observer {
            timePickerViewModel.blockText.postValue(getString(R.string.selected_sleep_time, it))
        })

        root.nextBtn.setOnClickListener {
            onButtonClicked()
        }


        return root
    }

    private fun onButtonClicked(){
        if (timePickerViewModel.sleepHours.value!! <=0){
            CustomDialog(R.string.minimum_sleep_time_msg,
                {},
                noButtonText = R.string.empty_string,
                yesButtonText = R.string.ok_text).show(fragmentManager, "")
        }else{
        val startTime = Calendar.getInstance()
        val endTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR, timePickerViewModel.hours.value!!)
        startTime.set(Calendar.MINUTE, timePickerViewModel.minutes.value!!)

        endTime.set(Calendar.HOUR, timePickerViewModel.sleepHours.value!!)

        findNavController().navigate(
            TimePickerFragmentDirections.actionTimePickerFragmentToBlockedAppsFragment(
                startTime,
                endTime
            )
        )
        val duration = TimeDurationUtils.calculateDuration(startTime,endTime)
        Log.d("TimePickerViewModel:"," duration: $duration")}
    }



}
