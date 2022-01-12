package com.anna.mindhealth.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.anna.mindhealth.R
import com.anna.mindhealth.base.Utility.shortToastMessage
import com.anna.mindhealth.data.`interface`.AuthRepo
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AuthRepository(private val application: Application): AuthRepo {
    private var _authUser: MutableLiveData<FirebaseUser> = MutableLiveData()

    val authUser: LiveData<FirebaseUser> get() = _authUser

    /* ===============================
    *   Function to authenticate a user
    *   @param email
    *   @param password
    * ================================  */
    override fun logIn(email: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            Log.i(TAG, "Signing In user ...")
            when(task.isSuccessful){
                true -> {
                    if (Firebase.auth.currentUser!!.isEmailVerified){
                        // Update authenticated user value
                        _authUser.postValue(Firebase.auth.currentUser)

                        Log.d(TAG, "signInUserWithEmail: Success")
                        shortToastMessage(application.applicationContext, application.getString(
                            R.string.toast_log_in_success, Firebase.auth.currentUser!!.email))

                    } else {
                        Log.d(TAG, "Sending email verification prompt")
                        shortToastMessage(application.applicationContext, application.getString(
                            R.string.toast_email_verification_prompt))
                    }
                }
                else -> {
                        Log.d(TAG, "signInUserWithEmail: Failure", task.exception)
                        shortToastMessage(application.applicationContext, application.getString(R.string.toast_log_in_fail))
                }
            }
        }

    }

    /* ================================
    *   Function to register a user
    *   @param email
    *   @param password
    *   @param securityLevel
    * =================================  */
    override fun register(email: String, password: String, securityLevel: Int) {
        Firebase.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            Log.i(TAG, "Registering user with $email")
            when(task.isSuccessful){
                true -> {
                    Log.d(TAG, "createUserWithEmail: Success")
                    // Update authenticated user value
                    _authUser.postValue(Firebase.auth.currentUser)
                    sendVerification()
                    UserRepository(application).insert(email,securityLevel)
                }
                else -> {
                    Log.e(TAG, "createUserWithEmail: Failure", task.exception)
                }
            }
        }

    }


    /* ========================================
    *   Function to send an email verification
    * =========================================  */
    override fun sendVerification() {
        Firebase.auth.currentUser!!.sendEmailVerification().addOnCompleteListener { task ->
            Log.i(TAG, "Sending email verification...")
            if (task.isSuccessful){
                Log.d(TAG, "Email verification sent")
                shortToastMessage(application.applicationContext, application.getString(
                    R.string.toast_email_verification_success
                ))
            } else {
                Log.e(TAG, "Email verification not sent", task.exception)
                shortToastMessage(application.applicationContext, application.getString(
                    R.string.toast_email_verification_fail
                ))
            }
        }
    }

    /* =============================================
    *   Function to check user authentication state
    * ==============================================  */
    override fun checkAuthState() {
        Log.i(TAG, "Checking authentication ...")
        if (Firebase.auth.currentUser != null){
            if(Firebase.auth.currentUser!!.isEmailVerified){
                Log.d(TAG, "CheckAuthState: Signed In")
                _authUser.postValue(Firebase.auth.currentUser)
            }
        } else {
            Log.d(TAG, "CheckAuthState: Signed Out")
            _authUser.postValue(null)
        }
    }


    /* ========================================
    *   Function to log out a user
    * =========================================  */
    override fun logOut() {
        Firebase.auth.signOut()
        _authUser.postValue(null)
    }

    companion object{
        val TAG = this::class.simpleName
    }
}