package com.anna.mindhealth.ui.assessment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.anna.mindhealth.data.`interface`.CrudRepo
import com.anna.mindhealth.data.model.Assessment
import com.anna.mindhealth.data.repository.AssessmentRepository

class AssessmentViewModel(application: Application): AndroidViewModel(application) {
    private val assessmentRepository: CrudRepo

    init {
        assessmentRepository = AssessmentRepository(application)
    }

    fun insertAssessmentResponses(assessment: Assessment){
        assessmentRepository.insert(assessment)
    }
}