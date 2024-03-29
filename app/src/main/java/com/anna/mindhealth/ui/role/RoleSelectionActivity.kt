package com.anna.mindhealth.ui.role

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.anna.mindhealth.base.Utility.PATIENT_ROLE
import com.anna.mindhealth.base.Utility.THERAPIST_ROLE
import com.anna.mindhealth.databinding.ActivityRoleSelectionBinding
import com.anna.mindhealth.ui.auth.after.PatientActivity
import com.anna.mindhealth.ui.auth.after.TherapistActivity
import com.anna.mindhealth.ui.auth.before.LoginActivity

class RoleSelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRoleSelectionBinding
    private lateinit var roleSelectionViewModel: RoleSelectionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoleSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        roleSelectionViewModel = ViewModelProvider(this).get(RoleSelectionViewModel::class.java)

        roleSelectionViewModel.authUser.observe(this) { firebaseUser ->
            if (firebaseUser != null) {
                roleSelectionViewModel.therapistReference?.get()?.addOnSuccessListener { document ->
                    when(document.exists()){
                        true -> startActivity(Intent(this, TherapistActivity::class.java))
                        false -> startActivity(Intent(this, PatientActivity::class.java))
                    }
                }?.addOnFailureListener {
                    Log.e(TAG, "Error fetching user data", it.cause)
                }

            }
        }

        initializeButtons()
    }

    private fun initializeButtons(){
        binding.btnPatientSelection.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java).apply {
                putExtra(securityLevel, PATIENT_ROLE)
                putExtra(activityName, TAG)
            })
        }

        binding.btnTherapistSelection.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java).apply {
                putExtra(securityLevel, THERAPIST_ROLE)
                putExtra(activityName, TAG)
            })
        }
    }

    override fun onStart() {
        super.onStart()
        roleSelectionViewModel.checkAuthenticationState()
    }

    companion object {
        val TAG = RoleSelectionActivity::class.simpleName
        const val securityLevel = "com.anna.mindHealth.ui.role.RoleSelectionActivity.securityLevel"
        const val activityName = "com.anna.mindHealh.ui.RoleSelectionActivity"
    }
}