package com.anna.mindhealth.ui.therapist.therapy_profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.anna.mindhealth.data.`interface`.CrudRepo
import com.anna.mindhealth.data.`interface`.UserRepo
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

    init {
        therapistProfileRepository = TherapistProfileRepository(application)
        userRepository = UserRepository(application)
        therapistProfileReference = therapistProfileRepository.read(Firebase.auth.currentUser!!.uid)
    }

    fun updateProfile(therapistProfile: TherapistProfile){
        therapistProfileRepository.insert(therapistProfile)
    }
}