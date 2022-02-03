package com.anna.mindhealth.ui.patient.assessment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.anna.mindhealth.data.`interface`.AssessmentRepo
import com.anna.mindhealth.data.model.Assessment
import com.anna.mindhealth.data.repository.AssessmentRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.ktx.Firebase

class AssessmentViewModel(application: Application): AndroidViewModel(application) {
    private val assessmentRepository: AssessmentRepo
    val assessmentRef: DocumentReference

    init {
        assessmentRepository = AssessmentRepository(application)
        assessmentRef = assessmentRepository.read(Firebase.auth.currentUser!!.uid)
    }

    fun insertAssessmentResponses(assessment: Assessment){
        assessmentRepository.insert(assessment)
    }

}