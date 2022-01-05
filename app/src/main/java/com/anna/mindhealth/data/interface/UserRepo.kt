package com.anna.mindhealth.data.`interface`

import androidx.lifecycle.LiveData
import com.anna.mindhealth.data.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot

interface UserRepo {
    fun insert(email: String, securityLevel: Int)
    fun updateAssessmentStatus(status: Boolean)
    fun readPatient(userId: String): LiveData<DocumentReference>
}