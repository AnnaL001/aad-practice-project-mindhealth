package com.anna.mindhealth.data.model

data class Patient(
    val id: String = "",
    val email: String = "",
    val phone_no: String = "",
    val name: String = "",
    val avatar: String = "",
    @field:JvmField // Boolean field prefixed with 'is'
    val is_assessment_done: Boolean = false,
    @field:JvmField // Boolean field prefixed with 'is'
    val is_therapist_selected: Boolean = false,
    val therapist: String = "",
    val security_level: Int = 0 // Not set
)