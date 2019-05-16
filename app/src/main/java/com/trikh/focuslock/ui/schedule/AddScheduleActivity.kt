package com.trikh.focuslock.ui.schedule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.trikh.focuslock.R
import com.trikh.focuslock.widget.extensions.setAppBarLayout
import kotlinx.android.synthetic.main.toolbar.*

class AddScheduleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_schedule)

        arcToolbar.setAppBarLayout(appbar)
    }
}
