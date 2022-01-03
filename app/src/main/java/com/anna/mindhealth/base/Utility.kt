package com.anna.mindhealth.base

import android.content.Context
import android.os.Message
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

object Utility {
    fun shortToastMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}