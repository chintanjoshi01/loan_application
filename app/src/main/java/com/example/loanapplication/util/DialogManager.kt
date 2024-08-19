package com.example.loanapplication.util

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.EditText
import com.example.loanapplication.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import javax.inject.Inject

class DialogManager @Inject constructor(private val context: Context) {

    fun showInfoDialog(
        title: String = "Message",
        message: String,
        positiveButtonText: String = "OK",
        setCancelable: Boolean = false,
        onPositiveClick: (() -> Unit)? = null,
        context: Context
    ) {

        MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_App_MaterialAlertDialog).setTitle(
            title
        ).setMessage(message).setPositiveButton(positiveButtonText) { _, _ ->
            onPositiveClick?.invoke()
        }.setCancelable(setCancelable).show()
    }




    fun showCustomDialog(
        title: String = "Message",
        message: String,
        positiveButtonText: String = "OK",
        negativeButtonText: String = "Cancel",
        setCancelable: Boolean = false,
        onPositiveClick: ((String, String) -> Unit)? = null,
        context: Context,
        layout: Int,
        inflater: LayoutInflater
    ) {
        val customView = inflater.inflate(layout, null)
        val amount = customView.findViewById<EditText>(R.id.editText1)
        val duration = customView.findViewById<EditText>(R.id.editText2)
        val amountInputLayout =
            customView.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.inputLayout1)
        val durationInputLayout =
            customView.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.inputLayout2)

        val dialog =
            MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_App_MaterialAlertDialog)
                .setTitle(title)
                .setView(customView)
                .setMessage(message)
                .setPositiveButton(positiveButtonText, null)
                .setNegativeButton(negativeButtonText) { dialog, _ ->
                    dialog.dismiss()
                }
                .setCancelable(setCancelable)
                .create()

        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                var isValid = true
                if (amount.text.isNullOrEmpty()) {
                    amountInputLayout.error = "Please enter amount"
                    isValid = false
                } else {
                    amountInputLayout.error = null
                }
                if (duration.text.isNullOrEmpty()) {
                    durationInputLayout.error = "Please enter duration"
                    isValid = false
                } else {
                    durationInputLayout.error = null
                }
                if (isValid) {
                    onPositiveClick?.invoke(amount.text.toString(), duration.text.toString())
                    dialog.dismiss()
                }
            }
        }
        dialog.show()
    }


}
