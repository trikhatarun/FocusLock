package com.trikh.focuslock.widget.app_picker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import com.trikh.focuslock.R
import com.trikh.focuslock.utils.AutoFitGridLayoutManager
import kotlinx.android.synthetic.main.app_picker_dialog.*


class AppPickerDialog(val interactionListener: InteractionListener) : DialogFragment() {

    private val applicationListAdapter = AppsAdapter(ArrayList())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.app_picker_dialog, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appsRV.layoutManager = AutoFitGridLayoutManager(view.context, 70)
        appsRV.adapter = applicationListAdapter

        AndroidViewModelFactory.getInstance(activity!!.application)
            .create(AppListViewModel::class.java)
            .getAppInfoList()
            .observe(this, Observer { appList ->
                applicationListAdapter.updateList(appList)
            })

        confirmationButton.setOnClickListener{
            interactionListener.onConfirm(applicationListAdapter.getSelectedApplicationList())
            dismiss()
        }

        cancelButton.setOnClickListener{
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        val params = dialog.window!!.attributes
        params.width = ConstraintLayout.LayoutParams.MATCH_PARENT
        params.height = ConstraintLayout.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = params as android.view.WindowManager.LayoutParams
    }

    public interface InteractionListener {
        fun onConfirm(applicationList: List<AppInfo>)
    }
}