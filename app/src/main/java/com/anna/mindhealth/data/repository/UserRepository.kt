package com.anna.mindhealth.data.repository

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
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
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import java.io.ByteArrayOutputStream

class UserRepository(private val application: Application): UserRepo {
    private val fileMetadata = storageMetadata {
        contentType = "image/jpeg"
        setCustomMetadata(CUSTOM_KEY, CUSTOM_VALUE_1)
    }
    /* ================================================
    *   Function to insert user data into Firestore
    *   @param email: String
    *   @param securityLevel: Int
    *   @param resumeUri: Uri
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

    /* ================================================
    *   Function to insert therapist data into Firestore
    *   @param email: String
    *   @param securityLevel: Int
    *   @param resumeUri: Uri
    *   @param authId: String
    * =================================================  */
    private fun insertTherapist(resumeUri: Uri?, authId: String, email: String, securityLevel: Int
    ) {
        Log.d(AuthRepository.TAG, "insertUser: Inserting ...")

        val uploadReference =
            Firebase.storage.reference.child("therapists/$authId/resume/${getFileName(application.applicationContext, resumeUri!!)}")

        uploadResume(uploadReference, authId, email, securityLevel, resumeUri)
    }

    /* ================================================
    *   Function to insert patient data into Firestore
    *   @param patient: Patient
    * =================================================  */
    private fun insertPatient(patient: Patient){
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
    *   @param status: Boolean
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
    *   Function to fetch user data
    *   @param userId: String
    * =======================================================  */
    override fun read(userId: String?): DocumentReference? {
        val userRef = userId?.let {
            Firebase.firestore.collection(application.getString(R.string.dbcol_users))
                .document(it)
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
            1 -> updatePatientPersonalInfo(data as Patient, bitmap)
            2 -> updateTherapistPersonalInfo(data as Therapist, bitmap)
            else -> Log.d(TAG, "Unknown security level")
        }
    }

    /* ======================================================
    *   Function to update therapist's personal details
    *   @param data: Any
    * =======================================================  */
    private fun updateTherapistPersonalInfo(therapist: Therapist, bitmap: Bitmap) {
        Log.d(AuthRepository.TAG, "Updating personal info...")

        val uploadReference =
            Firebase.storage.reference.child("therapists/${therapist.id}/images/avatar")

        uploadTherapistAvatar(uploadReference, bitmap, therapist)

    }

    private fun uploadTherapistAvatar(storageReference: StorageReference, bitmap: Bitmap, therapist: Therapist){
        storageReference.putBytes(getByteArray(bitmap), fileMetadata)
            .addOnProgressListener { task ->
                val progress = (100.0 * task.bytesTransferred) / task.totalByteCount
                Log.d(TAG, "Upload progress: $progress%")
                shortToastMessage(
                    application.applicationContext,
                    application.getString(R.string.toast_upload_progress, progress.toInt())
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

                storageReference.downloadUrl
            }.addOnFailureListener { e ->
                Log.e(TAG, "Error: Failed to upload avatar", e.cause)
                shortToastMessage(
                    application.applicationContext,
                    application.getString(R.string.toast_upload_fail)
                )
            }.addOnCompleteListener { task ->
                Log.d(TAG, "Upload success")
                shortToastMessage(
                    application.applicationContext,
                    application.getString(R.string.toast_upload_success))

                // Update avatar file path alongside other details
                Firebase.firestore.collection(application.getString(R.string.dbcol_users))
                    .document(therapist.id).update(
                        mapOf(
                            "avatar" to task.result.toString(),
                            "name" to therapist.name,
                            "phone_no" to therapist.phone_no,
                            "rate" to therapist.rate
                        )
                    )
            }
    }

    private fun uploadPatientAvatar(storageReference: StorageReference, bitmap: Bitmap, patient: Patient){
        storageReference.putBytes(getByteArray(bitmap), fileMetadata)
            .addOnProgressListener { task ->
                val progress = (100.0 * task.bytesTransferred) / task.totalByteCount
                Log.d(TAG, "Upload progress: $progress%")
                shortToastMessage(
                    application.applicationContext,
                    application.getString(R.string.toast_upload_progress, progress.toInt())
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

                storageReference.downloadUrl
            }.addOnFailureListener { e ->
                Log.e(TAG, "Error: Failed to upload avatar", e.cause)
                shortToastMessage(
                    application.applicationContext,
                    application.getString(R.string.toast_upload_fail)
                )
            }.addOnCompleteListener { task ->
                Log.d(TAG, "Upload success")
                shortToastMessage(
                    application.applicationContext,
                    application.getString(R.string.toast_upload_success))

                // Update avatar file path alongside other details
                Firebase.firestore.collection(application.getString(R.string.dbcol_users))
                    .document(patient.id).update(
                        mapOf(
                            "avatar" to task.result.toString(),
                            "name" to patient.name,
                            "phone_no" to patient.phone_no
                        )
                    )
            }
    }

    private fun uploadResume(storageReference: StorageReference, authId: String, email: String, securityLevel: Int, resumeUri: Uri?){
        val fileMetadata = storageMetadata {
            contentType = "application/pdf"
            setCustomMetadata(CUSTOM_KEY, CUSTOM_VALUE)
        }

        if (resumeUri != null) {
            storageReference.putFile(resumeUri, fileMetadata).addOnProgressListener { task ->
                val progress = (100.0 * task.bytesTransferred) / task.totalByteCount
                Log.d(TAG, "Upload progress: $progress%")
                shortToastMessage(
                    application.applicationContext,
                    application.getString(R.string.toast_upload_progress, progress.toInt())
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

                storageReference.downloadUrl
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
    }



    /* ======================================================
    *   Function to retrieve bytearray from user's avatar
    *   @param bitmap: Bitmap
    *   @return ByteArray
    * =======================================================  */
    private fun getByteArray(bitmap: Bitmap): ByteArray{
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    /* ======================================================
    *   Function to update patient's personal details
    *   @param patient: Patient
    * =======================================================  */
    private fun updatePatientPersonalInfo(patient: Patient, bitmap: Bitmap){
        Log.d(AuthRepository.TAG, "Updating personal info...")

        val uploadReference =
            Firebase.storage.reference.child("patients/${patient.id}/images/avatar")

        uploadPatientAvatar(uploadReference, bitmap, patient)
    }


    companion object {
        val TAG = UserRepository::class.simpleName
        const val CUSTOM_KEY = "File Type"
        const val CUSTOM_VALUE = "Resume"
        const val CUSTOM_VALUE_1 = "Profile image"
    }
}