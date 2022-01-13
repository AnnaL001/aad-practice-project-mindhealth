package com.anna.mindhealth.ui.auth.before

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.anna.mindhealth.R
import com.anna.mindhealth.base.Utility.PATIENT_ROLE
import com.anna.mindhealth.base.Utility.THERAPIST_ROLE
import com.anna.mindhealth.base.Utility.getFileName
import com.anna.mindhealth.databinding.ActivityRegisterBinding
import com.google.android.material.textview.MaterialTextView

class RegisterActivity: AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    private var resumeUri: Uri ?= null

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

        val openFileSelector = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
            if (uri != null){
                resumeUri = uri
                findViewById<MaterialTextView>(R.id.txv_upload_CV).text = getString(R.string.txv_upload_cv_file_name, getFileName(this, uri))
            }
        }

        if (selectedSecurityLevel == THERAPIST_ROLE){
            binding.vsSubmitCv.inflate()
            findViewById<ImageButton>(R.id.imv_btn_upload_cv).setOnClickListener {
                openFileSelector.launch("application/pdf")
            }
        }

        binding.btnRegisterViaEmail.setOnClickListener {
            when(selectedSecurityLevel){
                PATIENT_ROLE -> {
                    if (registerViewModel.validateCredentialsInput(inputEmail,inputPassword,inputConfirmPassword)){
                        registerViewModel.register(inputEmail, inputPassword, selectedSecurityLevel, null)
                    }
                }

                THERAPIST_ROLE -> {
                    if (registerViewModel.validateCredentialsInput(inputEmail, inputPassword, inputConfirmPassword) && resumeUri != null){
                            registerViewModel.register(inputEmail, inputPassword, selectedSecurityLevel,resumeUri)
                            startActivity(Intent(this, LoginActivity::class.java).putExtra(
                                securityLevel, selectedSecurityLevel))
                    }
                }
            }

        }

        initializeLink()
    }



    private fun initializeLink(){
        binding.txvLoginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java).
            putExtra(securityLevel, selectedSecurityLevel))
        }
    }

    companion object{
        const val securityLevel = "com.anna.MindHealth.ui.auth.before.RegisterActivity.securityLevel"
    }

}