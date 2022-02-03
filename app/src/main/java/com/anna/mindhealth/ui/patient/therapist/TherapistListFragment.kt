package com.anna.mindhealth.ui.patient.therapist

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.anna.mindhealth.R
import com.anna.mindhealth.base.Utility.shortToastMessage
import com.anna.mindhealth.data.model.Assessment
import com.anna.mindhealth.data.model.Therapist
import com.anna.mindhealth.databinding.FragmentTherapistListBinding
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects

class TherapistListFragment: Fragment() {
    private var _binding: FragmentTherapistListBinding ?= null
    private val binding get() = _binding!!
    private val therapistListAdapter = TherapistListRecyclerAdapter()
    private lateinit var therapistListViewModel: TherapistListViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTherapistListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        therapistListViewModel = ViewModelProvider(this).get(TherapistListViewModel::class.java)

        therapistListViewModel.assessmentReference.get().addOnCompleteListener { task ->
            if (task.isSuccessful){
                val assessment = task.result.toObject<Assessment>()

                if (assessment != null){
                    therapistListViewModel.getTherapistList(assessment)?.get()
                        ?.addOnCompleteListener {
                            if (it.isSuccessful){
                                val therapistList = it.result.toObjects<Therapist>()
                                Log.d(TAG, "list size: ${therapistList.size}")
                                Log.d(TAG, "list: $therapistList")
                                therapistListAdapter.setTherapistList(therapistList)
                                Log.d(TAG, "adapter: ${therapistListAdapter.itemCount}")
                            }
                        }?.addOnFailureListener { e ->
                            shortToastMessage(requireContext(), getString(R.string.toast_sign_up_fail))
                            Log.d(TAG, "Error while loading therapist list", e.cause)
                        }

                } else {
                    redirectToHome()
                    shortToastMessage(requireContext(), getString(R.string.toast_take_assessment_prompt))
                }
            }

        }.addOnFailureListener { e ->
            shortToastMessage(requireContext(), getString(R.string.toast_sign_up_fail))
            Log.d(TAG, "Error while loading assessment", e.cause)
        }
        initializeRecyclerView()

    }

    private fun redirectToHome(){
        requireView().findNavController().navigate(R.id.action_fragment_to_fragment_home)
    }

    private fun initializeRecyclerView(){
        binding.rcvTherapistList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = therapistListAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        val TAG = TherapistListFragment::class.simpleName
    }
}