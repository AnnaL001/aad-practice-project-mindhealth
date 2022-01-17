package com.anna.mindhealth.data.model

import android.net.Uri

data class Therapist(
    val id: String = "",
    val name: String = "",
    val email:String = "",
    val avatar: String = "",
    val resume: String = "",
    @field:JvmField // Boolean field prefixed with 'is'
    val is_available: Boolean = false,
    @field:JvmField // Boolean field prefixed with 'is'
    val is_vetted: Boolean = false,
    val rating: Int = 0,
    val rate: Double = 0.0,
    val security_level: Int = 0
)