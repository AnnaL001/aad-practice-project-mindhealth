package com.anna.mindhealth.ui.assessment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.anna.mindhealth.R
import com.anna.mindhealth.data.model.Assessment
import com.anna.mindhealth.databinding.FragmentAssessmentResponsesBinding
import com.google.firebase.firestore.ktx.toObject

class AssessmentResponsesFragment: Fragment() {
    private var _binding: FragmentAssessmentResponsesBinding ?= null
    private lateinit var assessmentViewModel: AssessmentViewModel

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAssessmentResponsesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        assessmentViewModel = ViewModelProvider(this).get(AssessmentViewModel::class.java)

        assessmentViewModel.assessmentResponses.observe(viewLifecycleOwner, { assessmentRef ->
            assessmentRef.get().addOnCompleteListener { task ->
                val assessment = task.result.toObject<Assessment>()!!
                val assessmentQuestions = assessment.responses.keys.toTypedArray()

                // Assessment Response 1
                binding.txvAssessmentQuestion1.text = assessmentQuestions[0]
                binding.txvAssessmentResponse1.text = assessment.responses[assessmentQuestions[0]]

                // Assessment Response 2
                binding.txvAssessmentQuestion2.text = assessmentQuestions[1]
                binding.txvAssessmentResponse2.text = assessment.responses[assessmentQuestions[1]]

                // Assessment Response 3
                binding.txvAssessmentQuestion3.text = assessmentQuestions[2]
                binding.txvAssessmentResponse3.text = assessment.responses[assessmentQuestions[2]]

                // Assessment Response 4
                binding.txvAssessmentQuestion4.text = assessmentQuestions[3]
                binding.txvAssessmentResponse4.text = assessment.responses[assessmentQuestions[3]]

                // Assessment Response 5
                binding.txvAssessmentQuestion5.text = assessmentQuestions[4]
                binding.txvAssessmentResponse5.text = assessment.responses[assessmentQuestions[4]]

                // Assessment Response 6
                binding.txvAssessmentQuestion6.text = assessmentQuestions[5]
                binding.txvAssessmentResponse6.text = assessment.responses[assessmentQuestions[5]]

                // Assessment Response 7
                binding.txvAssessmentQuestion7.text = assessmentQuestions[6]
                binding.txvAssessmentResponse7.text = assessment.responses[assessmentQuestions[6]]

                // Assessment Response 8
                binding.txvAssessmentQuestion8.text = assessmentQuestions[7]
                binding.txvAssessmentResponse8.text = assessment.responses[assessmentQuestions[7]]

                // Assessment Response 9
                binding.txvAssessmentQuestion9.text = assessmentQuestions[8]
                binding.txvAssessmentResponse9.text = assessment.responses[assessmentQuestions[8]]

                // Assessment Response 10
                binding.txvAssessmentQuestion10.text = assessmentQuestions[9]
                binding.txvAssessmentResponse10.text = assessment.responses[assessmentQuestions[9]]

                // Assessment Response 11
                binding.txvAssessmentQuestion11.text = assessmentQuestions[10]
                binding.txvAssessmentResponse11.text = assessment.responses[assessmentQuestions[10]]
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}