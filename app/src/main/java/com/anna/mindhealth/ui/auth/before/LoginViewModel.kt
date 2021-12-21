package com.anna.mindhealth.ui.auth.before

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.anna.mindhealth.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser

class LoginViewModel(application: Application): AndroidViewModel(application) {
    private val authRepository: AuthRepository
    val authUser: LiveData<FirebaseUser>

    init {
        authRepository = AuthRepository(application)
        authUser = authRepository.authUser
    }

    fun login(email: String, password: String){
        authRepository.logIn(email, password)
    }
}