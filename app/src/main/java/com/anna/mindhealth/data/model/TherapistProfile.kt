package com.anna.mindhealth.data.model

data class TherapistProfile(
    val shortDesc: String = "",
    val country: String = "",
    val gender: String = "",
    val concerns: String = "",
    val helpingApproach: String = "",
    val servicesProvided: String = "",
    val workingAges: HashMap<String, Boolean> = hashMapOf(),
    val languages: HashMap<String, Boolean> = hashMapOf(),
    val officeAddress: String = ""
)