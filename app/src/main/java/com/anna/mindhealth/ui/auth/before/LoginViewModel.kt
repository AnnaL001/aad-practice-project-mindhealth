package com.anna.mindhealth.ui.auth.before

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.anna.mindhealth.data.`interface`.AuthRepo
import com.anna.mindhealth.data.`interface`.UserRepo
import com.anna.mindhealth.data.repository.AuthRepository
import com.anna.mindhealth.data.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.ktx.Firebase

class LoginViewModel(application: Application): AndroidViewModel(application) {
    private val authRepository: AuthRepo
    val authUser: LiveData<FirebaseUser>

    init {
        authRepository = AuthRepository(application)
        authUser = authRepository.authUser
    }

    fun login(email: String, password: String){
        authRepository.logIn(email, password)
    }
}