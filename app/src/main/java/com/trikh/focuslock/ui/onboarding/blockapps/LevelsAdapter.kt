package com.trikh.focuslock.ui.onboarding.blockapps

import android.animation.ObjectAnimator
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.trikh.focuslock.R
import kotlinx.android.synthetic.main.level_card_rv_layout.view.*

class LevelsAdapter(private var list: List<String>, var listener: LevelCallBacks) :
    RecyclerView.Adapter<LevelsAdapter.ViewHolder>() {

    var context: Context? = null

    var lastCheckedPos: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        return ViewHolder(layoutInflater.inflate(R.layout.level_card_rv_layout, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(string: String) {

            if (adapterPosition == lastCheckedPos) {
                itemView.levelCb.isChecked = true
                /*val objectAnimator = ObjectAnimator.ofFloat(itemView.levelCb,"scaleY", 20f)
                objectAnimator.duration = 100
                objectAnimator.start()*/


            } else {
                itemView.levelCb.isChecked = false
            }
            itemView.levelCb.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    Log.d("LevelViewHolder:", "Level: $string")
                    listener.onLevelChanged(adapterPosition + 1)
                    lastCheckedPos = adapterPosition
                    notifyDataSetChanged()
                }
            }
        }
    }

    interface LevelCallBacks {
        fun onLevelChanged(level: Int)
    }
}