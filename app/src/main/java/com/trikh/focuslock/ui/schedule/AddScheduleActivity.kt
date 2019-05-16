package com.trikh.focuslock.ui.schedule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.trikh.focuslock.R
import com.trikh.focuslock.widget.OnSliderRangeMovedListener
import com.trikh.focuslock.widget.extensions.setAppBarLayout
import kotlinx.android.synthetic.main.activity_add_schedule.*
import kotlinx.android.synthetic.main.toolbar.*
import java.text.SimpleDateFormat
import java.util.*

class AddScheduleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_schedule)

        arcToolbar.setAppBarLayout(appbar)

        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        sleep_time_tv.text = timeFormat.format(time_picker.start.time)
        awake_time_tv.text = timeFormat.format(time_picker.end.time)

        time_picker.setOnChangeListener(object : OnSliderRangeMovedListener {
            override fun onChange(start: Calendar, end: Calendar) {
                sleep_time_tv.text = timeFormat.format(start.time)
                awake_time_tv.text = timeFormat.format(end.time)
            }
        })

        time_picker.setOnTouchListener { v, event ->
            nestedScrollView.requestDisallowInterceptTouchEvent(true)
            false
        }
    }
}
