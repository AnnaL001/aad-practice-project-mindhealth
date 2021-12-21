package com.anna.mindhealth.ui.role

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.anna.mindhealth.databinding.ActivityRoleSelectionBinding
import com.anna.mindhealth.ui.auth.after.MainActivity
import com.anna.mindhealth.ui.auth.before.LoginActivity

class RoleSelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRoleSelectionBinding
    private lateinit var roleSelectionViewModel: RoleSelectionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoleSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        roleSelectionViewModel = ViewModelProvider(this).get(RoleSelectionViewModel::class.java)

        roleSelectionViewModel.authUser.observe(this, { firebaseUser ->
            if (firebaseUser != null){
                startActivity(Intent(this, MainActivity::class.java))
            }
        })

        initializeButtons()
    }

    private fun initializeButtons(){
        binding.btnPatientSelection.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java).apply {
                putExtra(securityLevel, 1)
            })
        }

        binding.btnTherapistSelection.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java).apply {
                putExtra(securityLevel, 2)
            })
        }
    }

    override fun onStart() {
        super.onStart()
        roleSelectionViewModel.checkAuthenticationState()
    }

    companion object {
        const val securityLevel = "com.anna.mindHealth.ui.role.RoleSelection.securityLevel"
    }
}