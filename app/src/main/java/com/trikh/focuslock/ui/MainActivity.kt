package com.trikh.focuslock.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.snackbar.Snackbar
import com.trikh.focuslock.R
import com.trikh.focuslock.ui.schedule.EditScheduleFragment
import com.trikh.focuslock.ui.schedule.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(bottomAppBar)

        supportFragmentManager.beginTransaction().replace(R.id.container, HomeFragment()).commit()

        fab.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.container, EditScheduleFragment())
                .addToBackStack(null).commit()
            bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
        }
    }

    override fun onBackPressed() {
        bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
        super.onBackPressed()
    }
}
