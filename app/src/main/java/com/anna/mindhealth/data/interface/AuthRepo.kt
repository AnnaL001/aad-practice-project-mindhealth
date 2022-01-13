package com.anna.mindhealth.data.`interface`

import android.net.Uri

interface AuthRepo {
    fun logIn(email: String, password: String)
    fun register(email: String, password: String, securityLevel: Int, resumeUri: Uri?)
    fun sendVerification()
    fun checkAuthState()
    fun logOut()
}