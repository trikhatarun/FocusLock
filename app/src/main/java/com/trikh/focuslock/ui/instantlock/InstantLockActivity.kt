package com.trikh.focuslock.ui.instantlock

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.trikh.focuslock.R
import com.trikh.focuslock.widget.arctoolbar.setAppBarLayout
import kotlinx.android.synthetic.main.toolbar.*

class InstantLockActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instant_lock)

        arcToolbar.setAppBarLayout(appbar)
        setSupportActionBar(toolbar)
        toolbar_title.text = getString(R.string.instant_lock)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_option_menu, menu)
        return true
    }
}
