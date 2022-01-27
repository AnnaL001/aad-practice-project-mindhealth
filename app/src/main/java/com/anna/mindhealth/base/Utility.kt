package com.anna.mindhealth.base

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Message
import android.provider.OpenableColumns
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView

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

    fun setTextViewValues(textView: MaterialTextView, textValue: String){
        textView.text = textValue
    }

    fun setEditTextValues(editText: EditText, textValue: String){
        editText.setText(textValue)
    }

    fun setImageViewResource(shapeableImageView: ShapeableImageView, bitmap: Bitmap){
        shapeableImageView.setImageBitmap(bitmap)
    }

    fun setImageViewResource(shapeableImageView: ShapeableImageView, resId: Int){
        shapeableImageView.setImageResource(resId)
    }

    fun setImageViewResource(imageView: ImageView, resId: Int){
        imageView.setImageResource(resId)
    }
}