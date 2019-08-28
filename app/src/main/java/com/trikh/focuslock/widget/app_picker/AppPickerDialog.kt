package com.trikh.focuslock.widget.app_picker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.trikh.focuslock.R
import com.trikh.focuslock.utils.AutoFitGridLayoutManager
import com.trikh.focuslock.utils.ViewModelFactory
import kotlinx.android.synthetic.main.app_picker_dialog.*


class AppPickerDialog(
    private val selectedAppList: List<AppInfo>,
    private val interactionListener: InteractionListener
) : DialogFragment() {

    private val applicationListAdapter = AppsAdapter(ArrayList())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(context).inflate(R.layout.app_picker_dialog, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        appsRV.layoutManager = AutoFitGridLayoutManager(view.context, 70)

        appsRV.adapter = applicationListAdapter
        val columns =
            (appsRV.layoutManager as AutoFitGridLayoutManager).getColumnCountForAccessibility(
                appsRV.Recycler(),
                RecyclerView.State()
            )

        ViewModelProviders.of(this, ViewModelFactory(activity!!.application, selectedAppList))
            .get(AppListViewModel::class.java)
            .getAppInfoList()
            .observe(this, Observer { appList ->
                applicationListAdapter.updateList(appList)
            })

        confirmationButton.setOnClickListener {
            interactionListener.onConfirm(applicationListAdapter.getSelectedApplicationList())
            dismiss()
        }

        cancelButton.setOnClickListener {
            dismiss()
        }

        appsSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                //Do nothing
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                applicationListAdapter.filter(query)
                return true
            }

        })
    }

    override fun onResume() {
        super.onResume()
        val dialog = dialog
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    interface InteractionListener {
        fun onConfirm(applicationList: List<AppInfo>)
    }
}