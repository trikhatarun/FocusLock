package com.trikh.focuslock.ui.schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListAdapter
import android.widget.TextView
import com.trikh.focuslock.R

class ScheduleMenuOptionAdapter(
    private val optionList: List<PopupMenuOption>,
    private val itemClickListener: PopupMenuItemClickListener) : BaseAdapter(), ListAdapter {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val itemView = convertView
            ?: LayoutInflater.from(parent?.context).inflate(R.layout.menu_item, parent, false)

        val menuOption = itemView.findViewById<TextView>(R.id.menu_option)
        val currentOption = (getItem(position) as PopupMenuOption)
        menuOption.text = currentOption.title
        menuOption.setCompoundDrawablesRelativeWithIntrinsicBounds(
            menuOption.context.getDrawable(currentOption.iconRes),
            null,
            null,
            null
        )

        menuOption.setOnClickListener {
            itemClickListener.onItemClicked(currentOption.id)
        }

        return itemView!!
    }

    override fun getItem(position: Int): Any {
        return optionList[position]
    }

    override fun getItemId(position: Int): Long {
        return optionList[position].id
    }

    override fun getCount(): Int {
        return optionList.size
    }

    interface PopupMenuItemClickListener {
        fun onItemClicked(id: Long)
    }
}