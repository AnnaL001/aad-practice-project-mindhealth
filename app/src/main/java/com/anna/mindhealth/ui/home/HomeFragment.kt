package com.anna.mindhealth.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.anna.mindhealth.R
import com.anna.mindhealth.data.model.Patient
import com.anna.mindhealth.databinding.FragmentHomeBinding
import com.google.firebase.firestore.ktx.toObject

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

        homeViewModel.patientData.observe(viewLifecycleOwner, { patientRef ->
            patientRef.get().addOnCompleteListener { task ->
                val patient = task.result.toObject<Patient>()

                binding.txvWelcomeUser.text = getString(R.string.txv_welcome_user_text, patient!!.name)
                initializeAssessmentStatus(patient)
            }
        })
    }


    private fun initializeAssessmentStatus(patient: Patient){
        if (patient.is_assessment_done){
            binding.btnViewResponsesLink.apply {
                text = getString(R.string.btn_view_responses_link_text)
                setOnClickListener {
                    redirectToResponses()
                }
            }
        } else {
            binding.btnViewResponsesLink.apply {
                setOnClickListener {
                    redirectToAssessment()
                }
            }
        }
    }

    private fun redirectToAssessment(){
        view?.findNavController()?.navigate(R.id.action_fragment_home_to_fragment_assessment_questions)
    }

    private fun redirectToResponses(){
        view?.findNavController()?.navigate(R.id.action_fragment_home_to_fragment_assessment_responses)
    }

    companion object{
        val TAG = HomeFragment::class.simpleName
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}