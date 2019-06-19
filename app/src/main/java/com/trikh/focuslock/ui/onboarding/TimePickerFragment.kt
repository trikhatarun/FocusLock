package com.trikh.focuslock.ui.onboarding


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

import com.trikh.focuslock.R
import com.trikh.focuslock.databinding.FragmentTimePickerBinding
import com.trikh.focuslock.ui.instantlock.InstantLockViewModel
import kotlinx.android.synthetic.main.fragment_time_picker.*
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



        return root
    }



}
