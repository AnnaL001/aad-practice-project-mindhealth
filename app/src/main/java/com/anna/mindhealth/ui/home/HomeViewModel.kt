package com.anna.mindhealth.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.anna.mindhealth.data.`interface`.AuthRepo
import com.anna.mindhealth.data.`interface`.UserRepo
import com.anna.mindhealth.data.model.User
import com.anna.mindhealth.data.repository.AuthRepository
import com.anna.mindhealth.data.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.ktx.Firebase

class HomeViewModel(application: Application): AndroidViewModel(application) {
    private val authRepository: AuthRepo
    private val userRepository: UserRepo

    val patientData: LiveData<DocumentReference>

    init {
        authRepository = AuthRepository(application)
        userRepository = UserRepository(application)
        patientData = userRepository.readPatient(Firebase.auth.currentUser!!.uid)
    }
}