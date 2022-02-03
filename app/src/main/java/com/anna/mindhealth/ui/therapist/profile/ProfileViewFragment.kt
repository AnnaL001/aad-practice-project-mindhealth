package com.anna.mindhealth.ui.therapist.profile

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.anna.mindhealth.R
import com.anna.mindhealth.base.Utility.setImageViewResource
import com.anna.mindhealth.base.Utility.setTextViewValues
import com.anna.mindhealth.data.model.Therapist
import com.anna.mindhealth.databinding.FragmentTherapyProfileBinding
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.firestore.ktx.toObject

class ProfileViewFragment: Fragment() {
    private var _binding: FragmentTherapyProfileBinding ?= null
    private val binding get() = _binding!!

    private lateinit var therapyProfileViewModel: TherapyProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTherapyProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        therapyProfileViewModel = ViewModelProvider(this).get(TherapyProfileViewModel::class.java)

        initializeButtons()
        initializeGeneralInfo()
        initializeTherapyProfile()
    }

    private fun initializeGeneralInfo(){
        therapyProfileViewModel.therapistReference?.get()?.addOnCompleteListener { task ->
            val therapist = task.result.toObject<Therapist>()!!

            Glide.with(requireContext())
                .asBitmap().load(Uri.parse(therapist.avatar))
                .placeholder(R.drawable.ic_baseline_account_circle_24)
                .into(binding.imvAvatar)

            setTextViewValues(binding.txvNameProfile, therapist.name)
            setTextViewValues(binding.txvPhoneProfile, therapist.phone_no)
            setTextViewValues(binding.txvRatingNoProfile, therapist.rating.toString())
        }
    }

    private fun initializeTherapyProfile(){
        therapyProfileViewModel.therapistReference?.get()?.addOnCompleteListener { task ->
            @Suppress("UNCHECKED_CAST")
            val therapyProfile = therapyProfileViewModel.getTherapyProfile(task.result.data?.get("profile") as HashMap<String, Any>)

            setGenderImageView(binding.imvGenderIcon, therapyProfile.gender)
            setCountryImageView(binding.imvCountryIcon, therapyProfile.country)
            setTextViewValues(binding.txvShortDescSaved, therapyProfile.short_desc)
            setChipTextValues(binding.chipGrpConcernsSaved, therapyProfile.concerns)
            setChipTextValues(binding.chipGrpServicesSaved, therapyProfile.services_provided)
            setTextViewValues(binding.txvHelpingApproachSaved, therapyProfile.helping_approach)
            setTextViewValues(binding.txvPhysicalAddressSaved, therapyProfile.office_address)

            if(therapyProfile.working_ages.isNotEmpty() || therapyProfile.languages.isNotEmpty()){
                binding.vsPreferences.inflate()
                val txvAges = requireView().findViewById<MaterialTextView>(R.id.txv_ages_preference_saved)
                val txvLang = requireView().findViewById<MaterialTextView>(R.id.txv_lang_preference_saved)
                val txvValAge = listOf(R.string.child_age, R.string.young_adult, R.string.middle_aged, R.string.old_aged)
                val txvValLang = listOf(R.string.lang_eng, R.string.lang_swa, R.string.lang_fre)

                setAgePrefCheckBoxValue(txvAges, txvValAge, therapyProfile.working_ages)
                setLanguageCheckBoxValue(txvLang, txvValLang, therapyProfile.languages)
            }

        }
    }

    private fun initializeButtons(){
        binding.btnEditProfileLink.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_fragment_to_fragment_edit_profile)
        }

        binding.btnUpdatePersonalInfoLink.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_fragment_to_fragment_personal_info)
        }
    }

    private fun setLanguageCheckBoxValue(textView: MaterialTextView, textValues: List<Int>, preferences: HashMap<String, Boolean>){
        var no = 1
        var displayLangTxt = ""
        textValues.forEach { stringRes ->
            if (preferences[getString(stringRes)] == true){
                displayLangTxt += getString(R.string.numbered_list, no++, getString(stringRes))
                textView.text = displayLangTxt
            }
        }
    }

    private fun setAgePrefCheckBoxValue(textView: MaterialTextView, textValues: List<Int>, preferences: HashMap<String, Boolean>){
        var no = 1
        var displayAgeTxt = ""
        textValues.forEach { stringRes ->
            val ageGroup = getString(stringRes)
            if (preferences[ageGroup] == true){
                displayAgeTxt += getString(R.string.numbered_list, no++, getAgeGroup(ageGroup))
                textView.text = displayAgeTxt
            }
        }
    }

    private fun getAgeGroup(textValue: String): String{
        var ageGroup = ""
        when(textValue){
            getString(R.string.child_age) -> ageGroup = GROUP_CHILDREN
            getString(R.string.young_adult) -> ageGroup = GROUP_YOUTH
            getString(R.string.middle_aged) -> ageGroup = GROUP_MIDDLE_AGED
            getString(R.string.old_aged) -> ageGroup = GROUP_OLD_AGED
        }
        return ageGroup
    }

    private fun setChipTextValues(chipGroup: ChipGroup, textValues: List<String>){
        textValues.forEach { item ->
            val chip = Chip(requireContext())
            chip.text = item
            chipGroup.addView(chip)
        }
    }

    private fun setCountryImageView(imageView: ShapeableImageView, textValue: String){
        when(textValue){
            COUNTRY_KENYA -> setImageViewResource(imageView, R.drawable.kenya)
            COUNTRY_UGANDA -> setImageViewResource(imageView, R.drawable.uganda)
            COUNTRY_TANZANIA -> setImageViewResource(imageView, R.drawable.tanzania)
        }
    }

    private fun setGenderImageView(imageView: ShapeableImageView, textValue: String){
        when(textValue){
            GENDER_MALE -> setImageViewResource(imageView, R.drawable.ic_baseline_male_24)
            GENDER_FEMALE -> setImageViewResource(imageView, R.drawable.ic_baseline_female_24)
            GENDER_OTHER -> setImageViewResource(imageView, R.drawable.ic_baseline_transgender_24)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        val TAG = ProfileViewFragment::class.simpleName
        const val COUNTRY_KENYA = "Kenya"
        const val COUNTRY_UGANDA = "Uganda"
        const val COUNTRY_TANZANIA = "Tanzania"
        const val GENDER_MALE = "Male"
        const val GENDER_FEMALE = "Female"
        const val GENDER_OTHER = "Other"
        const val GROUP_CHILDREN = "Children"
        const val GROUP_YOUTH = "Young adults"
        const val GROUP_MIDDLE_AGED = "Middle aged adults"
        const val GROUP_OLD_AGED = "Old aged adults"
    }
}