package com.anna.mindhealth.data.`interface`

interface AuthRepo {
    fun logIn(email: String, password: String)
    fun register(email: String, password: String)
    fun sendVerification()
    fun checkAuthState()
    fun logOut()
}