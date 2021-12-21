package com.anna.mindhealth.ui.auth.after

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.forEach
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.anna.mindhealth.R
import com.anna.mindhealth.data.repository.AuthRepository
import com.anna.mindhealth.databinding.ActivityMainBinding
import com.anna.mindhealth.ui.role.RoleSelectionActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    private val navView by lazy {
        binding.layoutContentMain.navView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        mainViewModel.authUser.observe(this, { firebaseUser ->
            if (firebaseUser == null){
                startActivity(Intent(this, RoleSelectionActivity::class.java))
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

    override fun onStart() {
        super.onStart()
        mainViewModel.checkAuthenticationState()
    }

}