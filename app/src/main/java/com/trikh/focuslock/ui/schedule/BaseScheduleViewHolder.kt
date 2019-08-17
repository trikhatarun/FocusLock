package com.trikh.focuslock.ui.schedule

import android.view.View
import android.widget.AdapterView
import android.widget.ListPopupWindow
import androidx.recyclerview.widget.RecyclerView
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.utils.extensions.getMenuOptions
import com.trikh.focuslock.utils.extensions.px
import kotlinx.android.synthetic.main.schedule_layout.view.*

open class BaseScheduleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun showPopupMenu(schedule: Schedule, anchorView: View) {
        val popupMenu = ListPopupWindow(itemView.options_iv.context)
        popupMenu.anchorView = anchorView
        popupMenu.height = ListPopupWindow.WRAP_CONTENT
        popupMenu.width = 203.px
        popupMenu.setAdapter(ScheduleMenuOptionAdapter(schedule.getMenuOptions))
        popupMenu.show()
    }
}