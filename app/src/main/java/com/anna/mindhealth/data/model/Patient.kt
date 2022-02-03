package com.anna.mindhealth.data.model

data class Patient(
    val id: String = "",
    val email: String = "",
    var phone_no: String = "",
    var name: String = "",
    var avatar: String = "",
    @field:JvmField // Boolean field prefixed with 'is'
    var is_assessment_done: Boolean = false,
    @field:JvmField // Boolean field prefixed with 'is'
    var is_therapist_selected: Boolean = false,
    var therapist: String = "",
    val security_level: Int = 0 // Not set
)