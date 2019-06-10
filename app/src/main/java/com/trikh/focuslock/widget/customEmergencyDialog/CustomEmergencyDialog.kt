package com.trikh.focuslock.widget.customEmergencyDialog


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.trikh.focuslock.BuildConfig
import com.trikh.focuslock.R
import kotlinx.android.synthetic.main.custom_about_dialog_layout.*
import kotlinx.android.synthetic.main.custom_emergency_dialog_layout.*


class CustomEmergencyDialog(private val listener: DialogListener) : DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.custom_emergency_dialog_layout, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog.setCanceledOnTouchOutside(false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        noBtn.setOnClickListener {
            dismiss()
        }

        yesBtn.setOnClickListener {
            listener.onBlock()
            dismiss()
        }

    }

    override fun onResume() {
        super.onResume()

        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)

    }


    interface DialogListener {
        fun onBlock()
    }


}