package com.anna.mindhealth.base

import android.content.Context
import android.net.Uri
import android.os.Message
import android.provider.OpenableColumns
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

object Utility {
    const val PATIENT_ROLE = 1
    const val THERAPIST_ROLE = 2

    fun shortToastMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun getFileName(context: Context, uri: Uri): String?{
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        val fileNameIndex = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor?.moveToFirst()
        val fileName = fileNameIndex?.let { cursor.getString(it) }
        cursor?.close()
        return fileName
    }
}