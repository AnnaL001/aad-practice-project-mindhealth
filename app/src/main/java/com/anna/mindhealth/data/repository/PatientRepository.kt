package com.anna.mindhealth.data.repository

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import com.anna.mindhealth.R
import com.anna.mindhealth.base.Utility
import com.anna.mindhealth.base.Utility.getByteArray
import com.anna.mindhealth.base.Utility.imageMetadata
import com.anna.mindhealth.data.`interface`.PatientRepo
import com.anna.mindhealth.data.model.Patient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class PatientRepository(private val application: Application): PatientRepo {
    /* ================================================
    *   Function to insert patient data into Firestore
    *   @param patient: Patient
    * =================================================  */
    fun insert(patient: Patient){
        Log.d(UserRepository.TAG, "insertUser: Inserting ...")

        Firebase.firestore.collection(application.getString(R.string.dbcol_patients))
            .document(patient.id).set(patient).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(UserRepository.TAG, "insertUser: Success ...")
                    AuthRepository(application).logOut()
                } else {
                    Utility.shortToastMessage(
                        application.applicationContext,
                        application.getString(R.string.toast_sign_up_fail)
                    )
                }
            }
    }

    /* ======================================================
    *   Function to update patient's personal details
    *   @param patient: Patient
    * =======================================================  */
    fun update(patient: Patient, bitmap: Bitmap){
        Log.d(AuthRepository.TAG, "Updating personal info...")

        val uploadReference =
            Firebase.storage.reference.child("${application.getString(R.string.dbcol_patients)}/${patient.id}/${application.getString(R.string.storage_images)}/avatar")

        uploadPatientAvatar(uploadReference, bitmap, patient)
    }

    /* ======================================================
    *   Function to update patient's avatar
    *   @param storageReference: StorageReference
    *   @param bitmap: Bitmap
    *   @param patient: Patient
    * =======================================================  */
    private fun uploadPatientAvatar(storageReference: StorageReference, bitmap: Bitmap, patient: Patient){
        storageReference.putBytes(getByteArray(bitmap), imageMetadata)
            .addOnProgressListener { task ->
                val progress = (100.0 * task.bytesTransferred) / task.totalByteCount
                Log.d(UserRepository.TAG, "Upload progress: $progress%")
                Utility.shortToastMessage(
                    application.applicationContext,
                    application.getString(R.string.toast_upload_progress, progress.toInt())
                )
            }.addOnPausedListener {
                Log.d(UserRepository.TAG, "Upload paused")
                Utility.shortToastMessage(
                    application.applicationContext,
                    application.getString(R.string.toast_upload_paused)
                )
            }.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }

                storageReference.downloadUrl
            }.addOnFailureListener { e ->
                Log.e(UserRepository.TAG, "Error: Failed to upload avatar", e.cause)
                Utility.shortToastMessage(
                    application.applicationContext,
                    application.getString(R.string.toast_upload_fail)
                )
            }.addOnCompleteListener { task ->
                Log.d(UserRepository.TAG, "Upload success")
                Utility.shortToastMessage(
                    application.applicationContext,
                    application.getString(R.string.toast_upload_success)
                )

                // Update avatar file path alongside other details
                Firebase.firestore.collection(application.getString(R.string.dbcol_patients))
                    .document(patient.id).update(
                        mapOf(
                            "avatar" to task.result.toString(),
                            "name" to patient.name,
                            "phone_no" to patient.phone_no
                        )
                    )
            }
    }


    /* =======================================================
    *   Function to update whether assessment has been done
    *   @param status: Boolean
    * ========================================================  */
    override fun updateAssessmentStatus(status: Boolean){
        val patientId = Firebase.auth.currentUser!!.uid
        Firebase.firestore.collection(application.getString(R.string.dbcol_patients)).document(patientId).
        update("is_assessment_done", status).
        addOnCompleteListener { task ->
            if (task.isSuccessful){
                Log.d(UserRepository.TAG, "Assessment status updated")
            } else {
                Log.e(UserRepository.TAG, "Error occurred while updating assessment status", task.exception)
            }
        }
    }

    /* ======================================================
    *   Function to fetch patient data
    *   @param userId: String
    * =======================================================  */
    fun read(patientId: String?): DocumentReference? {
        val patientRef = patientId?.let {
            Firebase.firestore.collection(application.getString(R.string.dbcol_patients))
                .document(it)
        }
        return patientRef
    }

}