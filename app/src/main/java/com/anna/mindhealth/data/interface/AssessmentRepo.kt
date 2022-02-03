package com.anna.mindhealth.data.`interface`

import androidx.lifecycle.LiveData
import com.anna.mindhealth.data.model.Assessment
import com.google.firebase.firestore.DocumentReference

interface AssessmentRepo {
    fun insert(assessment: Assessment)
    fun read(id: String): DocumentReference
}