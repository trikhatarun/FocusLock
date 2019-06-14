package com.trikh.focuslock.widget.customschedulepopup

import android.annotation.SuppressLint
import android.content.Context
import android.view.MenuItem
import android.view.View
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import com.trikh.focuslock.R

@SuppressLint("RestrictedApi")
class CustomSchedulePopup(
    private val context:Context,
    menuBuilder: MenuBuilder,
    view: View,
    val onItemClicked: (id: Int)-> Unit
): MenuPopupHelper(
    context,
    menuBuilder,
    view),
MenuBuilder.Callback
{
   // val menuInflater= MenuInflater(context)
    init {
        //menuInflater.inflate(popUpView, menuBuilder)
        menuBuilder.setCallback(this)
        this.setForceShowIcon(true  )

    }

    override fun onMenuModeChange(menu: MenuBuilder?) {

    }

    override fun onMenuItemSelected(menu: MenuBuilder?, item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.edit -> {
                onItemClicked(1)
                dismiss()
                //Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show();
            }
            R.id.enable -> {
                onItemClicked(2)
                dismiss()
                //Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show();
            }
            R.id.disable -> {
                onItemClicked(3)
                dismiss()
                //Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show();
            }
            R.id.delete -> {
                onItemClicked(4)
                dismiss()
                //Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show();
            }
        }

        return true
    }



}