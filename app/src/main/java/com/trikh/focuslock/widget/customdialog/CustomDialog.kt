package com.trikh.focuslock.widget.customdialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.trikh.focuslock.R

class CustomDialog(private val title : String): DialogFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.custom_dialog_layout,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleTextView = view.findViewById<TextView>(R.id.title_textview)
        titleTextView.setText(title)
        val buttonNo = view.findViewById<TextView>(R.id.no_button)
        val buttonYes =view.findViewById<TextView>(R.id.yes_button)

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