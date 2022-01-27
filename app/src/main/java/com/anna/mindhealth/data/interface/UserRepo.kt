package com.anna.mindhealth.data.`interface`

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import com.anna.mindhealth.data.model.Therapist
import com.google.firebase.firestore.DocumentReference

interface UserRepo {
    fun insert(email: String, securityLevel: Int,resumeUri: Uri?)
    fun updateAssessmentStatus(status: Boolean)
    fun read(userId: String?): DocumentReference?
    fun update(data: Any, securityLevel: Int, bitmap: Bitmap)
    fun updateTherapistAvailability(therapist: Therapist)
}