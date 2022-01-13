package com.anna.mindhealth.data.`interface`

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.DocumentReference

interface UserRepo {
    fun insert(email: String, securityLevel: Int,resumeUri: Uri?)
    fun updateAssessmentStatus(status: Boolean)
    fun read(userId: String?): DocumentReference?
}