package com.trikh.focuslock.ui

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.trikh.focuslock.R
import com.trikh.focuslock.ui.schedule.ScheduleFragment
import com.trikh.focuslock.ui.settting.SettingsFragment
import com.trikh.focuslock.widget.extensions.setAppBarLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity(), ScheduleFragment.OnFragmentInteractionListener,
    SettingsFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arcToolbar.setAppBarLayout(appbar)

        bottomNavigationBar.itemIconTintList = null
        bottomNavigationBar.setupWithNavController(findNavController(R.id.container))
    }
}
