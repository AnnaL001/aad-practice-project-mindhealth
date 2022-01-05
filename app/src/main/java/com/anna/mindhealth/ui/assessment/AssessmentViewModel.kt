package com.anna.mindhealth.ui.assessment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.anna.mindhealth.data.`interface`.CrudRepo
import com.anna.mindhealth.data.model.Assessment
import com.anna.mindhealth.data.repository.AssessmentRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AssessmentViewModel(application: Application): AndroidViewModel(application) {
    private val assessmentRepository: CrudRepo

    init {
        assessmentRepository = AssessmentRepository(application)
    }

    fun insertAssessmentResponses(assessment: Assessment){
        assessmentRepository.insert(assessment)
    }

    val assessmentResponses = assessmentRepository.read(Firebase.auth.currentUser!!.uid)
}