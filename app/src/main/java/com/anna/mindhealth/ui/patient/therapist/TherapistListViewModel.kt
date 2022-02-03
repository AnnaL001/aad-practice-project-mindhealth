package com.anna.mindhealth.ui.patient.therapist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.anna.mindhealth.data.`interface`.AssessmentRepo
import com.anna.mindhealth.data.`interface`.TherapistRepo
import com.anna.mindhealth.data.model.Assessment
import com.anna.mindhealth.data.repository.AssessmentRepository
import com.anna.mindhealth.data.repository.TherapistRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase

class TherapistListViewModel(application: Application): AndroidViewModel(application) {
    val assessmentReference: DocumentReference

    private val therapistRepository: TherapistRepo = TherapistRepository(application)
    private val assessmentRepository: AssessmentRepo = AssessmentRepository(application)

    init {
        assessmentReference = assessmentRepository.read(Firebase.auth.currentUser!!.uid)
    }

    fun getTherapistList(assessment: Assessment): Query?{
        return therapistRepository.retrieveCuratedTherapistList(assessment)
    }
}