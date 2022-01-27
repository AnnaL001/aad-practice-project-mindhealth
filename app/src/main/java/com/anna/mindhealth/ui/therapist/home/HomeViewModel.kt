package com.anna.mindhealth.ui.therapist.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.anna.mindhealth.data.`interface`.UserRepo
import com.anna.mindhealth.data.model.Therapist
import com.anna.mindhealth.data.repository.UserRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.ktx.Firebase

class HomeViewModel(application: Application): AndroidViewModel(application) {
    private val userRepository: UserRepo
    val therapistReference: DocumentReference?

    init {
        userRepository = UserRepository(application)
        therapistReference = userRepository.read(Firebase.auth.currentUser!!.uid)
    }

    fun updateTherapistAvailability(therapist: Therapist){
        userRepository.updateTherapistAvailability(therapist)
    }
}