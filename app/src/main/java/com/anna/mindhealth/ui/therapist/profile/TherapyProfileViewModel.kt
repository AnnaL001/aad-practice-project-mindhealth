package com.anna.mindhealth.ui.therapist.profile

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import com.anna.mindhealth.data.`interface`.CrudRepo
import com.anna.mindhealth.data.`interface`.UserRepo
import com.anna.mindhealth.data.model.Therapist
import com.anna.mindhealth.data.model.TherapistProfile
import com.anna.mindhealth.data.repository.TherapistProfileRepository
import com.anna.mindhealth.data.repository.UserRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.ktx.Firebase

class TherapyProfileViewModel(application: Application): AndroidViewModel(application) {
    private val therapistProfileRepository: CrudRepo
    private val userRepository: UserRepo
    val therapistProfileReference: DocumentReference?
    val therapistReference: DocumentReference?

    init {
        therapistProfileRepository = TherapistProfileRepository(application)
        userRepository = UserRepository(application)
        therapistProfileReference = therapistProfileRepository.read(Firebase.auth.currentUser!!.uid)
        therapistReference = userRepository.read(Firebase.auth.currentUser!!.uid)
    }

    fun updateProfile(therapistProfile: TherapistProfile){
        therapistProfileRepository.insert(therapistProfile)
    }

    fun updatePersonalInfo(therapist: Therapist, securityLevel: Int, bitmap: Bitmap){
        userRepository.update(therapist, securityLevel, bitmap)
    }
}