package com.anna.mindhealth.data.repository

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.anna.mindhealth.base.Utility.PATIENT_ROLE
import com.anna.mindhealth.base.Utility.THERAPIST_ROLE
import com.anna.mindhealth.data.`interface`.UserRepo
import com.anna.mindhealth.data.model.Patient
import com.anna.mindhealth.data.model.Therapist
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.ktx.Firebase

class UserRepository(private val application: Application): UserRepo {
    /* ================================================
    *   Function to insert user data into Firestore
    *   @param email: String
    *   @param securityLevel: Int
    *   @param resumeUri: Uri
    * =================================================  */
    override fun insert(email: String, securityLevel: Int, resumeUri: Uri?) {
        val authId = Firebase.auth.currentUser!!.uid

        when(securityLevel){
            PATIENT_ROLE -> {
                val patient = Patient(id = authId , email = email, name = email.substring(0, email.indexOf("@")), security_level = securityLevel)
                PatientRepository(application).insert(patient)
            }

            THERAPIST_ROLE -> {
                TherapistRepository(application).insert(resumeUri, authId, email, securityLevel)
            }
            else -> Log.e(TAG, "Security level is unknown or not set")
        }
    }


    /* ======================================================
    *   Function to fetch user data
    *   @param userId: String
    * =======================================================  */
    override fun read(userId: String?, securityLevel: Int): DocumentReference? {
        var userRef: DocumentReference?= null

        when(securityLevel){
            PATIENT_ROLE -> { userRef = PatientRepository(application).read(userId) }
            THERAPIST_ROLE -> { userRef = TherapistRepository(application).read(userId)}
            else -> Log.e(TAG, "Security level is unknown or not set")
        }

        return userRef
    }

    /* ======================================================
    *   Function to update user's personal details
    *   @param data: Any
    *   @param securityLevel: Int
    * =======================================================  */
    override fun update(data: Any, securityLevel: Int, bitmap: Bitmap) {
        when(securityLevel){
            PATIENT_ROLE -> PatientRepository(application).update(data as Patient, bitmap)
            THERAPIST_ROLE -> TherapistRepository(application).update(data as Therapist, bitmap)
            else -> Log.d(TAG, "Security level is unknown or not set")
        }
    }

    companion object {
        val TAG = UserRepository::class.simpleName
    }
}