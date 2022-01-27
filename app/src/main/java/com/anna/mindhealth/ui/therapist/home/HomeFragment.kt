package com.anna.mindhealth.ui.therapist.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.anna.mindhealth.R
import com.anna.mindhealth.base.Utility.THERAPIST_ROLE
import com.anna.mindhealth.base.Utility.setImageViewResource
import com.anna.mindhealth.base.Utility.setTextViewValues
import com.anna.mindhealth.data.`interface`.OnClickAvailabilityDialogListener
import com.anna.mindhealth.data.model.Therapist
import com.anna.mindhealth.databinding.FragmentHomeTherapistBinding
import com.anna.mindhealth.dialog.UpdateAvailabilityDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class HomeFragment: Fragment(), OnClickAvailabilityDialogListener {
    private var _binding: FragmentHomeTherapistBinding ?= null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeTherapistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        homeViewModel.therapistReference?.get()?.addOnCompleteListener { task ->
            val therapist = task.result.toObject<Therapist>()

            if (therapist!!.security_level == THERAPIST_ROLE && therapist.is_vetted){
                binding.root.visibility = View.VISIBLE
            }

            setTextViewValues(textView = binding.txvGreetingsUser, textValue = getString(R.string.txv_greetings_user_text, therapist.name))
            setAvailability(therapist.is_available)
        }

        initializeButtons()
    }

    private fun setAvailability(availability: Boolean){
        when(availability){
            true -> {
                setTextViewValues(textView = binding.txvSetAvailability, textValue = getString(R.string.txv_availability_set_text, "Yes"))
                setImageViewResource(imageView = binding.imvAvailabilityIcon, resId = R.drawable.ic_baseline_check_24)
            }
            false -> {
                setTextViewValues(textView = binding.txvSetAvailability, textValue = getString(R.string.txv_availability_set_text, "No"))
                setImageViewResource(imageView = binding.imvAvailabilityIcon, resId = R.drawable.ic_baseline_clear_24)
            }
        }
    }

    private fun initializeButtons(){
        binding.btnUpdateProfileLink.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_fragment_to_fragment_edit_profile)
        }

        binding.btnUpdateAvailabilityLink.setOnClickListener {
            UpdateAvailabilityDialog().show(childFragmentManager, UpdateAvailabilityDialog.TAG)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onDialogPositiveClick(id: String) {
        val therapist = Therapist(id = id, is_available = true)
        homeViewModel.updateTherapistAvailability(therapist)
        setAvailability(therapist.is_available)
    }

    override fun onDialogNegativeClick(id: String) {
        val therapist = Therapist(id = id, is_available = false)
        homeViewModel.updateTherapistAvailability(therapist)
        setAvailability(therapist.is_available)
    }
}