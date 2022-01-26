package com.anna.mindhealth.ui.auth.before

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.anna.mindhealth.R
import com.anna.mindhealth.base.Utility.PATIENT_ROLE
import com.anna.mindhealth.base.Utility.THERAPIST_ROLE
import com.anna.mindhealth.base.Utility.getFileName
import com.anna.mindhealth.base.Utility.shortToastMessage
import com.anna.mindhealth.databinding.ActivityRegisterBinding
import com.anna.mindhealth.ui.therapist.profile.TherapistInfoFragment
import com.google.android.material.textview.MaterialTextView

class RegisterActivity: AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    private var resumeUri: Uri ?= null
    private val selectedSecurityLevel by lazy {
        intent.getIntExtra(LoginActivity.securityLevel, 0)
    }
    private var storagePermission: Boolean = false

    private val requestStoragePermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { permission ->
        Log.d(TherapistInfoFragment.TAG, "RequestStoragePermission")
        if (permission == true){
            Log.d(TherapistInfoFragment.TAG, "User has allowed permission to access MANIFEST.permission.READ_EXTERNAL_STORAGE")
        }
    }

    private fun verifyStoragePermission() {
        Log.d(TherapistInfoFragment.TAG, "verifyPermissions: asking user for permissions.")
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE

        if (ContextCompat.checkSelfPermission(binding.root.context, permission) == PackageManager.PERMISSION_GRANTED){
            storagePermission = true
        } else {
            // Request for permissions
            requestStoragePermission.launch(permission)
        }
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
                when(storagePermission){
                    true -> openFileSelector.launch("application/pdf")
                    else -> verifyStoragePermission()
                }

            }
        }

        binding.btnRegisterViaEmail.setOnClickListener {
            when(selectedSecurityLevel){
                PATIENT_ROLE -> {
                    if (registerViewModel.validateCredentialsInput(
                            binding.edtInputEmail.editText!!.text.toString(),
                            binding.edtInputPassword.editText!!.text.toString(),
                            binding.edtInputConfirmPassword.editText!!.text.toString())){
                        registerViewModel.register(
                            binding.edtInputEmail.editText!!.text.toString(),
                            binding.edtInputPassword.editText!!.text.toString(),
                            selectedSecurityLevel,
                            null)
                    }
                }

                THERAPIST_ROLE -> {
                    if (registerViewModel.validateCredentialsInput(
                            binding.edtInputEmail.editText!!.text.toString(),
                            binding.edtInputPassword.editText!!.text.toString(),
                            binding.edtInputConfirmPassword.editText!!.text.toString())){
                        if (resumeUri != null){
                            registerViewModel.register(
                                binding.edtInputEmail.editText!!.text.toString(),
                                binding.edtInputPassword.editText!!.text.toString(),
                                selectedSecurityLevel,
                                resumeUri)
                            startActivity(Intent(this, LoginActivity::class.java).putExtra(
                                securityLevel, selectedSecurityLevel))
                        } else {
                            shortToastMessage(this, getString(R.string.error_msg_no_file))
                        }

                    }
                }
            }

        }

        initializeLink()
    }



    private fun initializeLink(){
        binding.txvLoginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java).apply {
                putExtra(securityLevel, selectedSecurityLevel)
                putExtra(activityName, TAG)
            })
        }
    }

    companion object{
        val TAG = RegisterActivity::class.simpleName
        const val securityLevel = "com.anna.MindHealth.ui.auth.before.RegisterActivity.securityLevel"
        const val activityName = "com.anna.MindHealth.ui.auth.before.RegisterActivity"
    }

}