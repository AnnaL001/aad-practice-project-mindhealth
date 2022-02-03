package com.anna.mindhealth.ui.role

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.anna.mindhealth.base.Utility.THERAPIST_ROLE
import com.anna.mindhealth.data.`interface`.AuthRepo
import com.anna.mindhealth.data.`interface`.UserRepo
import com.anna.mindhealth.data.repository.AuthRepository
import com.anna.mindhealth.data.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.ktx.Firebase

class RoleSelectionViewModel(application: Application): AndroidViewModel(application) {
    private val authRepository: AuthRepo
    private val userRepository: UserRepo
    val authUser: LiveData<FirebaseUser>

    init {
        authRepository = AuthRepository(application)
        userRepository = UserRepository(application)
        authUser = authRepository.authUser
    }

    fun checkAuthenticationState(){
        authRepository.checkAuthState()
    }

    val therapistReference: DocumentReference? = userRepository.read(Firebase.auth.currentUser?.uid, THERAPIST_ROLE)
}