package com.trikh.focuslock.widget.customFeedbackDialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.trikh.focuslock.R
import kotlinx.android.synthetic.main.custom_feedback_dialog_layout.*

class CustomFeedbackDialog(
    private val listener: DialogListener

) : DialogFragment() {

    private var userName: String? = null
    private var userTitle: String? = null
    private var userDescription: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.custom_feedback_dialog_layout, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog.setCanceledOnTouchOutside(false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))



        cancelBtn.setOnClickListener { dismiss() }
        submitBtn.setOnClickListener {
            userName = nameEt.text.toString()
            userTitle = titleEt.text.toString()
            userDescription = descriptionEt.text.toString()
            var cancel = true
            //var focusView: View? = null

            if (userTitle!!.isNotEmpty() || userDescription!!.isNotEmpty()) {
                cancel = false
            }
            if (!cancel) {
                listener.onSubmit(userName!!, userTitle!!, userDescription!!)
                dismiss()
            } else {
                //focusView?.requestFocus()
                Toast.makeText(context, "Please enter either title or description.", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)

    }

    interface DialogListener {
        fun onSubmit(name: String, title: String, description: String)
    }


}