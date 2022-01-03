package com.anna.mindhealth.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.anna.mindhealth.R
import com.anna.mindhealth.databinding.FragmentHomeBinding

class HomeFragment: Fragment() {
    private var _binding: FragmentHomeBinding ?= null

    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        initializeButtons()

        homeViewModel.authUser.observe(viewLifecycleOwner, { firebaseUser ->
            // Update button text and redirect depending on whether assessment is done
        })

    }

    private fun initializeButtons(){
        binding.btnViewResponsesLink.setOnClickListener {
            redirectToAssessment()
        }

        binding.btnTherapistSelectionLink.setOnClickListener {

        }
    }

    private fun redirectToAssessment(){
        view?.findNavController()?.navigate(R.id.action_fragment_home_to_fragment_assessment_questions)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}