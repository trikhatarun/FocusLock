package com.trikh.focuslock.widget.app_picker

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.recyclerview.widget.RecyclerView
import com.trikh.focuslock.R
import com.trikh.focuslock.utils.extensions.px
import kotlinx.android.synthetic.main.app_list_item.view.*

class AppsAdapter(private var applicationList: List<AppInfo>) : RecyclerView.Adapter<AppsAdapter.ApplicationViewHolder>() {

    private var filteredList = mutableListOf<AppInfo>()

    init {
        filteredList.addAll(applicationList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationViewHolder {
        return ApplicationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.app_list_item, parent, false))
    }

    override fun getItemCount() = filteredList.size


    override fun onBindViewHolder(holder: ApplicationViewHolder, position: Int) {
        holder.bind(filteredList[position])
    }

    fun updateList(applicationList: List<AppInfo>) {
        this.applicationList = applicationList
        this.filteredList = applicationList.toMutableList()
        notifyDataSetChanged()
    }

    fun getSelectedApplicationList(): List<AppInfo> {
        val selectedAppsList = ArrayList<AppInfo>()
        applicationList.forEach{
            if (it.blocked) {
                selectedAppsList.add(it)
            }
        }

        return selectedAppsList
    }

    fun filter(query: String?) {
        if (!query.isNullOrEmpty()) {
            filteredList = applicationList.filter { it.name.toLowerCase().contains(query.toLowerCase()) }.toMutableList()
        } else {
            filteredList.clear()
            filteredList.addAll(applicationList)
        }
        notifyDataSetChanged()
    }

    inner class ApplicationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        override fun onClick(v: View?) {
            updateData()
            updateView(filteredList[adapterPosition].blocked)
        }

        fun bind(appInfo: AppInfo) {
            itemView.appIcon.setImageDrawable(appInfo.icon)
            itemView.appName.text = appInfo.name
            itemView.setOnClickListener(this)
            updateView(appInfo.blocked)
        }

        private fun updateView(block: Boolean) {
            if (!block) {
                itemView.appIcon.elevation = 0.0f
                if (itemView.selectedAppIcon.visibility != View.GONE) {
                    val unblockAppSelectorAnimation: AnimatorSet = AnimatorInflater.loadAnimator(itemView.context, R.animator.hide_app_select_icon_animator) as AnimatorSet
                    unblockAppSelectorAnimation.setTarget(itemView.selectedAppIcon)
                    unblockAppSelectorAnimation.doOnEnd { itemView.selectedAppIcon.visibility = View.GONE }
                    unblockAppSelectorAnimation.start()
                }
            } else {
                itemView.appIcon.elevation = 2.px.toFloat()
                itemView.selectedAppIcon.visibility = View.VISIBLE
                val unblockAppSelectorAnimation: AnimatorSet = AnimatorInflater.loadAnimator(itemView.context, R.animator.show_app_select_icon_animator) as AnimatorSet
                unblockAppSelectorAnimation.setTarget(itemView.selectedAppIcon)
                unblockAppSelectorAnimation.start()
            }
        }

        private fun updateData() {
            applicationList.find { it.packageName == filteredList[adapterPosition].packageName }?.let {
                it.blocked = !it.blocked
            }
            filteredList[adapterPosition].blocked = !filteredList[adapterPosition].blocked
        }
    }
}