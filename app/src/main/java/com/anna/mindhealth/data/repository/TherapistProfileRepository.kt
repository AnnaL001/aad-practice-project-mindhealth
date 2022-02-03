package com.anna.mindhealth.data.repository

import android.app.Application
import android.util.Log
import com.anna.mindhealth.R
import com.anna.mindhealth.base.Utility.shortToastMessage
import com.anna.mindhealth.data.`interface`.TherapistProfileRepo
import com.anna.mindhealth.data.model.TherapistProfile
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TherapistProfileRepository(private val application: Application): TherapistProfileRepo {

    /* =======================================================
    *   Function to update therapist profile data into Firestore
    *   @param data: TherapistProfile
    * ========================================================  */
    override fun update(therapistProfile: TherapistProfile) {
        val therapistId = Firebase.auth.currentUser!!.uid
        Firebase.firestore.collection(application.getString(R.string.dbcol_therapists)).document(therapistId)
            .update("profile", therapistProfile).addOnCompleteListener { task ->
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

    companion object{
        val TAG = TherapistProfileRepository::class.simpleName
    }
}