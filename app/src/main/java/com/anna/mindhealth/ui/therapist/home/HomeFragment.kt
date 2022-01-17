package com.anna.mindhealth.ui.therapist.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.anna.mindhealth.R
import com.anna.mindhealth.base.Utility.THERAPIST_ROLE
import com.anna.mindhealth.data.model.Therapist
import com.anna.mindhealth.databinding.FragmentHomeTherapistBinding
import com.google.firebase.firestore.ktx.toObject

class HomeFragment: Fragment() {
    private var _binding: FragmentHomeTherapistBinding ?= null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeTherapistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        homeViewModel.therapistReference?.get()?.addOnCompleteListener { task ->
            val therapist = task.result.toObject<Therapist>()

            if (therapist!!.security_level == THERAPIST_ROLE && therapist.is_vetted){
                binding.root.visibility = View.VISIBLE
            }

            setViewDetails(therapist.name, therapist.is_available)
        }

        initializeButtons()
    }

    private fun setViewDetails(name: String, availability: Boolean){
        binding.txvGreetingsUser.text = getString(R.string.txv_greetings_user_text, name)

        when(availability){
            true -> binding.txvSetAvailability.text = getString(R.string.txv_availability_set_text, "Yes")
            false -> binding.txvSetAvailability.text = getString(R.string.txv_availability_set_text, "No")
        }
    }

    private fun initializeButtons(){
        binding.btnUpdateProfileLink.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_fragment_home_to_fragment_edit_profile)
        }

        binding.btnUpdateAvailabilityLink.setOnClickListener {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}