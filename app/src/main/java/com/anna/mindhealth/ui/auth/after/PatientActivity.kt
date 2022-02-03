package com.anna.mindhealth.ui.auth.after

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.anna.mindhealth.R
import com.anna.mindhealth.base.Utility.PATIENT_ROLE
import com.anna.mindhealth.base.Utility.shortToastMessage
import com.anna.mindhealth.databinding.ActivityPatientBinding
import com.anna.mindhealth.ui.auth.before.LoginActivity
import com.anna.mindhealth.ui.role.RoleSelectionActivity

class PatientActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPatientBinding
    private val userActivityViewModel: UserActivityViewModel by lazy {
        ViewModelProvider(this).get(UserActivityViewModel::class.java)
    }

    private val navView by lazy {
        binding.layoutContentPatient.navView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        init()
        initializeNavController()

    }

    private fun init(){
        userActivityViewModel.authUser.observe(this) { patientUser ->
            if (patientUser == null) {
                startActivity(Intent(this, RoleSelectionActivity::class.java))
            } else {
                userActivityViewModel.patientReference?.get()?.addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        if (!task.result.exists()){
                            userActivityViewModel.logout()
                            shortToastMessage(this, getString(R.string.toast_log_in_wrong_role_patient))
                        }
                    }
                }?.addOnFailureListener {
                    Log.e(LoginActivity.TAG, "Error fetching patient data", it.cause)
                }
            }
        }
    }

    private fun initializeNavController(){
        // Get NavHostFragment and NavController
        val navController = findNavController(R.id.nav_host_fragment_content_patient)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_profile, R.id.nav_session, R.id.nav_more
            )
        )
        // Connect toolbar with Navigation controller
        setupActionBarWithNavController(navController, appBarConfiguration)
        // Connect Bottom Navigation View with NavController
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.option_logout -> {
                userActivityViewModel.logout()
                true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        userActivityViewModel.checkAuthenticationState()
    }



    companion object{
        val TAG = PatientActivity::class.simpleName
    }
    
}