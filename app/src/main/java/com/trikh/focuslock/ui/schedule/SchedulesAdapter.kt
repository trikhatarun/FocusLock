package com.trikh.focuslock.ui.schedule

import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListPopupWindow
import androidx.recyclerview.widget.RecyclerView
import com.trikh.focuslock.Application
import com.trikh.focuslock.R
import com.trikh.focuslock.data.model.InstantLockSchedule
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.utils.*
import com.trikh.focuslock.utils.Constants.Companion.INSTANT_LOCK
import com.trikh.focuslock.utils.Constants.Companion.SCHEDULE
import com.trikh.focuslock.utils.extensions.px
import kotlinx.android.synthetic.main.instant_lock_schedule.view.*
import kotlinx.android.synthetic.main.instant_lock_schedule.view.startTimeLabelTv
import kotlinx.android.synthetic.main.schedule_layout.view.*
import kotlinx.android.synthetic.main.schedule_layout.view.blockedAppsRv
import java.util.*

class SchedulesAdapter(val scheduleInteractionListener: ScheduleInteractionListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var instantLock: InstantLockSchedule? = null
    private var scheduleList: List<Schedule> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            INSTANT_LOCK -> InstantLockViewHolder(inflater.inflate(R.layout.instant_lock_schedule, parent, false))
            else -> ScheduleViewHolder(inflater.inflate(R.layout.schedule_layout, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (instantLock != null) {
            when (holder) {
                is ScheduleViewHolder -> holder.bind(scheduleList[position - 1])
                is InstantLockViewHolder -> holder.bind(instantLock)
            }
        } else {
            (holder as ScheduleViewHolder).bind(scheduleList[position])
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
        return if (instantLock != null) {
            when (position) {
                0 -> INSTANT_LOCK
                else -> SCHEDULE
            }
        } else {
            SCHEDULE
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
                val endTimeCalendar = Calendar.getInstance()
                endTimeCalendar.timeInMillis = endTime
                val appInfoList = IconsUtils(itemView.context).getIconsFromPackageManager(blockedApps)

                itemView.blockedAppsRv.layoutManager = AutoFitGridLayoutManager(Application.instance, 48)
                itemView.blockedAppsRv.adapter = BlockedAppsAdapter(appInfoList)
                itemView.blockedAppsLabelTv.text =
                    Application.instance.getString(R.string.blocked_apps, appInfoList.size)

                val timer = object: CountDownTimer(TimeDurationUtils.calculateDifferenceMillis(Calendar.getInstance(), endTimeCalendar), 60000) {
                    override fun onTick(millisUntilFinished: Long) {
                        itemView.time.text = TimeDurationUtils.calculateDuration(Calendar.getInstance(), endTimeCalendar)
                    }

                    override fun onFinish() {
                    }
                }
                timer.start()
            }
        }
    }

    inner class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        ScheduleMenuOptionAdapter.PopupMenuItemClickListener {

        private var schedule: Schedule? = null
        private var popupMenu: ListPopupWindow? = null

        fun bind(schedule: Schedule) {

            this.schedule = schedule

            //Set background gray if schedule is disabled
            if (schedule.level == -1) {
                if (schedule.active!!) {
                    itemView.backgroundView.visibility = View.GONE
                } else {
                    itemView.backgroundView.visibility = View.VISIBLE
                }
            }

            if (schedule.level == -1) {
                itemView.endTimeLabelTv.text = itemView.endTimeLabelTv.context.resources.getString(R.string.end_time)
                itemView.startTimeLabelTv.text =
                    itemView.startTimeLabelTv.context.resources.getString(R.string.start_time)
            } else {
                itemView.endTimeLabelTv.text = itemView.endTimeLabelTv.context.resources.getString(R.string.awake_time)
                itemView.startTimeLabelTv.text =
                    itemView.startTimeLabelTv.context.resources.getString(R.string.sleep_time)
            }

            val duration = TimeDurationUtils.calculateDurationRoundOffTen(schedule.startTime, schedule.endTime)
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
            if (popupMenu == null) {
                popupMenu = ListPopupWindow(itemView.options_iv.context)
                popupMenu?.anchorView = anchorView
                popupMenu?.height = ListPopupWindow.WRAP_CONTENT
                popupMenu?.width = 203.px
            }
            popupMenu?.setAdapter(ScheduleMenuOptionAdapter(schedule, this))
            popupMenu?.show()
        }

        override fun onItemClicked(id: Long) {
            popupMenu?.dismiss()
            schedule?.run {
                scheduleInteractionListener.onScheduleAction(id, this)
            }
        }
    }

    interface ScheduleInteractionListener {
        fun onScheduleAction(actionId: Long, schedule: Schedule)
    }
}