package com.anna.mindhealth.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.anna.mindhealth.R
import com.anna.mindhealth.base.Utility
import com.anna.mindhealth.base.Utility.shortToastMessage
import com.anna.mindhealth.data.`interface`.CrudRepo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TherapistProfileRepository(private val application: Application): CrudRepo {

    /* =======================================================
    *   Function to insert therapist profile data into Firestore
    *   @param data: TherapistProfile
    * ========================================================  */
    override fun insert(data: Any) {
        val therapistId = Firebase.auth.currentUser!!.uid
        Firebase.firestore.collection(application.getString(R.string.dbcol_users)).document(therapistId)
            .collection(application.getString(R.string.dbcol_therapist_profile))
            .document(application.getString(R.string.doc_therapist_profile)).set(data)
            .addOnCompleteListener { task ->
                Log.d(TAG, "Saving profile data...")
                if (task.isSuccessful){
                    Log.d(TAG, "Profile data saved")
                    shortToastMessage(
                        application.applicationContext,
                        application.getString(R.string.toast_therapist_profile_data_save_success)
                    )
                } else {
                    Log.d(TAG, "Error: Profile dat not saved", task.exception)
                    shortToastMessage(
                        application.applicationContext,
                        application.getString(R.string.toast_therapist_profile_data_save_fail)
                    )
                }
            }
    }

    /* =======================================================
    *   Function to fetch therapist profile data from Firestore
    *   @param id: String
    * ========================================================  */
    override fun read(id: String): DocumentReference {
        val therapistProfileRef = id.let {
            Firebase.firestore.collection(application.getString(R.string.dbcol_users))
                .document(it).collection(application.getString(R.string.dbcol_therapist_profile))
                .document(application.getString(R.string.doc_therapist_profile))
        }
        return therapistProfileRef
    }

    companion object{
        val TAG = TherapistProfileRepository::class.simpleName
    }
}