package com.trikh.focuslock.ui.schedule

import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trikh.focuslock.R
import com.trikh.focuslock.widget.app_picker.AppInfo
import kotlinx.android.synthetic.main.blocked_app_list_item.view.*

class BlockedAppsAdapter(private var blockedAppList: List<AppInfo>) :
    RecyclerView.Adapter<BlockedAppsAdapter.BlockedAppViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockedAppViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return BlockedAppViewHolder(layoutInflater.inflate(R.layout.blocked_app_list_item, parent, false))
    }

    override fun getItemCount() = blockedAppList.size

    override fun onBindViewHolder(holder: BlockedAppViewHolder, position: Int) {
        holder.setApp(blockedAppList[position].icon)
    }

    fun updateList(list: List<AppInfo>){
        blockedAppList = list
        notifyDataSetChanged()
    }

    inner class BlockedAppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setApp(appIcon: Drawable) {
            itemView.appIcon.setImageDrawable(appIcon)
        }
    }
}