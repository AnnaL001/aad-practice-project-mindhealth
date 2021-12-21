package com.anna.mindhealth.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.anna.mindhealth.R
import com.anna.mindhealth.base.AppNotificationMethods
import com.anna.mindhealth.data.`interface`.AuthRepo
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthRepository(private val application: Application): AuthRepo {
    private var _authUser: MutableLiveData<FirebaseUser> = MutableLiveData()

    val authUser get() = _authUser

    override fun logIn(email: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            Log.i(TAG, "Signing In user ...")
            when(task.isSuccessful){
                true -> {
                    if (Firebase.auth.currentUser!!.isEmailVerified){
                        // Update authenticated user value
                        _authUser.postValue(Firebase.auth.currentUser)

                        Log.d(TAG, "signInUserWithEmail: Success")
                        AppNotificationMethods.shortToastMessage(application.applicationContext, application.getString(
                            R.string.toast_log_in_success, Firebase.auth.currentUser!!.email))
                    } else {
                        Log.d(TAG, "Sending email verification prompt")
                        AppNotificationMethods.shortToastMessage(application.applicationContext, application.getString(
                            R.string.toast_email_verification_prompt))
                    }
                }
                else -> {
                        Log.d(TAG, "signInUserWithEmail: Failure", task.exception)
                        AppNotificationMethods.shortToastMessage(application.applicationContext, application.getString(R.string.toast_log_in_fail))
                }
            }
        }

    }

    override fun register(email: String, password: String) {
        Firebase.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            Log.i(TAG, "Registering user with $email")
            when(task.isSuccessful){
                true -> {
                    Log.d(TAG, "createUserWithEmail: Success")
                    // Update authenticated user value
                    _authUser.postValue(Firebase.auth.currentUser)
                    sendVerification()
                    logOut()
                }
                else -> {
                    Log.d(TAG, "createUserWithEmail: Failure", task.exception)
                }
            }
        }

    }

    override fun sendVerification() {
        Firebase.auth.currentUser!!.sendEmailVerification().addOnCompleteListener { task ->
            Log.i(TAG, "Sending email verification...")
            if (task.isSuccessful){
                Log.d(TAG, "Email verification sent")
                AppNotificationMethods.shortToastMessage(application.applicationContext, application.getString(
                    R.string.toast_email_verification_success
                ))
            } else {
                Log.d(TAG, "Email verification not sent", task.exception)
                AppNotificationMethods.shortToastMessage(application.applicationContext, application.getString(
                    R.string.toast_email_verification_fail
                ))
            }
        }
    }

    override fun checkAuthState() {
        Log.i(TAG, "Checking authentication ...")
        if (Firebase.auth.currentUser != null){
            Log.d(TAG, "CheckAuthState: Signed In")
            _authUser.postValue(Firebase.auth.currentUser)
        } else {
            Log.d(TAG, "CheckAuthState: Signed Out")
            _authUser.postValue(null)
        }
    }

    override fun logOut() {
        Firebase.auth.signOut()
        _authUser.postValue(null)
    }

    companion object{
        val TAG = this::class.simpleName
    }
}