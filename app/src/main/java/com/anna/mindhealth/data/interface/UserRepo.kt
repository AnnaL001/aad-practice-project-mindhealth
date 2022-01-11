package com.anna.mindhealth.data.`interface`

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.DocumentReference

interface UserRepo {
    fun insert(email: String, securityLevel: Int)
    fun updateAssessmentStatus(status: Boolean)
    fun read(userId: String?): LiveData<DocumentReference>
}