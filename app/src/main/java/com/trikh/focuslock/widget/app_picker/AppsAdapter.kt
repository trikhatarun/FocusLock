package com.trikh.focuslock.widget.app_picker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trikh.focuslock.R
import kotlinx.android.synthetic.main.app_list_item.view.*

class AppsAdapter(private val applicationList: List<AppInfo>) : RecyclerView.Adapter<AppsAdapter.ApplicationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationViewHolder {
        return ApplicationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.app_list_item, parent, false))
    }

    override fun getItemCount() = applicationList.size


    override fun onBindViewHolder(holder: ApplicationViewHolder, position: Int) {
        holder.bind(applicationList[position])
    }

    class ApplicationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(appInfo: AppInfo) {
            itemView.appIcon.setImageDrawable(appInfo.icon)
            itemView.appName.text = appInfo.name
        }
    }
}