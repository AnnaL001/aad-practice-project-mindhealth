package com.anna.mindhealth.ui.patient.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.anna.mindhealth.R
import com.anna.mindhealth.base.Utility.PATIENT_ROLE
import com.anna.mindhealth.base.Utility.setImageViewResource
import com.anna.mindhealth.base.Utility.setTextViewValues
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

        homeViewModel.patientReference?.get()?.addOnCompleteListener { task ->
            val patient = task.result.toObject<Patient>()

            if (patient != null){
                if (patient.security_level == PATIENT_ROLE){
                    binding.root.visibility = View.VISIBLE
                }

                setTextViewValues(textView = binding.txvWelcomeUser, textValue = getString(R.string.txv_welcome_user_text, patient.name))
                initializeAssessmentStatus(patient)
                initializeTherapistSelectionStatus(patient)
            }
        }
    }

    private fun updateAssessmentDetails(){
        binding.imvAssignmentStatus.apply {
            setImageViewResource(imageView = this, resId = R.drawable.ic_outline_assignment_turned_in_24)
            contentDescription = getString(R.string.icon_assessment_status_true)
        }
    }


    private fun initializeAssessmentStatus(patient: Patient){
        when(patient.is_assessment_done){
            true -> {
                binding.btnViewResponsesLink.apply {
                    text = getString(R.string.btn_view_responses_link_text)
                    setOnClickListener {
                        redirectToResponses()
                    }
                }
                updateAssessmentDetails()
            }
            false -> {
                binding.btnViewResponsesLink.apply {
                    setOnClickListener {
                        redirectToAssessment()
                    }
                }
            }
        }
    }

    private fun updateTherapistSelectionDetails(){
        setTextViewValues(binding.txvTherapistSelectionState, getString(R.string.therapist_selection_true))
    }
    private fun initializeTherapistSelectionStatus(patient: Patient){
        when(patient.is_therapist_selected){
            true -> {
                updateTherapistSelectionDetails()

                binding.btnTherapistSelectionLink.apply {
                    text = getString(R.string.btn_therapist_selection_true)
                    setOnClickListener {

                    }
                }
            }

            false -> {
                binding.btnTherapistSelectionLink.apply {
                    setOnClickListener {
                        redirectToTherapistList()
                    }
                }
            }
        }
    }

    private fun redirectToAssessment(){
        view?.findNavController()?.navigate(R.id.action_fragment_to_fragment_assessment_questions)
    }

    private fun redirectToResponses(){
        view?.findNavController()?.navigate(R.id.action_fragment_to_fragment_assessment_responses)
    }

    private fun redirectToTherapistList(){
        requireView().findNavController().navigate(R.id.action_fragment_to_fragment_therapist_list)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}