package com.anna.mindhealth.data.model

data class User(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val avatar: String = "",
    val assessment_done: Boolean = false,
    val therapist_selected: Boolean = false,
    val security_level: Int = 0 // Not set
)