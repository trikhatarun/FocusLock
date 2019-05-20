package com.trikh.focuslock.widget.app_picker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import com.trikh.focuslock.R
import kotlinx.android.synthetic.main.app_picker_dialog.*


class AppPickerDialog : DialogFragment() {

    private val applicationListAdapter = AppsAdapter(ArrayList())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.app_picker_dialog, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appsRV.adapter = applicationListAdapter

        AndroidViewModelFactory.getInstance(activity!!.application)
            .create(AppListViewModel::class.java)
            .getAppInfoList()
            .observe(this, Observer { appList ->
                appList.forEach {

                }
            })
    }
}