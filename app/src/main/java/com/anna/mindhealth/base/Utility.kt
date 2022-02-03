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
import com.anna.mindhealth.data.repository.UserRepository
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.storage.ktx.storageMetadata
import java.io.ByteArrayOutputStream

object Utility {
    const val PATIENT_ROLE = 1
    const val THERAPIST_ROLE = 2
    const val MAX_JUST_THERE = 2000
    const val MAX_IN_BETWEEN = 10000
    const val CUSTOM_KEY = "File Type"
    const val CUSTOM_VALUE = "Resume"
    const val CUSTOM_VALUE_1 = "Profile image"

    val imageMetadata = storageMetadata {
        contentType = "image/jpeg"
        setCustomMetadata(CUSTOM_KEY, CUSTOM_VALUE_1)
    }

    val fileMetadata = storageMetadata {
        contentType = "application/pdf"
        setCustomMetadata(CUSTOM_KEY, CUSTOM_VALUE)
    }

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

    /* ======================================================
    *   Function to retrieve bytearray from user's avatar
    *   @param bitmap: Bitmap
    *   @return ByteArray
    * =======================================================  */
    fun getByteArray(bitmap: Bitmap): ByteArray{
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

}