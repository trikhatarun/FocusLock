package com.trikh.focuslock.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.DialogTitle
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.trikh.focuslock.R
import com.trikh.focuslock.ui.instantlock.InstantLockActivity
import com.trikh.focuslock.ui.schedule.AddScheduleActivity
import com.trikh.focuslock.ui.schedule.ScheduleFragment
import com.trikh.focuslock.ui.settting.SettingsFragment
import com.trikh.focuslock.widget.arctoolbar.setAppBarLayout
import com.trikh.focuslock.widget.customdialog.CustomDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity(), ScheduleFragment.OnFragmentInteractionListener,
    SettingsFragment.OnFragmentInteractionListener{

    override fun onFragmentInteraction(uri: Uri) {

    }

    /*private fun showDialog(title: String, yesText: String, noText: String){
        val dialog = CustomDialog(title,noText,yesText)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        dialog.show(fragmentTransaction,"")
    }

    override fun onSelectYes() {
        Toast.makeText(this,"Yes is Clicked",Toast.LENGTH_SHORT).show()

        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSelectNo() {
        Toast.makeText(this,"No is Clicked",Toast.LENGTH_SHORT).show()
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arcToolbar.setAppBarLayout(appbar)

        bottomNavigationBar.itemIconTintList = null
        bottomNavigationBar.setupWithNavController(findNavController(R.id.container))
        //showDialog("Are you really want to unblock the apps","YES", "NO")
    }

    fun onAddScheduleClick(v: View) {
        startActivity(Intent(this, AddScheduleActivity::class.java))
        fabMenu.toggle(true)
    }

    fun onInstantLockClick(v: View) {
        startActivity(Intent(this, InstantLockActivity::class.java))
        fabMenu.toggle(true)
    }
}
