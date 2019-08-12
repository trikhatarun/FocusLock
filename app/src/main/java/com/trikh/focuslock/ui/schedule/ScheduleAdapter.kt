package com.trikh.focuslock.ui.schedule

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log

import android.view.*
import androidx.appcompat.view.menu.MenuBuilder
import androidx.recyclerview.widget.RecyclerView
import com.trikh.focuslock.Application
import com.trikh.focuslock.R
import com.trikh.focuslock.data.model.Schedule
import com.trikh.focuslock.utils.AutoFitGridLayoutManager
import com.trikh.focuslock.utils.Constants.Companion.CUSTOM_SCHEDULE
import com.trikh.focuslock.utils.Constants.Companion.DAILY_SCHEDULE
import com.trikh.focuslock.utils.TimeDurationUtils

import com.trikh.focuslock.utils.TimeUtils
import com.trikh.focuslock.utils.WeekDaysUtils
import com.trikh.focuslock.widget.customschedulepopup.CustomSchedulePopup
import kotlinx.android.synthetic.main.schedule_layout.view.*
import kotlinx.android.synthetic.main.schedule_layout.view.blockedListTv
import kotlinx.android.synthetic.main.schedule_layout.view.blocked_apps_title
import kotlinx.android.synthetic.main.schedule_layout.view.sleepTimeLabelTv

class ScheduleAdapter(private var scheduleList: List<Schedule>, val listener: PopupCallBacks) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var context: Context? = null

    override fun getItemViewType(position: Int): Int {
        return if (scheduleList[position].level == -1) CUSTOM_SCHEDULE else DAILY_SCHEDULE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context

        return when (viewType) {
            CUSTOM_SCHEDULE -> CustomScheduleViewHolder(
                inflater.inflate(
                    R.layout.schedule_layout,
                    parent,
                    false
                )
            )
            else -> ScheduleViewHolder(inflater.inflate(R.layout.schedule_layout, parent, false))
        }
    }

    override fun getItemCount() = scheduleList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ScheduleViewHolder) {
            holder.bind(scheduleList[position])
        } else if (holder is CustomScheduleViewHolder) {
            holder.bind(scheduleList[position])
        }
    }

    inner class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("RestrictedApi")
        fun bind(schedule: Schedule) {
            schedule.run {
                if (schedule.active!!) {
                    itemView.backgroundView.visibility = View.GONE
                } else {
                    itemView.backgroundView.visibility = View.VISIBLE
                }
                itemView.blockedListTv.text = context?.resources?.getString(R.string.end_time)
                itemView.sleepTimeLabelTv.text = context?.resources?.getString(R.string.start_time)
                val duration = TimeDurationUtils.calculateDuration(startTime, endTime)
                itemView.hours_tv.text = duration

                itemView.sleepTimeTv.text = TimeUtils.getSleepTime(startTime.time, level!!)
                itemView.awakeTimeTv.text = TimeUtils.getAwakeTime(endTime.time, level)
                itemView.levelTv.text = itemView.context.getString(R.string.level_n, level)
                itemView.blocked_apps_title.text =
                    itemView.context.getString(R.string.blocked_apps, 0)

                itemView.options_iv.setOnClickListener {
                    context?.let {
                        val builder = MenuBuilder(it)
                        MenuInflater(it).inflate(R.menu.custom_schedule_popup_menu, builder)
                        builder.rootMenu.removeItem(R.id.enable)
                        builder.rootMenu.removeItem(R.id.disable)
                        builder.rootMenu.removeItem(R.id.delete)
                        CustomSchedulePopup(
                            it,
                            builder,
                            itemView.options_iv,
                            listener,
                            adapterPosition
                        ).show()
                    }

                }
                itemView.blockedAppsRv.layoutManager =
                    AutoFitGridLayoutManager(Application.instance, 48)
                itemView.blockedAppsRv.adapter = BlockedAppsAdapter(schedule.appInfoList)
                itemView.blocked_apps_title.text =
                    itemView.context.getString(R.string.blocked_apps, schedule.appList?.size)
            }
        }
    }

    fun setList(list: List<Schedule>) {
        this.scheduleList = list
        notifyDataSetChanged()
    }

    inner class CustomScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        @SuppressLint("RestrictedApi")
        fun bind(schedule: Schedule) {
            schedule.run {
                if (schedule.active!!) {
                    itemView.backgroundView.visibility = View.GONE
                } else {
                    itemView.backgroundView.visibility = View.VISIBLE
                }


                itemView.blockedListTv.text = context?.resources?.getString(R.string.end_time)
                itemView.sleepTimeLabelTv.text = context?.resources?.getString(R.string.start_time)
                val duration = TimeDurationUtils.calculateDuration(startTime, endTime)
                //Log.d("ScheduleAdapter:","Time Duration $duration")
                itemView.hours_tv.text = duration
                itemView.sleepTimeTv.text = TimeUtils.getSleepTime(startTime.time, 0)
                itemView.awakeTimeTv.text = TimeUtils.getAwakeTime(endTime.time, 0)

                itemView.levelTv.text =
                    selectedWeekDays?.let { it1 -> WeekDaysUtils.getNextWeek(it1, endTime) }

                itemView.blocked_apps_title.text =
                    itemView.context.getString(R.string.blocked_apps, 0)
                itemView.options_iv.setOnClickListener {
                    context?.let {
                        val builder = MenuBuilder(it)
                        MenuInflater(it).inflate(R.menu.custom_schedule_popup_menu, builder)
                        if (schedule.active!!) {
                            builder.rootMenu.removeItem(R.id.enable)
                        } else {
                            builder.rootMenu.removeItem(R.id.disable)
                        }
                        CustomSchedulePopup(
                            it,
                            builder,
                            itemView.options_iv,
                            listener,
                            adapterPosition
                        ).show()
                    }
                }

                itemView.blockedAppsRv.layoutManager =
                    AutoFitGridLayoutManager(Application.instance, 48)
                itemView.blockedAppsRv.adapter = BlockedAppsAdapter(schedule.appInfoList)
                itemView.blocked_apps_title.text =
                    Application.instance.getString(R.string.blocked_apps, schedule.appList?.size)
            }
        }
    }


    interface PopupCallBacks {
        fun onItemClicked(type: String, adpaterPos: Int)
    }


}