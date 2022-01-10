package com.anna.mindhealth.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.anna.mindhealth.R
import com.anna.mindhealth.base.Utility.shortToastMessage
import com.anna.mindhealth.data.`interface`.CrudRepo
import com.anna.mindhealth.data.`interface`.UserRepo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AssessmentRepository(private val application: Application): CrudRepo {
    private val userRepository: UserRepo = UserRepository(application)
    private var _assessmentRef: MutableLiveData<DocumentReference> = MutableLiveData()

    private val assessmentRef: LiveData<DocumentReference> get() = _assessmentRef

    /* =======================================================
    *   Function to insert assessment responses into Firestore
    *   @param data -- instance of Assessment data class
    * ========================================================  */
    override fun insert(data: Any) {
        val userId = Firebase.auth.currentUser!!.uid
        Firebase.firestore.collection(application.getString(R.string.dbcol_patients))
            .document(userId).collection(application.getString(R.string.dbcol_assessment))
            .document(application.getString(R.string.doc_initial_assessment)).set(data).addOnCompleteListener { task ->
                Log.d(TAG, "Saving assessment answers...")
                if (task.isSuccessful){
                    Log.d(TAG, "Assessment answers saved")
                    shortToastMessage(application.applicationContext,
                    application.getString(R.string.toast_assessment_choices_save_success))
                    userRepository.updateAssessmentStatus(true)
                } else {
                    Log.d(TAG, "Error: Assessment answers not saved", task.exception)
                    shortToastMessage(application.applicationContext,
                        application.getString(R.string.toast_assessment_choices_save_fail))
                }
            }
    }

    override fun read(id: String): LiveData<DocumentReference> {
        _assessmentRef.postValue(Firebase.firestore.collection(application.getString(R.string.dbcol_patients)).document(id)
            .collection(application.getString(R.string.dbcol_assessment))
            .document(application.getString(R.string.doc_initial_assessment)))
        return assessmentRef
    }

    companion object{
        val TAG = AssessmentRepository::class.simpleName
    }
}