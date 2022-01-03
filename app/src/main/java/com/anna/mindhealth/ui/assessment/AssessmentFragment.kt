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

        initializeButton()

    }

    private fun initializeButton(){
        binding.btnSaveChoices.setOnClickListener {
            // Question 1
            val selectedGender = getRadioSelectedItem(binding.radioGrpGender)
            val genderQuestion = binding.txvAssessmentQuestion1.text.toString()

            // Question 2
            val selectedAge = getRadioSelectedItem(binding.radioGrpAge)
            val ageQuestion = binding.txvAssessmentQuestion2.text.toString()

            // Question 3
            val selectedRelationshipStatus = getRadioSelectedItem(binding.radioGrpRshipStatus)
            val relationshipStatusQuestion = binding.txvAssessmentQuestion3.text.toString()

            // Question 4
            val selectedSeenTherapist = getRadioSelectedItem(binding.radioGrpSeenTherapist)
            val seenTherapistQuestion = binding.txvAssessmentQuestion4.text.toString()

            // Question 5
            val selectedMentalEffect = getRadioSelectedItem(binding.radioGrpMentalEffect)
            val mentalEffectQuestion = binding.txvAssessmentQuestion5.text.toString()

            // Question 6
            val selectedPhysicalState = getRadioSelectedItem(binding.radioGrpPhysicalHealth)
            val physicalStateQuestion = binding.txvAssessmentQuestion6.text.toString()

            // Question 7
            val selectedEatingHabit = getRadioSelectedItem(binding.radioGrpEatingHabits)
            val eatingHabitQuestion = binding.txvAssessmentQuestion7.text.toString()

            // Question 8
            val selectedFinancialStatus = getRadioSelectedItem(binding.radioGrpFinances)
            val financialStateQuestion = binding.txvAssessmentQuestion8.text.toString()

            //Question 9
            val selectedLanguage = getRadioSelectedItem(binding.radioGrpPrefLang)
            val languageQuestion = binding.txvAssessmentQuestion9.text.toString()

            //Question10
            val selectedCountry = getSelectedSpinnerItem(binding.spinnerCountries)
            val countryQuestion = binding.txvAssessmentQuestion10.text.toString()

            //Question 11
            val selectedOptions = getSelectedCheckboxes(listOf(
                binding.checkboxCouplesTherapy, binding.checkboxGroupTherapy, binding.checkboxIndividualTherapy
            ))
            val optionsQuestion = binding.txvAssessmentQuestion11.text.toString()


            val responses = hashMapOf(
                genderQuestion to selectedGender,
                ageQuestion to selectedAge,
                relationshipStatusQuestion to selectedRelationshipStatus,
                seenTherapistQuestion to selectedSeenTherapist,
                mentalEffectQuestion to selectedMentalEffect,
                physicalStateQuestion to selectedPhysicalState,
                eatingHabitQuestion to selectedEatingHabit,
                financialStateQuestion to selectedFinancialStatus,
                languageQuestion to selectedLanguage,
                countryQuestion to selectedCountry,
                optionsQuestion to selectedOptions
            )

            Log.d(TAG, "$responses")

//            val assessment = Assessment(title = getString(R.string.assessment_title), responses = responses)
//            assessmentViewModel.insertAssessmentResponses(assessment)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        val TAG = AssessmentFragment::class.simpleName
    }
}