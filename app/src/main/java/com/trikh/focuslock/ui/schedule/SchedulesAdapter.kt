package com.trikh.focuslock.ui.schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListPopupWindow
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.trikh.focuslock.Application
import com.trikh.focuslock.R
import com.trikh.focuslock.data.model.InstantLockSchedule
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.utils.*
import com.trikh.focuslock.utils.Constants.Companion.CUSTOM_SCHEDULE
import com.trikh.focuslock.utils.Constants.Companion.DAILY_SCHEDULE
import com.trikh.focuslock.utils.Constants.Companion.INSTANT_LOCK
import kotlinx.android.synthetic.main.instant_lock_schedule.view.*
import kotlinx.android.synthetic.main.schedule_layout.view.*
import kotlinx.android.synthetic.main.schedule_layout.view.blockedAppsRv
import kotlinx.android.synthetic.main.schedule_layout.view.blockedListTv
import kotlinx.android.synthetic.main.schedule_layout.view.sleepTimeLabelTv
import java.util.*

class SchedulesAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var instantLock: InstantLockSchedule? = null
    private var scheduleList: List<Schedule> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            CUSTOM_SCHEDULE -> CustomScheduleViewHolder(inflater.inflate(R.layout.schedule_layout, parent, false))
            INSTANT_LOCK -> InstantLockViewHolder(inflater.inflate(R.layout.instant_lock_schedule, parent, false))
            else -> PrimaryScheduleViewHolder(inflater.inflate(R.layout.schedule_layout, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CustomScheduleViewHolder -> holder.bind(scheduleList[position])
            is PrimaryScheduleViewHolder -> holder.bind(scheduleList[position])
            is InstantLockViewHolder -> holder.bind(instantLock)
        }
    }

    override fun getItemCount(): Int {
        return if (instantLock != null) {
            scheduleList.size + 1
        } else {
            scheduleList.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0 && instantLock != null) {
            INSTANT_LOCK
        } else if (scheduleList[position].level == -1) {
            CUSTOM_SCHEDULE
        } else {
            DAILY_SCHEDULE
        }
    }

    fun setList(scheduleList: List<Schedule>?) {
        if (scheduleList != null) {
            this.scheduleList = scheduleList
        } else {
            this.scheduleList = emptyList()
        }
        notifyDataSetChanged()
    }

    fun setInstantLock(instantLock: InstantLockSchedule?) {
        this.instantLock = instantLock
        notifyDataSetChanged()
    }

    inner class InstantLockViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(instantLock: InstantLockSchedule?) {
            instantLock?.run {
                val startTime = Calendar.getInstance()
                val endTimeCalendar = Calendar.getInstance()
                endTimeCalendar.timeInMillis = endTime
                val appInfoList = IconsUtils(itemView.context).getIconsFromPackageManager(blockedApps)

                itemView.blockedAppsRv.layoutManager = AutoFitGridLayoutManager(Application.instance, 48)
                itemView.blockedAppsRv.adapter = BlockedAppsAdapter(appInfoList)
                itemView.blockedListTv.text = Application.instance.getString(R.string.blocked_apps, appInfoList.size)
                itemView.time.text = TimeDurationUtils.calculateDuration(startTime, endTimeCalendar)
            }
        }
    }

    inner class CustomScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(schedule: Schedule) {
            schedule.run {
                if (schedule.active!!) {
                    itemView.backgroundView.visibility = View.GONE
                } else {
                    itemView.backgroundView.visibility = View.VISIBLE
                }

                itemView.blockedListTv.text = itemView.blockedListTv.context.resources.getString(R.string.end_time)
                itemView.sleepTimeLabelTv.text = itemView.sleepTimeLabelTv.context.resources.getString(R.string.start_time)

                val duration = TimeDurationUtils.calculateDuration(startTime, endTime)

                itemView.hours_tv.text = duration
                itemView.sleepTimeTv.text = TimeUtils.getSleepTime(startTime.time, 0)
                itemView.awakeTimeTv.text = TimeUtils.getAwakeTime(endTime.time, 0)

                itemView.levelTv.text =
                        selectedWeekDays?.let { it1 -> WeekDaysUtils.getNextWeek(it1, endTime) }

                itemView.blocked_apps_title.text =
                        itemView.context.getString(R.string.blocked_apps, 0)
                itemView.options_iv.setOnClickListener {
                    val popupMenu = ListPopupWindow(itemView.options_iv.context)
                    //Todo: Customise Schedule menu adapter acco to schedule
                    popupMenu.setAdapter()
                }

                itemView.blockedAppsRv.layoutManager =
                        AutoFitGridLayoutManager(Application.instance, 48)
                itemView.blockedAppsRv.adapter = BlockedAppsAdapter(schedule.appInfoList)
                itemView.blocked_apps_title.text =
                        Application.instance.getString(R.string.blocked_apps, schedule.appList?.size)
            }
        }
    }

    inner class PrimaryScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(schedule: Schedule) {
            schedule.run {
                if (schedule.active!!) {
                    itemView.backgroundView.visibility = View.GONE
                } else {
                    itemView.backgroundView.visibility = View.VISIBLE
                }
                itemView.blockedListTv.text = itemView.blockedListTv.context.resources.getString(R.string.end_time)
                itemView.sleepTimeLabelTv.text = itemView.sleepTimeLabelTv.resources.getString(R.string.start_time)
                val duration = TimeDurationUtils.calculateDuration(startTime, endTime)
                itemView.hours_tv.text = duration

                itemView.sleepTimeTv.text = TimeUtils.getSleepTime(startTime.time, level!!)
                itemView.awakeTimeTv.text = TimeUtils.getAwakeTime(endTime.time, level)
                itemView.levelTv.text = itemView.context.getString(R.string.level_n, level)
                itemView.blocked_apps_title.text =
                        itemView.context.getString(R.string.blocked_apps, 0)

                itemView.options_iv.setOnClickListener {
                    //Todo: open menu 2
                }
                itemView.blockedAppsRv.layoutManager =
                        AutoFitGridLayoutManager(Application.instance, 48)
                itemView.blockedAppsRv.adapter = BlockedAppsAdapter(schedule.appInfoList)
                itemView.blocked_apps_title.text =
                        itemView.context.getString(R.string.blocked_apps, schedule.appList?.size)
            }
        }
    }

    interface ScheduleListInteractionListener {
        fun openPopUpMenu(id: Int)
    }
}