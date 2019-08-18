package com.trikh.focuslock.ui.schedule

import android.view.View
import android.widget.ListPopupWindow
import androidx.recyclerview.widget.RecyclerView
import com.trikh.focuslock.Application
import com.trikh.focuslock.R
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.utils.AutoFitGridLayoutManager
import com.trikh.focuslock.utils.TimeDurationUtils
import com.trikh.focuslock.utils.TimeUtils
import com.trikh.focuslock.utils.WeekDaysUtils
import com.trikh.focuslock.utils.extensions.getMenuOptions
import com.trikh.focuslock.utils.extensions.px
import kotlinx.android.synthetic.main.schedule_layout.view.*

open class ScheduleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    open fun bind(schedule: Schedule) {
        //Set background gray if schedule is disabled
        if (schedule.active!!) {
            itemView.backgroundView.visibility = View.GONE
        } else {
            itemView.backgroundView.visibility = View.VISIBLE
        }

        if (schedule.level == -1) {
            itemView.endTimeLabelTv.text = itemView.endTimeLabelTv.context.resources.getString(R.string.end_time)
            itemView.startTimeLabelTv.text = itemView.startTimeLabelTv.context.resources.getString(R.string.start_time)
        } else {
            itemView.endTimeLabelTv.text = itemView.endTimeLabelTv.context.resources.getString(R.string.awake_time)
            itemView.startTimeLabelTv.text = itemView.startTimeLabelTv.context.resources.getString(R.string.sleep_time)
        }

        val duration = TimeDurationUtils.calculateDuration(schedule.startTime, schedule.endTime)
        itemView.hours_tv.text = duration

        val level: Int = if (schedule.level == -1) {
            0
        } else {
            schedule.level!!
        }
        itemView.startTimeTv.text = TimeUtils.getSleepTime(schedule.startTime.time, level)
        itemView.endTimeTv.text = TimeUtils.getAwakeTime(schedule.endTime.time, level)

        if (schedule.level == -1) {
            itemView.levelTv.text =
                    schedule.selectedWeekDays?.let { it1 -> WeekDaysUtils.getNextWeek(it1, schedule.endTime) }
        } else {
            itemView.levelTv.text = itemView.context.getString(R.string.level_n, level)
        }

        itemView.blocked_apps_title.text =
                Application.instance.getString(R.string.blocked_apps, schedule.appList?.size)
        itemView.blockedAppsRv.layoutManager =
                AutoFitGridLayoutManager(Application.instance, 48)
        itemView.blockedAppsRv.adapter = BlockedAppsAdapter(schedule.appInfoList)

        itemView.options_iv.setOnClickListener {
            showPopupMenu(schedule, it)
        }
    }

    private fun showPopupMenu(schedule: Schedule, anchorView: View) {
        val popupMenu = ListPopupWindow(itemView.options_iv.context)
        popupMenu.anchorView = anchorView
        popupMenu.height = ListPopupWindow.WRAP_CONTENT
        popupMenu.width = 203.px
        popupMenu.setAdapter(ScheduleMenuOptionAdapter(schedule))
        popupMenu.show()
    }

    interface ScheduleInteractionListener {
        fun onScheduleInteraction(id: Long, position: Int)
    }
}