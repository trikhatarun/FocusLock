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
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        if (scheduleList[position].level == null) {
            return 0;
        } else return 1;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            0 -> CustomScheduleViewHolder(inflater.inflate(R.layout.schedule_layout, parent, false))
            else -> ScheduleViewHolder(
                inflater.inflate(
                    R.layout.schedule_layout,
                    parent,
                    false
                )
            )
        }
    }

    //fun addList(list: ArrayList<Schedule>){this.scheduleList.addAll(list)}

    override fun getItemCount() = scheduleList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ScheduleViewHolder) {
            holder.bind(scheduleList[position])
        } else {
            (holder as CustomScheduleViewHolder).bind(scheduleList[position])
        }
    }

    inner class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(schedule: Schedule) {
            schedule.run {
                itemView.sleepTimeTv.text = TimeUtils.getSleepTime(startTime.time, level!!)
                itemView.awakeTimeTv.text = TimeUtils.getAwakeTime(endTime.time, level)
                itemView.levelTv.text = itemView.context.getString(R.string.level_n, level)
                itemView.blocked_apps_title.text =
                    itemView.context.getString(R.string.blocked_apps, 0)
            }
        }
    }

    inner class CustomScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(schedule: Schedule) {
            schedule.run {
                itemView.sleepTimeTv.text = TimeUtils.getSleepTime(startTime.time, 0)
                itemView.awakeTimeTv.text = TimeUtils.getAwakeTime(endTime.time, 0)
                itemView.levelTv.visibility = View.GONE
                itemView.blocked_apps_title.text =
                    itemView.context.getString(R.string.blocked_apps, 0)
            }
        }
    }
}