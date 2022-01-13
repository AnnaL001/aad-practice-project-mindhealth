package com.anna.mindhealth.data.repository

import android.app.Application
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.anna.mindhealth.R
import com.anna.mindhealth.base.Utility.getFileName
import com.anna.mindhealth.base.Utility.shortToastMessage
import com.anna.mindhealth.data.`interface`.UserRepo
import com.anna.mindhealth.data.model.Patient
import com.anna.mindhealth.data.model.Therapist
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import java.io.File
import java.io.FileInputStream

class UserRepository(private val application: Application): UserRepo {
    /* ================================================
    *   Function to insert user data into Firestore
    *   @param email
    *   @param securityLevel
    * =================================================  */
    override fun insert(email: String, securityLevel: Int, resumeUri: Uri?) {
        val authId = Firebase.auth.currentUser!!.uid

        when(securityLevel){
            1 -> {
                val patient = Patient(id = authId , email = email, name = email.substring(0, email.indexOf("@")), security_level = securityLevel)
                insertPatient(patient)
            }

            2 -> {
                insertTherapist(resumeUri, authId, email, securityLevel)

            }
            else -> Log.e(TAG, "Error: Security Level Unidentified")
        }
    }

    private fun insertTherapist(resumeUri: Uri?, authId: String, email: String, securityLevel: Int
    ) {
        Log.d(AuthRepository.TAG, "insertUser: Inserting ...")

        val fileMetadata = storageMetadata {
            contentType = "application/pdf"
        }

        val uploadReference =
            Firebase.storage.reference.child("$authId/resume/${getFileName(application.applicationContext, resumeUri!!)}")

        uploadReference.putFile(resumeUri, fileMetadata).addOnProgressListener { task ->
            val progress = (100 * task.bytesTransferred) / task.totalByteCount
            Log.d(TAG, "Upload progress: $progress%")
            shortToastMessage(
                application.applicationContext,
                application.getString(R.string.toast_upload_progress, progress)
            )
        }.addOnPausedListener {
            Log.d(TAG, "Upload paused")
            shortToastMessage(
                application.applicationContext,
                application.getString(R.string.toast_upload_paused)
            )
        }.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }

            uploadReference.downloadUrl
        }.addOnFailureListener { e ->
            Log.e(TAG, "Error: Failed to upload file", e.cause)
            shortToastMessage(
                application.applicationContext,
                application.getString(R.string.toast_upload_fail)
            )
        }.addOnCompleteListener { task ->
            Log.d(TAG, "Upload success")
            shortToastMessage(
                application.applicationContext,
                application.getString(R.string.toast_upload_success)
            )

            val therapist = Therapist(id = authId, email = email, name = email.substring(0, email.indexOf("@")), security_level = securityLevel, resume = task.result.toString())

            Firebase.firestore.collection(application.getString(R.string.dbcol_users))
                .document(authId).set(therapist).addOnCompleteListener { insertTask ->
                    if (!insertTask.isSuccessful) {
                        Log.e(TAG, "insertUser: Failure", insertTask.exception)
                    } else {
                        Log.d(TAG, "insertUser: Success ...")
                    }
                }
        }
    }

    private fun insertPatient(patient: Patient) {
        Log.d(TAG, "insertUser: Inserting ...")

        Firebase.firestore.collection(application.getString(R.string.dbcol_users))
            .document(patient.id).set(patient).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "insertUser: Success ...")
                    AuthRepository(application).logOut()
                } else {
                    shortToastMessage(
                        application.applicationContext,
                        application.getString(R.string.toast_sign_up_fail)
                    )
                }
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