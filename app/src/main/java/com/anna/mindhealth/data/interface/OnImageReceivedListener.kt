package com.anna.mindhealth.data.`interface`

import android.graphics.Bitmap
import android.net.Uri

interface OnImageReceivedListener {
    fun getImageUri(uri: Uri)
    fun getImageBitmap(bitmap: Bitmap)
}