package com.anna.mindhealth.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.anna.mindhealth.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser

class HomeViewModel(application: Application): AndroidViewModel(application) {
    private val authRepository: AuthRepository
    val authUser: LiveData<FirebaseUser>

    init {
        authRepository = AuthRepository(application)
        authUser = authRepository.authUser
    }
}