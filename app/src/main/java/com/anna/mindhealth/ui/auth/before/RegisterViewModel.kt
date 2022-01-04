package com.anna.mindhealth.ui.auth.before

import android.app.Application
import androidx.core.util.PatternsCompat
import androidx.lifecycle.AndroidViewModel
import com.anna.mindhealth.R
import com.anna.mindhealth.base.Utility.shortToastMessage
import com.anna.mindhealth.data.`interface`.AuthRepo
import com.anna.mindhealth.data.repository.AuthRepository

class RegisterViewModel(application: Application): AndroidViewModel(application) {
    private val authRepository: AuthRepo

    init {
        authRepository = AuthRepository(application)
    }


    fun register(email: String, password: String, securityLevel: Int){
        authRepository.register(email, password, securityLevel)
    }

    /* =========================
    *  Validate all input fields
    *  @param email
    *  @param password
    *  @param confirmPassword
    *  ========================= */
    fun validateCredentialsInput(email: String, password: String, confirmPassword: String): Boolean{
        var isValidated = false

        if(email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
            if (email.isEmailValid()){
                if (password.length >= 8){

                    if (password.isPasswordValid()){
                        if (password == confirmPassword){
                            isValidated = true
                        } else {
                            shortToastMessage(getApplication<Application>().applicationContext, getApplication<Application>().getString(R.string.error_msg_pswd_confirm_pswd))
                        }
                    } else {
                       shortToastMessage(getApplication<Application>().applicationContext, getApplication<Application>().getString(R.string.error_msg_pswd_criteria))
                    }

                } else {
                    shortToastMessage(getApplication<Application>().applicationContext, getApplication<Application>().getString(R.string.error_msg_pswd_length))
                }
            } else {
                shortToastMessage(getApplication<Application>().applicationContext, getApplication<Application>().getString(R.string.error_msg_email_criteria))
            }
        } else {
            shortToastMessage(getApplication<Application>().applicationContext, getApplication<Application>().getString(R.string.error_msg_required))
        }

        return isValidated
    }

    /* =================================================
    *  Extension function to check for password validity
    *  ================================================= */
    private fun String.isPasswordValid(): Boolean{
        // Password criteria
        val hasUpperCase: (Char) -> Boolean = { it.isUpperCase() }
        val hasLowerCase: (Char) -> Boolean = { it.isLowerCase() }
        val hasDigits: (Char) -> Boolean = { it.isDigit() }

        return this.any { hasUpperCase(it) } && this.any { hasLowerCase(it) } && this.any { hasDigits(it) }
    }

    /* ==============================================
    *  Extension function to check for email validity
    *  ============================================== */
    private fun String.isEmailValid(): Boolean = PatternsCompat.EMAIL_ADDRESS.matcher(this).matches()

}