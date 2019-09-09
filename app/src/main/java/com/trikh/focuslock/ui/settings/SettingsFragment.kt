package com.trikh.focuslock.ui.settings

import android.app.ActivityManager
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.trikh.focuslock.Application
import com.trikh.focuslock.R
import com.trikh.focuslock.data.source.ScheduleRepository
import com.trikh.focuslock.ui.MainActivity
import com.trikh.focuslock.ui.appblock.AppBlockService
import com.trikh.focuslock.ui.appblock.StartServiceReceiver
import com.trikh.focuslock.utils.Constants
import com.trikh.focuslock.utils.Constants.Companion.FEEDBACK_EMAIL
import com.trikh.focuslock.utils.Constants.Companion.PLAIN_TEXT
import com.trikh.focuslock.utils.Constants.Companion.SHARE_FOCUS_LOCK
import com.trikh.focuslock.widget.customAboutDialog.CustomAboutDialog
import com.trikh.focuslock.widget.customEmergencyDialog.CustomEmergencyDialog
import com.trikh.focuslock.widget.customFeedbackDialog.CustomFeedbackDialog
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*
import kotlin.collections.ArrayList


class SettingsFragment : Fragment(), SettingRecyclerViewAdapter.AdapterInteractionListener,
    CustomEmergencyDialog.DialogListener, CustomFeedbackDialog.DialogListener {
    val repository = ScheduleRepository()


    override fun onBlock() {
        repository.setEmergencyModeOn().subscribeBy {
            Log.d("SettingFragment", "${it}")
            if (!isServiceRunningInForeground(AppBlockService::class.java)){
                (activity as MainActivity).startService()
            }

        }
    }

    override fun onSubmit(name: String, title: String, description: String) {
        onSendingMail(name, title, description)
    }

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.toolbar_title?.text = getString(R.string.settings)

        initRecyclerView()
        inviteLinkTv.setOnClickListener { onInvite() }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    private fun onInvite() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = PLAIN_TEXT
        //intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        // TODO("put playStore url in app_link")
        intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.app_link))
        startActivity(Intent.createChooser(intent, SHARE_FOCUS_LOCK))


    }

    private fun onSendingMail(name: String, title: String, description: String) {

        val intent = Intent(Intent.ACTION_SEND)
        intent.data = Uri.parse("mailto:")
        intent.type = PLAIN_TEXT
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(FEEDBACK_EMAIL))
        intent.putExtra(Intent.EXTRA_SUBJECT, title)

        intent.putExtra(Intent.EXTRA_TEXT, name + "\n" + description)

        startActivity(Intent.createChooser(intent, "Send Feedback..."))
    }

    private fun initRecyclerView() {
        val settingsList: ArrayList<SettingsModel> = ArrayList()
        settingsList.add(SettingsModel(R.drawable.ic_emergency, R.string.setting_title_emergency))
        settingsList.add(SettingsModel(R.drawable.ic_feedback, R.string.setting_title_feedback))
        settingsList.add(SettingsModel(R.drawable.ic_about, R.string.setting_title_about))

        settingsRv.adapter = SettingRecyclerViewAdapter(settingsList, this)

        resources.getDrawable(R.drawable.line_divider)?.let { DividerItemDecoration(it) }
            ?.let { settingsRv.addItemDecoration(it) }
    }

    override fun onItemClick(item: Int) {
        when (item) {
            R.string.setting_title_emergency -> CustomEmergencyDialog(this).show(
                activity?.supportFragmentManager,
                null
            )

            R.string.setting_title_feedback -> CustomFeedbackDialog(this).show(
                activity?.supportFragmentManager,
                null
            )

            R.string.setting_title_about -> CustomAboutDialog().show(
                activity?.supportFragmentManager,
                null
            )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun isServiceRunningInForeground( serviceClass: Class<*>): Boolean {
        val manager = Application.instance.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                if (service.foreground) {
                    return true
                }

            }
        }
        return false
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

}
