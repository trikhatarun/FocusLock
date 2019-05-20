package com.trikh.focuslock.widget.app_picker

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.core.animation.doOnEnd
import androidx.recyclerview.widget.RecyclerView
import com.trikh.focuslock.R
import com.trikh.focuslock.utils.extensions.px
import kotlinx.android.synthetic.main.app_list_item.view.*

class AppsAdapter(private var applicationList: List<AppInfo>) :
    RecyclerView.Adapter<AppsAdapter.ApplicationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationViewHolder {
        return ApplicationViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.app_list_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = applicationList.size


    override fun onBindViewHolder(holder: ApplicationViewHolder, position: Int) {
        holder.bind(applicationList[position])
    }

    fun updateList(applicationList: List<AppInfo>) {
        this.applicationList = applicationList
        notifyDataSetChanged()
    }

    inner class ApplicationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        override fun onClick(v: View?) {
            updateData()
            updateView(applicationList[adapterPosition].blocked)
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
                    val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0f, 0.5f)
                    val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0f, 0.5f)
                    val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 1.0f, 0.0f)

                    val unblockAppSelectorAnimation = ObjectAnimator.ofPropertyValuesHolder(
                        itemView.selectedAppIcon,
                        scaleX,
                        scaleY,
                        alpha
                    ).apply {
                        interpolator = OvershootInterpolator()
                        duration = 400
                    }
                    unblockAppSelectorAnimation.doOnEnd {
                        itemView.selectedAppIcon.visibility = View.GONE
                    }

                    unblockAppSelectorAnimation.start()
                }
            } else {
                itemView.selectedAppIcon.elevation = 2.px.toFloat()
                itemView.selectedAppIcon.visibility = View.VISIBLE
                val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.5f, 1.0f)
                val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.5f, 1.0f)
                val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0.0f, 1.0f)

                val unblockAppSelectorAnimation = ObjectAnimator.ofPropertyValuesHolder(
                    itemView.selectedAppIcon,
                    scaleX,
                    scaleY,
                    alpha
                ).apply {
                    interpolator = OvershootInterpolator()
                    duration = 400
                }

                unblockAppSelectorAnimation.start()
            }
        }

        private fun updateData() {
            applicationList[adapterPosition].blocked = !applicationList[adapterPosition].blocked
        }
    }
}