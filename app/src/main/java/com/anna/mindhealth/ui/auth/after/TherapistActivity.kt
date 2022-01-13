package com.anna.mindhealth.ui.auth.after

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.anna.mindhealth.R
import com.anna.mindhealth.base.Utility.THERAPIST_ROLE
import com.anna.mindhealth.base.Utility.shortToastMessage
import com.anna.mindhealth.databinding.ActivityTherapistBinding
import com.anna.mindhealth.ui.role.RoleSelectionActivity

class TherapistActivity: AppCompatActivity() {
    private lateinit var binding: ActivityTherapistBinding
    private lateinit var userActivityViewModel: UserActivityViewModel

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
                    }
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu_therapist, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.option_logout_therapist -> {
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