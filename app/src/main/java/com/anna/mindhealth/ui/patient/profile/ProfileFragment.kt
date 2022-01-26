package com.anna.mindhealth.ui.patient.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.anna.mindhealth.R
import com.anna.mindhealth.databinding.FragmentProfileBinding

class ProfileFragment: Fragment() {
    private var _binding: FragmentProfileBinding?= null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeButtons()
    }

    private fun initializeButtons(){
        binding.cardPersonalInfoLink.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_fragment_to_fragment_personal_info)
        }

        binding.cardTherapistProfileLink.setOnClickListener {

        }

        binding.cardSessionHistoryLink.setOnClickListener {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}