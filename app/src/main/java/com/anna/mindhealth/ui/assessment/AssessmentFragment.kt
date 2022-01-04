package com.anna.mindhealth.ui.assessment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.anna.mindhealth.R
import com.anna.mindhealth.data.model.Assessment
import com.anna.mindhealth.databinding.FragmentAssessmentQuestionsBinding

class AssessmentFragment: Fragment(){
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
                binding.txvAssessmentQuestion1.text.toString() to getRadioSelectedItem(binding.radioGrpGender),
                binding.txvAssessmentQuestion2.text.toString() to getRadioSelectedItem(binding.radioGrpAge),
                binding.txvAssessmentQuestion3.text.toString() to getRadioSelectedItem(binding.radioGrpRshipStatus),
                binding.txvAssessmentQuestion4.text.toString() to getRadioSelectedItem(binding.radioGrpSeenTherapist),
                binding.txvAssessmentQuestion5.text.toString() to getRadioSelectedItem(binding.radioGrpMentalEffect),
                binding.txvAssessmentQuestion6.text.toString() to getRadioSelectedItem(binding.radioGrpPhysicalHealth),
                binding.txvAssessmentQuestion7.text.toString() to getRadioSelectedItem(binding.radioGrpEatingHabits),
                binding.txvAssessmentQuestion8.text.toString() to getRadioSelectedItem(binding.radioGrpFinances),
                binding.txvAssessmentQuestion9.text.toString() to getRadioSelectedItem(binding.radioGrpPrefLang),
                binding.txvAssessmentQuestion10.text.toString() to getSelectedSpinnerItem(binding.spinnerCountries),
                binding.txvAssessmentQuestion11.text.toString() to
                        getSelectedCheckboxes(listOf(
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


    private fun getRadioSelectedItem(radioGroup: RadioGroup): String {
        val checkedRadioBtn = binding.root.findViewById<RadioButton>(radioGroup.checkedRadioButtonId)
        return checkedRadioBtn.text.toString()
    }

    private fun getSelectedSpinnerItem(spinner: Spinner): String {
        return spinner.selectedItem.toString()
    }

    private fun getSelectedCheckboxes(checkBoxes: List<CheckBox>): String {
        val selectedItems = ArrayList<String>()

        checkBoxes.forEach { checkBox ->
            if (checkBox.isChecked){
                selectedItems.add(checkBox.text.toString())
            }
        }

        return selectedItems.joinToString()
    }

    private fun redirectToHome(){
        view?.findNavController()?.navigate(R.id.action_fragment_assessment_questions_to_fragment_home)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        val TAG = AssessmentFragment::class.simpleName
    }
}