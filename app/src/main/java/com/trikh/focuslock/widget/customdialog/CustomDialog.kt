package com.trikh.focuslock.widget.customdialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import com.trikh.focuslock.R
import kotlinx.android.synthetic.main.custom_dialog_layout.view.*

class CustomDialog(
    @StringRes val titleText: Int = R.string.dialog_message,
    @StringRes val noButtonText: Int = R.string.dialog_no_button,
    @StringRes val yesButtonText: Int = R.string.dialog_yes_button
) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.custom_dialog_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog.setCanceledOnTouchOutside(false)
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        val colorDrawable = ColorDrawable(Color.TRANSPARENT)
        dialog?.window?.setBackgroundDrawable(colorDrawable)

        view.titleTextView.setText(titleText)
        view.noButton.setText(noButtonText)
        view.yesButton.setText(yesButtonText)

        view.noButton.setOnClickListener {
            dismiss()
        }

        view.yesButton.setOnClickListener {
            val dialogListener = activity as DialogListener?
            dialogListener!!.onSelectYes()
            dismiss()
        }
    }

    interface DialogListener {
        fun onSelectYes()
    }
}