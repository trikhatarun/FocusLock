package com.trikh.focuslock.ui.schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trikh.focuslock.R
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.utils.TimeUtils
import kotlinx.android.synthetic.main.schedule_layout.view.*
import java.sql.Time

class ScheduleAdapter(private val scheduleList: List<Schedule>) :
    RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ScheduleViewHolder(inflater.inflate(R.layout.schedule_layout, parent, false))
    }

    override fun getItemCount() = scheduleList.size

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) =
        holder.bind(scheduleList[position])

    inner class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(schedule: Schedule) {
            schedule.run {
                itemView.sleepTimeTv.text = TimeUtils.getSleepTime(startTime.time,level!!)
                itemView.awakeTimeTv.text = TimeUtils.getAwakeTime(endTime.time, level)
                itemView.levelTv.text = itemView.context.getString(R.string.level_n,level)
                itemView.blocked_apps_title.text = itemView.context.getString(R.string.blocked_apps,0)
            }
        }
    }
}