package com.anna.mindhealth.ui.therapist.therapy_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.anna.mindhealth.data.model.Therapist
import com.anna.mindhealth.databinding.FragmentPersonalInfoTherapistBinding
import com.google.firebase.firestore.ktx.toObject

class TherapistInfoFragment: Fragment() {
    private var _binding: FragmentPersonalInfoTherapistBinding ?= null
    private val binding get() = _binding!!

    private lateinit var therapyProfileViewModel: TherapyProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalInfoTherapistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        therapyProfileViewModel = ViewModelProvider(this).get(TherapyProfileViewModel::class.java)

        therapyProfileViewModel.therapistReference?.get()?.addOnCompleteListener { task ->
            val therapist = task.result.toObject<Therapist>()!!
            val therapistName = therapist.name.split(" ").toList()

            if (therapistName.size < 2){
                binding.edtInputFirstName.editText?.setText(therapistName[0])
            } else {
                binding.edtInputFirstName.editText?.setText(therapistName[0])
                binding.edtInputLastName.editText?.setText(therapistName[1])
            }

            binding.edtInputEmail.editText?.setText(therapist.email)
            binding.edtInputPhoneNo.editText?.setText(therapist.phone_no)
            binding.edtInputRate.editText?.setText(therapist.rate.toString())
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}