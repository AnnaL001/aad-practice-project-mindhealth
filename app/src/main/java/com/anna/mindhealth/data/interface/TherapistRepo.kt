package com.anna.mindhealth.data.`interface`

import com.anna.mindhealth.data.model.Assessment
import com.anna.mindhealth.data.model.Therapist
import com.google.firebase.firestore.Query

interface TherapistRepo {
    fun retrieveCuratedTherapistList(assessment: Assessment): Query?
    fun updateTherapistAvailability(therapist: Therapist)
}