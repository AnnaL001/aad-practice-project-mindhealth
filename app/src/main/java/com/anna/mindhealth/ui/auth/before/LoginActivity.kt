package com.anna.mindhealth.ui.auth.before

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.anna.mindhealth.R
import com.anna.mindhealth.base.Utility.PATIENT_ROLE
import com.anna.mindhealth.base.Utility.THERAPIST_ROLE
import com.anna.mindhealth.base.Utility.shortToastMessage
import com.anna.mindhealth.databinding.ActivityLoginBinding
import com.anna.mindhealth.ui.auth.after.PatientActivity
import com.anna.mindhealth.ui.auth.after.TherapistActivity
import com.anna.mindhealth.ui.role.RoleSelectionActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    private val selectedSecurityLevel by lazy {
        intent.getIntExtra(RoleSelectionActivity.securityLevel, 0)
    }

    private val setSecurityLevel by lazy {
        intent.getIntExtra(RegisterActivity.securityLevel, 0)
    }

    private val edtInputEmailLogin by lazy {
        binding.edtInputEmailLogin.editText
    }

    private val edtInputPasswordLogin by lazy {
        binding.edtInputPasswordLogin.editText
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        initializeButtons()
        initializeSelectedSecurityLevel()
        initializeSetSecurityLevel()
        initializeLinks()
    }

    private fun initializeSelectedSecurityLevel(){
        when (selectedSecurityLevel) {
            PATIENT_ROLE -> {
                loginViewModel.authUser.observe(this, { patientUser ->
                    if (patientUser != null) {
                        startActivity(Intent(this, PatientActivity::class.java))
                    }
                })
            }

            THERAPIST_ROLE -> {
                loginViewModel.authUser.observe(this, { therapistUser ->
                    if (therapistUser != null) {
                        startActivity(Intent(this, TherapistActivity::class.java))
                    }
                })
            }
            else -> Log.d(
                TAG,
                "User's selected security level is not set"
            )
        }
    }

    private fun initializeSetSecurityLevel(){
        when (setSecurityLevel) {
            PATIENT_ROLE -> {
                loginViewModel.authUser.observe(this, { patientUser ->
                    if (patientUser != null) {
                        startActivity(Intent(this, PatientActivity::class.java))
                    }
                })
            }

            THERAPIST_ROLE -> {
                loginViewModel.authUser.observe(this, { therapistUser ->
                    if (therapistUser != null) {
                        startActivity(Intent(this, TherapistActivity::class.java))
                    }
                })
            }
            else -> Log.d(
                TAG,
                "User's set security level is not set"
            )
        }
    }

    private fun initializeButtons(){
        binding.btnLoginViaEmail.setOnClickListener {
            if (edtInputEmailLogin!!.text.toString().isNotEmpty() &&
                edtInputPasswordLogin!!.text.toString().isNotEmpty()){
                loginViewModel.login(
                    edtInputEmailLogin!!.text.toString(),
                    edtInputPasswordLogin!!.text.toString())
            } else {
                shortToastMessage(this, getString(R.string.error_msg_required))
            }
        }

    }

    private fun initializeLinks(){
        val roleSelectionActivity = intent.getStringExtra(RoleSelectionActivity.activityName)
        val registerActivity = intent.getStringExtra(RegisterActivity.activityName)

        binding.txvRegisterLink.setOnClickListener {
            if (roleSelectionActivity != null){
                startActivity(Intent(this, RegisterActivity::class.java)
                    .putExtra(securityLevel, selectedSecurityLevel))
            } else if (registerActivity != null){
                startActivity(Intent(this, RegisterActivity::class.java)
                    .putExtra(securityLevel, setSecurityLevel))
            }
        }

        binding.txvResetPassword.setOnClickListener {

        }
    }



    companion object {
        val TAG = LoginActivity::class.simpleName
        const val securityLevel = "com.anna.MindHealth.ui.auth.before.LoginActivity.securityLevel"
    }
}