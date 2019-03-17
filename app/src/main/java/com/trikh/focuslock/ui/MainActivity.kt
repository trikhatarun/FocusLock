package com.trikh.focuslock.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomappbar.BottomAppBar
import com.trikh.focuslock.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(bottomAppBar)

        fab.setOnClickListener {
            fab.hide()
            startActivity(Intent(this@MainActivity, QuickLockActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        fab.show()
    }
}
