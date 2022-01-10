package com.anna.mindhealth.data.model

data class Therapist(
    val id: String = "",
    val name: String = "",
    val email:String = "",
    val avatar: String = "",
    @field:JvmField // Boolean field prefixed with 'is'
    val is_vetted: Boolean = false,
    val rating: Int = 0,
    val security_level: Int = 0
)