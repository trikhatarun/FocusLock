package com.trikh.focuslock.ui.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.appbar.AppBarLayout

import com.trikh.focuslock.R
import com.trikh.focuslock.widget.customAboutDialog.CustomAboutDialog
import com.trikh.focuslock.widget.customEmergencyDialog.CustomEmergencyDialog
import com.trikh.focuslock.widget.customFeedbackDialog.CustomFeedbackDialog
import com.trikh.focuslock.widget.customdialog.CustomDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.toolbar.view.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SettingsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SettingsFragment : Fragment() {
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
        activity?.inviteLinkTv?.visibility = View.VISIBLE
        activity?.mainTv2?.visibility = View.VISIBLE

        activity?.findViewById<AppBarLayout>(R.id.appbar)?.toolbar_title?.text =
            context?.resources?.getString(R.string.settings)
//        view.toolbar_title.text = "Settings"
        initRecyclerView()
        inviteLinkTv.setOnClickListener { onInvite() }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    private fun onInvite() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        //intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_SUBJECT, "Focus Lock")
        // TODO("put playStore url in app_link")
        intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.app_link))

        startActivity(Intent.createChooser(intent, "Share Focus Lock"))


    }

    private fun onSendingMail(name: String, title: String, description: String) {

        val intent = Intent(Intent.ACTION_SEND)
        intent.data = Uri.parse("mailto:")
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("trikha.tarun01@gmail.com"))
        intent.putExtra(Intent.EXTRA_SUBJECT, title)

        intent.putExtra(Intent.EXTRA_TEXT, name + "\n" + description)

        startActivity(Intent.createChooser(intent, "Send Feedback..."))
    }

    private fun initRecyclerView() {
        val settingList: ArrayList<SettingModel> = ArrayList()
        settingList.add(
            SettingModel(
                R.drawable.ic_emergency,
                R.string.setting_title_emergency,
                R.drawable.ic_info
            )
        )
        settingList.add(
            SettingModel(
                R.drawable.ic_feedback,
                R.string.setting_title_feedback,
                R.drawable.ic_info
            )
        )
        settingList.add(
            SettingModel(
                R.drawable.ic_about,
                R.string.setting_title_about,
                R.drawable.ic_info
            )
        )
        settingsRv.adapter =
            SettingRecyclerViewAdapter(settingList, onItemClicked = { adapterPosition ->


                when (adapterPosition) {
                    0 -> {
                        CustomEmergencyDialog(onBlock = {
                            if (it) {
                                //TODO perform task when user don't want to disable all schedules
                                //Toast.makeText(context,"block : yes",Toast.LENGTH_LONG).show()
                            } else {
                                //TODO perform task when user wants to disable all schedules
                                //Toast.makeText(context,"block : no",Toast.LENGTH_LONG).show()
                            }
                        }).show(activity?.supportFragmentManager, "")

                    }

                    1 -> {
                        CustomFeedbackDialog(onSubmit = { name, title, description ->
                            onSendingMail(name, title, description)
                            /*Toast.makeText(
                                context,
                                "$name $title $description", Toast.LENGTH_SHORT
                            ).show()*/
                        }).show(activity?.supportFragmentManager, "")
                    }

                    2 -> {

                        CustomAboutDialog().show(activity?.supportFragmentManager, "")

                    }
                }


            })
        context?.resources?.getDrawable(R.drawable.line_divider)?.let {
            DividerItemDecoration(
                it
            )
        }?.let { settingsRv.addItemDecoration(it) }


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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

}
