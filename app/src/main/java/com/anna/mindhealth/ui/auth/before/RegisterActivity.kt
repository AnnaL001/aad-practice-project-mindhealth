package com.anna.mindhealth.ui.auth.before

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.anna.mindhealth.databinding.ActivityRegisterBinding

class RegisterActivity: AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    private val inputEmail by lazy {
        binding.edtInputEmail.editText!!.text.toString()
    }

    private val inputPassword by lazy {
        binding.edtInputPassword.editText!!.text.toString()
    }

    private val inputConfirmPassword by lazy {
        binding.edtInputConfirmPassword.editText!!.text.toString()
    }

    private val selectedSecurityLevel by lazy {
        intent.getIntExtra(LoginActivity.securityLevel, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        initializeButtons()
        initializeLink()
    }

    private fun initializeButtons(){
        binding.btnRegisterViaEmail.setOnClickListener {
            if (registerViewModel.validateCredentialsInput(inputEmail,inputPassword,inputConfirmPassword)){
                registerViewModel.register(inputEmail, inputPassword, selectedSecurityLevel)
            }
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnRegisterViaGoogle.setOnClickListener {

        }
    }

    private fun initializeLink(){
        binding.txvLoginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

}