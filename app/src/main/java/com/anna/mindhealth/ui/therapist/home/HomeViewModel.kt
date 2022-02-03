package com.anna.mindhealth.ui.therapist.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.anna.mindhealth.base.Utility.THERAPIST_ROLE
import com.anna.mindhealth.data.`interface`.TherapistRepo
import com.anna.mindhealth.data.`interface`.UserRepo
import com.anna.mindhealth.data.model.Therapist
import com.anna.mindhealth.data.repository.TherapistRepository
import com.anna.mindhealth.data.repository.UserRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.ktx.Firebase

class HomeViewModel(application: Application): AndroidViewModel(application) {
    private val userRepository: UserRepo
    private val therapistRepository: TherapistRepo
    val therapistReference: DocumentReference?

    init {
        userRepository = UserRepository(application)
        therapistRepository = TherapistRepository(application)
        therapistReference = userRepository.read(Firebase.auth.currentUser!!.uid, THERAPIST_ROLE)
    }

    fun updateTherapistAvailability(therapist: Therapist){
        therapistRepository.updateTherapistAvailability(therapist)
    }
}