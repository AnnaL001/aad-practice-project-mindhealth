package com.anna.mindhealth.data.model

data class User(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val avatar: String = "",
    @field:JvmField // Boolean field prefixed with 'is'
    val is_assessment_done: Boolean = false,
    @field:JvmField // Boolean field prefixed with 'is'
    val is_therapist_selected: Boolean = false,
    val security_level: Int = 0 // Not set
)