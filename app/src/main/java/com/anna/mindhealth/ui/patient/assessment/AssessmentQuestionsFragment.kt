package com.anna.mindhealth.ui.patient.assessment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.anna.mindhealth.R
import com.anna.mindhealth.base.BaseFragment
import com.anna.mindhealth.data.model.Assessment
import com.anna.mindhealth.databinding.FragmentAssessmentQuestionsBinding

class AssessmentQuestionsFragment: BaseFragment(){
    private var _binding: FragmentAssessmentQuestionsBinding ?= null
    private lateinit var assessmentViewModel: AssessmentViewModel

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAssessmentQuestionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        assessmentViewModel = ViewModelProvider(this).get(AssessmentViewModel::class.java)

        initializeButtons()

    }

    private fun initializeButtons(){
        binding.btnSaveChoices.setOnClickListener {
            // Retrieve assessment responses
            val responses = hashMapOf(
                binding.txvAssessmentQuestion1.text.toString() to getRadioSelectedItem(binding.radioGrpGender, binding.root),
                binding.txvAssessmentQuestion2.text.toString() to getRadioSelectedItem(binding.radioGrpAge, binding.root),
                binding.txvAssessmentQuestion3.text.toString() to getRadioSelectedItem(binding.radioGrpRshipStatus, binding.root),
                binding.txvAssessmentQuestion4.text.toString() to getRadioSelectedItem(binding.radioGrpSeenTherapist, binding.root),
                binding.txvAssessmentQuestion5.text.toString() to getRadioSelectedItem(binding.radioGrpMentalEffect, binding.root),
                binding.txvAssessmentQuestion6.text.toString() to getRadioSelectedItem(binding.radioGrpPhysicalHealth, binding.root),
                binding.txvAssessmentQuestion7.text.toString() to getRadioSelectedItem(binding.radioGrpEatingHabits, binding.root),
                binding.txvAssessmentQuestion8.text.toString() to getRadioSelectedItem(binding.radioGrpFinances, binding.root),
                binding.txvAssessmentQuestion9.text.toString() to getRadioSelectedItem(binding.radioGrpPrefLang, binding.root),
                binding.txvAssessmentQuestion10.text.toString() to getSelectedSpinnerItem(binding.spinnerCountries),
                binding.txvAssessmentQuestion11.text.toString() to
                        getSelectedCheckboxesInString(listOf(
                    binding.checkboxCouplesTherapy, binding.checkboxGroupTherapy, binding.checkboxIndividualTherapy
                ))
            )

            Log.d(TAG, "$responses")

            // Insert assessment responses and redirect to home
            val assessment = Assessment(title = getString(R.string.assessment_title), responses = responses)
            assessmentViewModel.insertAssessmentResponses(assessment)
            redirectToHome()
        }
    }



    private fun redirectToHome(){
        view?.findNavController()?.navigate(R.id.action_fragment_to_fragment_home)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        val TAG = AssessmentQuestionsFragment::class.simpleName
    }
}