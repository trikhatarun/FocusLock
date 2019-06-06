package com.trikh.focuslock.ui.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.trikh.focuslock.R
import com.trikh.focuslock.utils.Constants.Companion.FEEDBACK_EMAIL
import com.trikh.focuslock.utils.Constants.Companion.PLAIN_TEXT
import com.trikh.focuslock.utils.Constants.Companion.SHARE_FOCUS_LOCK
import com.trikh.focuslock.widget.customAboutDialog.CustomAboutDialog
import com.trikh.focuslock.widget.customEmergencyDialog.CustomEmergencyDialog
import com.trikh.focuslock.widget.customFeedbackDialog.CustomFeedbackDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.toolbar.*


class SettingsFragment : Fragment(), SettingRecyclerViewAdapter.AdapterInteractionListener,
    CustomEmergencyDialog.DialogListener, CustomFeedbackDialog.DialogListener {


    override fun onBlock() {
        //TODO perform task when user don't want to disable all schedules
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
        activity?.fabMenu?.visibility = View.GONE
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
        val settingList: ArrayList<SettingModel> = ArrayList()
        settingList.add(SettingModel(R.drawable.ic_emergency, R.string.setting_title_emergency))
        settingList.add(SettingModel(R.drawable.ic_feedback, R.string.setting_title_feedback))
        settingList.add(SettingModel(R.drawable.ic_about, R.string.setting_title_about))

        settingsRv.adapter = SettingRecyclerViewAdapter(settingList, this)

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

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

}
