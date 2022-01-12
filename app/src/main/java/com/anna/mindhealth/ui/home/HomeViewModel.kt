package com.anna.mindhealth.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.anna.mindhealth.data.`interface`.AuthRepo
import com.anna.mindhealth.data.`interface`.UserRepo
import com.anna.mindhealth.data.repository.AuthRepository
import com.anna.mindhealth.data.repository.UserRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.ktx.Firebase

class HomeViewModel(application: Application): AndroidViewModel(application) {
    private val authRepository: AuthRepo
    private val userRepository: UserRepo

    val patientReference: DocumentReference ?

    init {
        authRepository = AuthRepository(application)
        userRepository = UserRepository(application)
        patientReference = userRepository.read(Firebase.auth.currentUser!!.uid)
    }
}