package com.anna.mindhealth.ui.therapist.therapy_profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.anna.mindhealth.R
import com.anna.mindhealth.base.BaseFragment
import com.anna.mindhealth.data.model.TherapistProfile
import com.anna.mindhealth.databinding.FragmentEditProfileBinding
import com.google.firebase.firestore.ktx.toObject

class ProfileUpdateFragment: BaseFragment() {
    private var _binding: FragmentEditProfileBinding ?= null
    private val binding get() = _binding!!

    private lateinit var profileUpdateViewModel: TherapyProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileUpdateViewModel = ViewModelProvider(this).get(TherapyProfileViewModel::class.java)

        initializeEditTextValues()
        initializeButton()
    }

    private fun initializeEditTextValues(){
        profileUpdateViewModel.therapistProfileReference?.get()?.addOnCompleteListener { task ->
            if (task.isSuccessful){
                val therapistProfile = task.result.toObject<TherapistProfile>()!!

                val workingAgesCheckboxes = listOf(binding.checkboxChildAge, binding.checkboxYoungAdult,
                        binding.checkboxMiddleAged, binding.checkboxOldAged)
                val languageCheckBoxes = listOf(binding.checkboxLangEng, binding.checkboxLangSwa, binding.checkboxLangFre)

                setEditTextValues(binding.edtInputShortDesc.editText!!, therapistProfile.shortDesc)
                setSelectedSpinnerItem(binding.spinnerCountriesTherapist, therapistProfile.country)
                setSelectedRadioButton(binding.radioGrpGenderTherapist, binding.root, therapistProfile.gender)
                setEditTextValues(binding.edtInputConcerns.editText!!, therapistProfile.concerns)
                setEditTextValues(binding.edtInputHelpingApproach.editText!!, therapistProfile.helpingApproach)
                setEditTextValues(binding.edtInputServicesProvided.editText!!, therapistProfile.servicesProvided)
                setSelectedCheckBoxes(workingAgesCheckboxes, therapistProfile.workingAges)
                setSelectedCheckBoxes(languageCheckBoxes, therapistProfile.languages)
                setEditTextValues(binding.edtInputPhysicalAddress.editText!!, therapistProfile.officeAddress)

                Log.d(TAG, "$therapistProfile")
            }
        }

    }

    private fun initializeButton(){
        binding.btnUpdateProfile.setOnClickListener {
            profileUpdateViewModel.updateProfile(
                TherapistProfile(
                    shortDesc = binding.edtInputShortDesc.editText?.text.toString(),
                    country = getSelectedSpinnerItem(binding.spinnerCountriesTherapist),
                    gender = getRadioSelectedItem(binding.radioGrpGenderTherapist, binding.root),
                    concerns = binding.edtInputConcerns.editText?.text.toString(),
                    helpingApproach = binding.edtInputHelpingApproach.editText?.text.toString(),
                    servicesProvided = binding.edtInputServicesProvided.editText?.text.toString(),
                    workingAges = getSelectedCheckboxesInHashMap(listOf(binding.checkboxChildAge, binding.checkboxYoungAdult, binding.checkboxMiddleAged, binding.checkboxOldAged)),
                    languages = getSelectedCheckboxesInHashMap(listOf(binding.checkboxLangEng, binding.checkboxLangSwa, binding.checkboxLangFre)),
                    officeAddress = binding.edtInputPhysicalAddress.editText?.text.toString()
                ))
            // redirect to profile
            redirectToTherapyProfile()
        }
    }

    private fun setEditTextValues(editText: EditText, textValue: String){
        editText.setText(textValue)
    }

    private fun redirectToTherapyProfile(){
        view?.findNavController()?.navigate(R.id.action_fragment_edit_profile_to_fragment_therapy_profile)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        val TAG = ProfileUpdateFragment::class.simpleName
    }
}