package com.anna.mindhealth.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.anna.mindhealth.R
import com.anna.mindhealth.data.`interface`.OnClickAvailabilityDialogListener
import com.anna.mindhealth.data.model.Therapist
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UpdateAvailabilityDialog: DialogFragment() {
    private lateinit var onClickListener: OnClickAvailabilityDialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.btn_update_availability_text)
            .setMessage(R.string.prompt_update_availability)
            .setNeutralButton(R.string.cancel){ dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.yes){ _, _ ->
                onClickListener.onDialogPositiveClick(Firebase.auth.currentUser!!.uid)
            }
            .setNegativeButton(R.string.no){ _, _ ->
                onClickListener.onDialogNegativeClick(Firebase.auth.currentUser!!.uid)
            }.show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onClickListener = parentFragment as OnClickAvailabilityDialogListener
        } catch (e: ClassCastException){
            Log.e(TAG, "onAttach: ClassCastException", e.cause)
        }
    }

    companion object{
        val TAG = UpdateAvailabilityDialog::class.simpleName
    }
}