package com.trikh.focuslock.widget.customdialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import com.trikh.focuslock.R
import kotlinx.android.synthetic.main.custom_dialog_layout.*

class CustomDialog(
    @StringRes val titleText: Int,
    private val onPositiveButtonClick: () -> Unit,
    @StringRes val yesButtonText: Int = R.string.dialog_yes_button,
    @StringRes val noButtonText: Int = R.string.dialog_no_button

) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.custom_dialog_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog.setCanceledOnTouchOutside(false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        titleTv.setText(titleText)
        negativeBtn.setText(noButtonText)
        positiveBtn.setText(yesButtonText)

        negativeBtn.setOnClickListener { dismiss() }
        positiveBtn.setOnClickListener {
            onPositiveButtonClick()
            dismiss()
        }
    }
}