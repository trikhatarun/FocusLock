package com.trikh.focuslock.ui.schedule

import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuBuilder
import androidx.recyclerview.widget.RecyclerView
import com.trikh.focuslock.Application
import com.trikh.focuslock.R
import com.trikh.focuslock.data.model.InstantLockSchedule
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.utils.AutoFitGridLayoutManager
import com.trikh.focuslock.utils.TimeDurationUtils
import com.trikh.focuslock.utils.TimeUtils
import com.trikh.focuslock.widget.customschedulepopup.CustomSchedulePopup
import kotlinx.android.synthetic.main.schedule_layout.view.*

class SchedulesAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var instantLock: InstantLockSchedule? = null
    private var scheduleList: List<Schedule> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    inner class InstantLockViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(instantLock: InstantLockSchedule?) {

        }
    }

    inner class ScheduleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
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

                //Todo: Move to activity. use callbacks to inflate
                itemView.options_iv.setOnClickListener {
                    val builder = MenuBuilder(it.context)
                    MenuInflater(it.context).inflate(R.menu.custom_schedule_popup_menu, builder)
                    builder.rootMenu.removeItem(R.id.enable)
                    builder.rootMenu.removeItem(R.id.disable)
                    builder.rootMenu.removeItem(R.id.delete)
                    CustomSchedulePopup(
                        it.context,
                        builder,
                        itemView.options_iv,
                        listener,
                        adapterPosition
                    ).show()
                }
                itemView.blockedAppsRv.layoutManager =
                    AutoFitGridLayoutManager(Application.instance, 48)
                itemView.blockedAppsRv.adapter = BlockedAppsAdapter(schedule.appInfoList)
                itemView.blocked_apps_title.text =
                    itemView.context.getString(R.string.blocked_apps, schedule.appList?.size)
            }
        }
    }
}