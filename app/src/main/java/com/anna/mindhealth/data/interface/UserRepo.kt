package com.anna.mindhealth.data.`interface`


import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.firestore.DocumentReference

interface UserRepo {
    fun insert(email: String, securityLevel: Int,resumeUri: Uri?)
    fun read(userId: String?, securityLevel: Int): DocumentReference?
    fun update(data: Any, securityLevel: Int, bitmap: Bitmap)
}