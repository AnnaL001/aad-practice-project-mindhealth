package com.anna.mindhealth.data.repository

import android.app.Application
import android.util.Log
import com.anna.mindhealth.R
import com.anna.mindhealth.base.Utility.shortToastMessage
import com.anna.mindhealth.data.`interface`.AssessmentRepo
import com.anna.mindhealth.data.model.Assessment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AssessmentRepository(private val application: Application): AssessmentRepo {
    /* =======================================================
    *   Function to insert assessment responses into Firestore
    *   @param data: Assessment
    * ========================================================  */
    override fun insert(assessment: Assessment) {
        val userId = Firebase.auth.currentUser!!.uid
        Firebase.firestore.collection(application.getString(R.string.dbcol_patients))
            .document(userId).collection(application.getString(R.string.dbcol_assessment))
            .document(application.getString(R.string.doc_initial_assessment)).set(assessment).addOnCompleteListener { task ->
                Log.d(TAG, "Saving assessment answers...")
                if (task.isSuccessful){
                    Log.d(TAG, "Assessment answers saved")
                    shortToastMessage(application.applicationContext,
                    application.getString(R.string.toast_assessment_choices_save_success))
                    PatientRepository(application).updateAssessmentStatus(true)
                } else {
                    Log.d(TAG, "Error: Assessment answers not saved", task.exception)
                    shortToastMessage(application.applicationContext,
                        application.getString(R.string.toast_assessment_choices_save_fail))
                }
            }
    }

    /* =======================================================
    *   Function to fetch assessment data from Firestore
    *   @param id: String
    * ========================================================  */
    override fun read(id: String): DocumentReference =
        Firebase.firestore.collection(application.getString(R.string.dbcol_patients))
            .document(id).collection(application.getString(R.string.dbcol_assessment))
            .document(application.getString(R.string.doc_initial_assessment))


    companion object{
        val TAG = AssessmentRepository::class.simpleName
    }
}