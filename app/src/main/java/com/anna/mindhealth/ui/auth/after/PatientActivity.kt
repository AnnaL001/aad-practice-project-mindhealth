package com.anna.mindhealth.ui.auth.after

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.anna.mindhealth.R
import com.anna.mindhealth.base.Utility.shortToastMessage
import com.anna.mindhealth.databinding.ActivityPatientBinding
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

        userActivityViewModel.authUser.observe(this, { patientUser ->
            if (patientUser == null){
                startActivity(Intent(this, RoleSelectionActivity::class.java))
            } else {
                userActivityViewModel.userReference?.get()?.addOnCompleteListener { task ->
                    if (task.result.data?.get("security_level").toString().toInt() != 1){
                        userActivityViewModel.logout()
                        shortToastMessage(this, getString(R.string.toast_log_in_wrong_role_patient))
                    }
                }
            }
        })

        // Get NavHostFragment and NavController
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
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
        menuInflater.inflate(R.menu.option_menu_patient, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.option_logout_patient -> {
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
}