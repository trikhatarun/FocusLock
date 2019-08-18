package com.trikh.focuslock.ui.schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trikh.focuslock.Application
import com.trikh.focuslock.R
import com.trikh.focuslock.data.model.InstantLockSchedule
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.utils.*
import com.trikh.focuslock.utils.Constants.Companion.INSTANT_LOCK
import com.trikh.focuslock.utils.Constants.Companion.SCHEDULE
import kotlinx.android.synthetic.main.instant_lock_schedule.view.*
import kotlinx.android.synthetic.main.schedule_layout.view.blockedAppsRv
import java.util.*

class SchedulesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
                val startTime = Calendar.getInstance()
                val endTimeCalendar = Calendar.getInstance()
                endTimeCalendar.timeInMillis = endTime
                val appInfoList = IconsUtils(itemView.context).getIconsFromPackageManager(blockedApps)

                itemView.blockedAppsRv.layoutManager = AutoFitGridLayoutManager(Application.instance, 48)
                itemView.blockedAppsRv.adapter = BlockedAppsAdapter(appInfoList)
                itemView.blockedAppsLabelTv.text = Application.instance.getString(R.string.blocked_apps, appInfoList.size)
                itemView.time.text = TimeDurationUtils.calculateDuration(startTime, endTimeCalendar)
            }
        }
    }
}