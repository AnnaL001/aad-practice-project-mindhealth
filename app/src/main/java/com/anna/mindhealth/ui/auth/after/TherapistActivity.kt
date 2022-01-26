package com.anna.mindhealth.ui.auth.after

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.anna.mindhealth.R
import com.anna.mindhealth.base.Utility.THERAPIST_ROLE
import com.anna.mindhealth.base.Utility.shortToastMessage
import com.anna.mindhealth.databinding.ActivityTherapistBinding
import com.anna.mindhealth.ui.role.RoleSelectionActivity

class TherapistActivity: AppCompatActivity() {
    private lateinit var binding: ActivityTherapistBinding
    private lateinit var userActivityViewModel: UserActivityViewModel

    private val navView by lazy {
        binding.layoutContentTherapist.navView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTherapistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        userActivityViewModel = ViewModelProvider(this).get(UserActivityViewModel::class.java)

        userActivityViewModel.authUser.observe(this, { therapistUser ->
            if (therapistUser == null){
                startActivity(Intent(this, RoleSelectionActivity::class.java))
            } else {
                userActivityViewModel.userReference?.get()?.addOnCompleteListener { task ->
                    if (task.result.data?.get("security_level").toString().toInt() != THERAPIST_ROLE){
                        userActivityViewModel.logout()
                        shortToastMessage(this, getString(R.string.toast_log_in_wrong_role_therapist)
                        )
                    } else if(!task.result.data?.get("is_vetted").toString().toBoolean()){
                        userActivityViewModel.logout()
                        shortToastMessage(this, getString(R.string.toast_log_in_unvetted_therapist))
                    }
                }
            }
        })

        initializeNavController()
    }

    private fun initializeNavController(){
        // Get NavHostFragment and NavController
        val navController = findNavController(R.id.nav_host_fragment_content_therapist)
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

    override fun onBackPressed() {
        super.onBackPressed()
        userActivityViewModel.logout()
    }



    override fun onStart() {
        super.onStart()
        userActivityViewModel.checkAuthenticationState()
    }

}