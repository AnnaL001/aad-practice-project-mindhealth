package com.anna.mindhealth.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.anna.mindhealth.R
import com.anna.mindhealth.base.Utility.shortToastMessage
import com.anna.mindhealth.data.`interface`.UserRepo
import com.anna.mindhealth.data.model.Patient
import com.anna.mindhealth.data.model.Therapist
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserRepository(private val application: Application): UserRepo {
    /* ================================================
    *   Function to insert user data into Firestore
    *   @param email
    *   @param securityLevel
    * =================================================  */
    override fun insert(email: String, securityLevel: Int) {
        val authId = Firebase.auth.currentUser!!.uid
        val patient = Patient(id = authId , email = email, name = email.substring(0, email.indexOf("@")), security_level = securityLevel)
        val therapist = Therapist(id = authId, email = email, name = email.substring(0, email.indexOf("@")), security_level = securityLevel )

        when(securityLevel){
            1 -> {
                Log.d(TAG, "insertUser: Inserting patient data...")
                Firebase.firestore.collection(application.getString(R.string.dbcol_users))
                    .document(authId).set(patient).addOnCompleteListener {
                        AuthRepository(application).logOut()
                    }.addOnFailureListener {
                        shortToastMessage(
                            application.applicationContext,
                            application.getString(R.string.toast_sign_up_fail)
                        )
                    }
                Log.d(TAG, "insertUser: patient data inserted ...")
            }

            2 -> {
                Log.d(AuthRepository.TAG, "insertUser: Inserting therapist data ...")
                Firebase.firestore.collection(application.getString(R.string.dbcol_users))
                    .document(authId).set(therapist).addOnCompleteListener {
                        AuthRepository(application).logOut()
                    }.addOnFailureListener {
                        shortToastMessage(
                            application.applicationContext,
                            application.getString(R.string.toast_sign_up_fail)
                        )
                    }
                Log.d(TAG, "insertUser: Therapist data inserted ...")
            }
            else -> Log.e(TAG, "Error: Security Level Unidentified")
        }
    }


    /* =======================================================
    *   Function to update whether assessment has been done
    *   @param status
    * ========================================================  */
    override fun updateAssessmentStatus(status: Boolean){
        val patientId = Firebase.auth.currentUser!!.uid
        Firebase.firestore.collection(application.getString(R.string.dbcol_users)).document(patientId).
        update("is_assessment_done", status).
        addOnCompleteListener { task ->
            if (task.isSuccessful){
                Log.d(TAG, "Assessment status updated")
            } else {
                Log.e(TAG, "Error occurred while updating assessment status", task.exception)
            }
        }
    }

    /* ======================================================
    *   Function to fetch patient data
    *   @param patientId
    * =======================================================  */
    override fun read(userId: String?): DocumentReference? {
        val patientRef = userId?.let {
            Firebase.firestore.collection(application.getString(R.string.dbcol_users))
                .document(it)
        }
        return patientRef
    }


    companion object {
        val TAG = UserRepository::class.simpleName
    }
}