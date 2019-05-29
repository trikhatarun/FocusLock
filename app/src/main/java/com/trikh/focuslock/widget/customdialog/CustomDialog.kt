package com.trikh.focuslock.widget.customdialog

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.trikh.focuslock.R

class CustomDialog(private val title : String, private val noButtonText : String, private val yesButtonText : String ): DialogFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.custom_dialog_layout,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.setCancelable(false)
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        var colorDrawable = ColorDrawable(Color.TRANSPARENT)
        dialog?.window?.setBackgroundDrawable(colorDrawable)
        val titleTextView = view.findViewById<TextView>(R.id.title_textview)
        titleTextView.setText(title)
        val buttonNo = view.findViewById<TextView>(R.id.no_button)
        buttonNo.setText(noButtonText)
        val buttonYes =view.findViewById<TextView>(R.id.yes_button)
        buttonYes.setText(yesButtonText)

        buttonNo.setOnClickListener {
            val dialogListener = activity as DialogListener?
            dialogListener!!.onSelectNo()
            dismiss()
        }

        buttonYes.setOnClickListener {
            val dialogListener = activity as DialogListener?
            dialogListener!!.onSelectYes()
            dismiss()
        }
    }

    interface DialogListener{
        fun onSelectYes()
        fun onSelectNo()
    }
}