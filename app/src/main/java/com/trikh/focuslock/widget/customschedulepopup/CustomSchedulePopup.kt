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
class CustomSchedulePopup(context: Context, menuBuilder: MenuBuilder, view: View,
                          val listener: ScheduleAdapter.PopupCallBacks,
                          private val adapterPosition: Int): MenuPopupHelper(context, menuBuilder, view), MenuBuilder.Callback {
    init {
        menuBuilder.setCallback(this)
        this.setForceShowIcon(true)
    }

    override fun onMenuModeChange(menu: MenuBuilder?) {

    }

    override fun onMenuItemSelected(menu: MenuBuilder?, item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.edit -> {
                listener.onItemClicked(Constants.POPUP_EDIT, adapterPosition)
                dismiss()
            }
            R.id.enable -> {
                listener.onItemClicked(Constants.POPUP_ENABLE, adapterPosition)
                dismiss()
            }
            R.id.disable -> {
                listener.onItemClicked(Constants.POPUP_DISABLE, adapterPosition)
                dismiss()
            }
            R.id.delete -> {
                listener.onItemClicked(Constants.POPUP_DELETE, adapterPosition)
                dismiss()
            }
        }

        return true
    }
}