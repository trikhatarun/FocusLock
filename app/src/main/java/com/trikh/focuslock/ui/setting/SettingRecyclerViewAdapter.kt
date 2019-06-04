package com.trikh.focuslock.ui.setting

import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.StringRes

import androidx.recyclerview.widget.RecyclerView
import com.trikh.focuslock.R
import com.trikh.focuslock.utils.setToastOffsets
import kotlinx.android.synthetic.main.emergency_toast_layout.view.*
import kotlinx.android.synthetic.main.setting_layout.view.*

class SettingRecyclerViewAdapter(
    private val items: ArrayList<SettingModel>,
    val listener: AdapterInteractionListener
) : RecyclerView.Adapter<SettingRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.setting_layout, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(s: SettingModel) {
            val context: Context = itemView.context

            if (adapterPosition == 0) {
                itemView.titleTv?.typeface = Typeface.createFromAsset(context.assets, "font/muli_bold.ttf")
                itemView.titleTv?.setTextColor(context.resources.getColor(R.color.colorPink))
            }

            itemView.iconImg.setImageResource(s.image)
            itemView.titleTv.setText(s.title)

            itemView.setOnClickListener { listener.onItemClick(s.title) }
        }

        /*private fun setEmergencyToast(context: Context, mView: ViewGroup, list: Array<Int>) {

            //val inflater: LayoutInflater
            val layout =
                LayoutInflater.from(context).inflate(R.layout.emergency_toast_layout, mView, false)
            layout.emergencyToastTv.text = context.resources?.getString(R.string.emergency_toast)
            with(Toast(context)) {
                setGravity(Gravity.NO_GRAVITY, list[0], list[1])
                duration = Toast.LENGTH_LONG
                val params = layout.layoutParams as LinearLayout.LayoutParams
                params.setMargins(74, 0, 80, 0)
                layout.layoutParams = params
                view = layout
                view.layoutParams = params
                show()
            }
        }*/
    }

    interface AdapterInteractionListener{
        fun onItemClick(@StringRes item : Int)
    }
}