package com.anna.mindhealth.ui.role

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.anna.mindhealth.data.`interface`.AuthRepo
import com.anna.mindhealth.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser

class RoleSelectionViewModel(application: Application): AndroidViewModel(application) {
    private val authRepository: AuthRepo
    val authUser: LiveData<FirebaseUser>

    init {
        authRepository = AuthRepository(application)
        authUser = authRepository.authUser
    }

    fun checkAuthenticationState(){
        authRepository.checkAuthState()
    }
}