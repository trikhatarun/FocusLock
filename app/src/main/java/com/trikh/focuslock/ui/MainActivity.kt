package com.trikh.focuslock.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.trikh.focuslock.R
import com.trikh.focuslock.ui.instantlock.InstantLockActivity
import com.trikh.focuslock.ui.schedule.CustomScheduleActivity
import com.trikh.focuslock.ui.schedule.ScheduleFragment

import com.trikh.focuslock.ui.settings.SettingsFragment
import com.trikh.focuslock.utils.extensions.hasUsageStatsPermission
import com.trikh.focuslock.widget.arctoolbar.setAppBarLayout
import com.trikh.focuslock.widget.customdialog.CustomDialog
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

    override fun onResume() {
        super.onResume()
        if (!hasUsageStatsPermission){
            CustomDialog(R.string.usage_permission_request_message,this::requestUsagePermission,R.string.grant,R.string.deny).show(supportFragmentManager,null)
        }
    }

    override fun onNewIntent(intent: Intent?) {
            super.onNewIntent(intent)
        if (Intent.ACTION_VIEW.equals(intent?.action)){
            Toast.makeText(this,"Open via link",Toast.LENGTH_SHORT).show()

        }
    }

    fun onAddScheduleClick(v: View) {
        findNavController(R.id.container).navigate(R.id.customSchedule)
        fabMenu.toggle(true)
    }

    fun onInstantLockClick(v: View) {
        findNavController(R.id.container).navigate(R.id.instantLock)
        fabMenu.toggle(true)
    }

    private fun requestUsagePermission(){
        startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
    }
}
