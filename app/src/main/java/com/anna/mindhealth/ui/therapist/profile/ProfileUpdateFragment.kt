package com.anna.mindhealth.ui.therapist.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.anna.mindhealth.R
import com.anna.mindhealth.base.BaseFragment
import com.anna.mindhealth.base.Utility.setEditTextValues
import com.anna.mindhealth.data.model.TherapistProfile
import com.anna.mindhealth.databinding.FragmentEditProfileBinding

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
        profileUpdateViewModel.therapistReference?.get()?.addOnCompleteListener { task ->
            if (task.isSuccessful){
                @Suppress("UNCHECKED_CAST")
                val therapistProfile = profileUpdateViewModel.getTherapyProfile(task.result.data?.get("profile") as HashMap<String, Any>)

                val workingAgesCheckboxes = listOf(binding.checkboxChildAge, binding.checkboxYoungAdult,
                        binding.checkboxMiddleAged, binding.checkboxOldAged)
                val languageCheckBoxes = listOf(binding.checkboxLangEng, binding.checkboxLangSwa, binding.checkboxLangFre)

                setEditTextValues(binding.edtInputShortDesc.editText!!, therapistProfile.short_desc)
                setSelectedSpinnerItem(binding.spinnerCountriesTherapist, therapistProfile.country)
                setSelectedRadioButton(binding.radioGrpGenderTherapist, binding.root, therapistProfile.gender)
                setEditTextValues(binding.edtInputConcerns.editText!!, therapistProfile.concerns.joinToString())
                setEditTextValues(binding.edtInputHelpingApproach.editText!!, therapistProfile.helping_approach)
                setEditTextValues(binding.edtInputServicesProvided.editText!!, therapistProfile.services_provided.joinToString())
                setSelectedCheckBoxes(workingAgesCheckboxes, therapistProfile.working_ages)
                setSelectedCheckBoxes(languageCheckBoxes, therapistProfile.languages)
                setEditTextValues(binding.edtInputPhysicalAddress.editText!!, therapistProfile.office_address)


                Log.d(TAG, "$therapistProfile")
            }
        }

    }


    private fun initializeButton(){
        binding.btnUpdateProfile.setOnClickListener {
            profileUpdateViewModel.updateProfile(
                TherapistProfile(
                    short_desc = binding.edtInputShortDesc.editText?.text.toString(),
                    country = getSelectedSpinnerItem(binding.spinnerCountriesTherapist),
                    gender = getRadioSelectedItem(binding.radioGrpGenderTherapist, binding.root),
                    concerns = binding.edtInputConcerns.editText?.text.toString().split(",").toMutableList() as ArrayList<String>,
                    helping_approach = binding.edtInputHelpingApproach.editText?.text.toString(),
                    services_provided = binding.edtInputServicesProvided.editText?.text.toString().split(",").toMutableList() as ArrayList<String>,
                    working_ages = getSelectedCheckboxesInHashMap(listOf(binding.checkboxChildAge, binding.checkboxYoungAdult, binding.checkboxMiddleAged, binding.checkboxOldAged)),
                    languages = getSelectedCheckboxesInHashMap(listOf(binding.checkboxLangEng, binding.checkboxLangSwa, binding.checkboxLangFre)),
                    office_address = binding.edtInputPhysicalAddress.editText?.text.toString()
                ))
            // redirect to profile
            redirectToTherapyProfile()
        }
    }


    private fun redirectToTherapyProfile(){
        view?.findNavController()?.navigate(R.id.action_fragment_to_fragment_therapy_profile)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        val TAG = ProfileUpdateFragment::class.simpleName
    }
}