package com.anna.mindhealth.data.repository

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.anna.mindhealth.R
import com.anna.mindhealth.base.Utility
import com.anna.mindhealth.base.Utility.MAX_IN_BETWEEN
import com.anna.mindhealth.base.Utility.MAX_JUST_THERE
import com.anna.mindhealth.base.Utility.fileMetadata
import com.anna.mindhealth.base.Utility.getByteArray
import com.anna.mindhealth.base.Utility.imageMetadata
import com.anna.mindhealth.data.`interface`.TherapistRepo
import com.anna.mindhealth.data.model.Assessment
import com.anna.mindhealth.data.model.Therapist
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class TherapistRepository(private val application: Application): TherapistRepo {
    private val JUST_THERE = application.getString(R.string.just_there)
    private val IN_BETWEEN = application.getString(R.string.in_between)
    private val GREAT = application.getString(R.string.great)

    private val SINGLE = application.getString(R.string.single_status)
    private val MARRIED = application.getString(R.string.married_status)
    private val DIVORCED = application.getString(R.string.divorced_status)
    private val WIDOWED = application.getString(R.string.widowed_status)

    /* ================================================
    *   Function to insert therapist data into Firestore
    *   @param email: String
    *   @param securityLevel: Int
    *   @param resumeUri: Uri
    *   @param authId: String
    * =================================================  */
    fun insert(resumeUri: Uri?, authId: String, email: String, securityLevel: Int
    ) {
        Log.d(AuthRepository.TAG, "insertUser: Inserting ...")

        val uploadReference =
            Firebase.storage.reference.child("${application.getString(R.string.dbcol_therapists)}/$authId/${application.getString(R.string.storage_resume)}/${
                Utility.getFileName(
                    application.applicationContext,
                    resumeUri!!
                )
            }")

        uploadResume(uploadReference, authId, email, securityLevel, resumeUri)
    }

    /* ================================================
    *   Function to upload therapist resume into Firestore
    *   @param storageReference: StorageReference
    *   @param authId: String
    *   @param email: String
    *   @param securityLevel: Int
    *   @param resumeUri: Uri
    * =================================================  */
    private fun uploadResume(storageReference: StorageReference, authId: String, email: String, securityLevel: Int, resumeUri: Uri?){
        if (resumeUri != null) {
            storageReference.putFile(resumeUri, fileMetadata).addOnProgressListener { task ->
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
                Log.e(UserRepository.TAG, "Error: Failed to upload file", e.cause)
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

                val therapist = Therapist(id = authId, email = email, name = email.substring(0, email.indexOf("@")), security_level = securityLevel, resume = task.result.toString())

                Firebase.firestore.collection(application.getString(R.string.dbcol_therapists))
                    .document(authId).set(therapist).addOnCompleteListener { insertTask ->
                        if (!insertTask.isSuccessful) {
                            Log.e(UserRepository.TAG, "insertUser: Failure", insertTask.exception)
                        } else {
                            Log.d(UserRepository.TAG, "insertUser: Success ...")
                        }
                    }
            }
        }
    }

    /* ======================================================
    *   Function to update therapist's personal details
    *   @param data: Any
    * =======================================================  */
    fun update(therapist: Therapist, bitmap: Bitmap) {
        Log.d(AuthRepository.TAG, "Updating personal info...")

        val uploadReference =
            Firebase.storage.reference.child("${application.getString(R.string.dbcol_therapists)}/${therapist.id}/${application.getString(R.string.storage_images)}/avatar")

        uploadTherapistAvatar(uploadReference, bitmap, therapist)

    }

    /* ================================================
    *   Function to upload therapist avatar into Firestore
    *   @param storageReference: StorageReference
    *   @param bitmap: Bitmap
    *   @param therapist: Therapist
    * =================================================  */
    private fun uploadTherapistAvatar(storageReference: StorageReference, bitmap: Bitmap, therapist: Therapist){
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
                Firebase.firestore.collection(application.getString(R.string.dbcol_therapists))
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

    /* ======================================================
    *   Function to update therapist availability
    *   @param therapist: Therapist
    * =======================================================  */
    override fun updateTherapistAvailability(therapist: Therapist){
        Firebase.firestore.collection(application.getString(R.string.dbcol_therapists))
            .document(therapist.id).update("is_available", therapist.is_available)
    }

    /* ======================================================
    *   Function to retrieve curated list of therapists
    *   @param assessment: Assessment
    *   @return Query
    * =======================================================  */
    override fun retrieveCuratedTherapistList(assessment: Assessment): Query? {
        val preferredOptions = assessment.responses[application.getString(R.string.assessment_question11)]?.split(",")
            ?.toMutableList()

        var therapistRef : Query ?= null

        when(assessment.responses[application.getString(R.string.assessment_question8)]){
            JUST_THERE -> {
                when(assessment.responses[application.getString(R.string.assessment_question3)]){
                    SINGLE, MARRIED -> {
                        preferredOptions?.let { options ->
                            therapistRef = Firebase.firestore.collection(application.getString(R.string.dbcol_therapists))
                                .whereEqualTo("is_vetted", true)
                                .whereEqualTo("is_available", true)
                                .whereEqualTo(
                                    "profile.languages.${
                                        assessment.responses[application.getString(
                                            R.string.assessment_question9
                                        )]
                                    }", true
                                )
                                .whereArrayContainsAny("profile.services_provided", options.toList())
                                .whereLessThanOrEqualTo("rate", MAX_JUST_THERE).orderBy("rate")
                                .orderBy("rating", Query.Direction.DESCENDING).limit(5)
                        }
                    }
                    DIVORCED -> {
                        preferredOptions?.add("Divorce Counselling")
                        preferredOptions?.let { options ->
                            therapistRef = Firebase.firestore.collection(application.getString(R.string.dbcol_therapists))
                                .whereEqualTo("is_vetted", true)
                                .whereEqualTo("is_available", true)
                                .whereEqualTo("profile.languages.${assessment.responses[application.getString(R.string.assessment_question9)]}", true)
                                .whereArrayContainsAny("profile.services_provided", options.toList())
                                .whereLessThanOrEqualTo("rate", MAX_JUST_THERE)
                                .orderBy("rate")
                                .orderBy("rating", Query.Direction.DESCENDING).limit(5)

                        }
                    }
                    WIDOWED -> {
                        preferredOptions?.add("Grief Counselling")
                        preferredOptions?.let { options ->
                            therapistRef = Firebase.firestore.collection(application.getString(R.string.dbcol_therapists))
                                .whereEqualTo("is_vetted", true)
                                .whereEqualTo("is_available", true)
                                .whereEqualTo("profile.languages.${assessment.responses[application.getString(R.string.assessment_question9)]}", true)
                                .whereArrayContainsAny("profile.services_provided", options.toList())
                                .whereLessThanOrEqualTo("rate", MAX_JUST_THERE)
                                .orderBy("rate")
                                .orderBy("rating", Query.Direction.DESCENDING).limit(5)

                        }
                    }
                    else -> Log.d(UserRepository.TAG, "Relationship status not set or unknown")
                }
            }

            IN_BETWEEN -> {
                when(assessment.responses[application.getString(R.string.assessment_question3)]){
                    SINGLE, MARRIED -> {
                        preferredOptions?.let { options ->
                            therapistRef = Firebase.firestore.collection(application.getString(R.string.dbcol_therapists))
                                .whereEqualTo("is_vetted", true)
                                .whereEqualTo("is_available", true)
                                .whereEqualTo("profile.languages.${assessment.responses[application.getString(R.string.assessment_question9)]}", true)
                                .whereArrayContainsAny("profile.services_provided", options.toList())
                                .whereGreaterThan("rate", MAX_JUST_THERE)
                                .whereLessThanOrEqualTo("rate", MAX_IN_BETWEEN)
                                .orderBy("rate")
                                .orderBy("rating", Query.Direction.DESCENDING).limit(5)

                        }
                    }
                    DIVORCED -> {
                        preferredOptions?.add("Divorce Counselling")
                        preferredOptions?.let { options ->
                            therapistRef = Firebase.firestore.collection(application.getString(R.string.dbcol_therapists))
                                .whereEqualTo("is_vetted", true)
                                .whereEqualTo("is_available", true)
                                .whereEqualTo("profile.languages.${assessment.responses[application.getString(R.string.assessment_question9)]}", true)
                                .whereArrayContainsAny("profile.services_provided", options.toList())
                                .whereGreaterThan("rate", MAX_JUST_THERE)
                                .whereLessThanOrEqualTo("rate", MAX_IN_BETWEEN)
                                .orderBy("rate")
                                .orderBy("rating", Query.Direction.DESCENDING).limit(5)

                        }
                    }
                    WIDOWED -> {
                        preferredOptions?.add("Grief Counselling")
                        preferredOptions?.let { options ->
                            therapistRef = Firebase.firestore.collection(application.getString(R.string.dbcol_therapists))
                                .whereEqualTo("is_vetted", true)
                                .whereEqualTo("is_available", true)
                                .whereEqualTo("profile.languages.${assessment.responses[application.getString(R.string.assessment_question9)]}", true)
                                .whereArrayContainsAny("profile.services_provided", options.toList())
                                .whereGreaterThan("rate", MAX_JUST_THERE)
                                .whereLessThanOrEqualTo("rate", MAX_IN_BETWEEN)
                                .orderBy("rate")
                                .orderBy("rating", Query.Direction.DESCENDING).limit(5)

                        }
                    }
                    else -> Log.d(UserRepository.TAG, "Relationship status not set or unknown")
                }
            }

            GREAT -> {
                when(assessment.responses[application.getString(R.string.assessment_question3)]){
                    SINGLE, MARRIED -> {
                        preferredOptions?.let { options ->
                            therapistRef = Firebase.firestore.collection(application.getString(R.string.dbcol_therapists))
                                .whereEqualTo("is_vetted", true)
                                .whereEqualTo("is_available", true)
                                .whereEqualTo("profile.languages.${assessment.responses[application.getString(R.string.assessment_question9)]}", true)
                                .whereArrayContainsAny("profile.services_provided", options.toList())
                                .whereGreaterThan("rate", MAX_IN_BETWEEN)
                                .orderBy("rate")
                                .orderBy("rating", Query.Direction.DESCENDING).limit(5)

                        }
                    }
                    DIVORCED -> {
                        preferredOptions?.add("Divorce Counselling")
                        preferredOptions?.let { options ->
                            therapistRef = Firebase.firestore.collection(application.getString(R.string.dbcol_therapists))
                                .whereEqualTo("is_vetted", true)
                                .whereEqualTo("is_available", true)
                                .whereEqualTo("profile.languages.${assessment.responses[application.getString(R.string.assessment_question9)]}", true)
                                .whereArrayContainsAny("profile.services_provided", options.toList())
                                .whereGreaterThan("rate", MAX_IN_BETWEEN)
                                .orderBy("rate")
                                .orderBy("rating", Query.Direction.DESCENDING).limit(5)

                        }
                    }
                    WIDOWED -> {
                        preferredOptions?.add("Grief Counselling")
                        preferredOptions?.let { options ->
                            therapistRef = Firebase.firestore.collection(application.getString(R.string.dbcol_therapists))
                                .whereEqualTo("is_vetted", true)
                                .whereEqualTo("is_available", true)
                                .whereEqualTo("profile.languages.${assessment.responses[application.getString(R.string.assessment_question9)]}", true)
                                .whereArrayContainsAny("profile.services_provided", options.toList())
                                .whereGreaterThan("rate", MAX_IN_BETWEEN)
                                .orderBy("rate")
                                .orderBy("rating", Query.Direction.DESCENDING).limit(5)

                        }
                    }
                    else -> Log.d(UserRepository.TAG, "Relationship status not set or unknown")
                }
            }

            else -> Log.d(UserRepository.TAG, "Finance level not set or unknown")
        }

        return therapistRef
    }

    /* ======================================================
    *   Function to fetch therapist data
    *   @param userId: String
    * =======================================================  */
    fun read(therapistId: String?): DocumentReference? {
        val therapistRef = therapistId?.let {
            Firebase.firestore.collection(application.getString(R.string.dbcol_therapists))
                .document(it)
        }
        return therapistRef
    }

}