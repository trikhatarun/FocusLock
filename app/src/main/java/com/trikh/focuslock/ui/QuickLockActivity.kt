package com.trikh.focuslock.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.trikh.focuslock.R
import kotlinx.android.synthetic.main.activity_quick_lock.*

class QuickLockActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quick_lock)

        setSupportActionBar(toolbar_layout.findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.quick_lock)

        Handler().postDelayed({ set_quick_lock_btn.show() }, 500)
    }
}
