package com.anna.mindhealth.ui.auth.before

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.anna.mindhealth.R
import com.anna.mindhealth.base.AppNotificationMethods
import com.anna.mindhealth.databinding.ActivityLoginBinding
import com.anna.mindhealth.ui.auth.after.MainActivity
import com.anna.mindhealth.ui.role.RoleSelectionActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    private val selectedSecurityLevel by lazy {
        intent.getIntExtra(RoleSelectionActivity.securityLevel, 0)
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

        loginViewModel.authUser.observe(this, { firebaseUser ->
            if (firebaseUser != null){
                startActivity(Intent(this, MainActivity::class.java))
            }
        })

        initializeButtons()
        initializeLinks()
    }

    private fun initializeButtons(){
        binding.btnLoginViaEmail.setOnClickListener {
            val email = edtInputEmailLogin!!.text.toString()
            val password = edtInputPasswordLogin!!.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()){
                loginViewModel.login(email, password)
            } else {
                AppNotificationMethods.shortToastMessage(this, getString(R.string.error_msg_required))
            }
        }

        binding.btnLoginViaGoogle.setOnClickListener {

        }
    }

    private fun initializeLinks(){
        binding.txvRegisterLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java).apply {
                putExtra(securityLevel, selectedSecurityLevel)
            })
        }

        binding.txvResetPassword.setOnClickListener {

        }
    }

    companion object {
        const val securityLevel = "com.anna.MindHealth.ui.auth.before.LoginActivity.securityLevel"
    }
}