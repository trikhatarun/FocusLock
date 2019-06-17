package com.trikh.focuslock.widget.customschedulepopup

import android.annotation.SuppressLint
import android.content.Context
import android.view.MenuItem
import android.view.View
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import com.trikh.focuslock.R
import com.trikh.focuslock.ui.schedule.ScheduleAdapter
import com.trikh.focuslock.utils.Constants

@SuppressLint("RestrictedApi")
class CustomSchedulePopup(
    private val context:Context,
    menuBuilder: MenuBuilder,
    view: View,
    val listener: ScheduleAdapter.PopupCallBacks,
    val adapter_position: Int
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
                listener.onItemClicked(Constants.POPUP_EDIT, adapter_position)
                dismiss()
                //Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show();
            }
            R.id.enable -> {
                listener.onItemClicked(Constants.POPUP_ENABLE, adapter_position)
                dismiss()
                //Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show();
            }
            R.id.disable -> {
                listener.onItemClicked(Constants.POPUP_DISABLE, adapter_position)
                dismiss()
                //Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show();
            }
            R.id.delete -> {
                listener.onItemClicked(Constants.POPUP_DELETE, adapter_position)
                dismiss()
                //Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show();
            }
        }

        return true
    }





}