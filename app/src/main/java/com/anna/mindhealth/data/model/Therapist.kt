package com.anna.mindhealth.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Therapist(
    var id: String = "",
    var name: String = "",
    var email:String = "",
    var phone_no: String = "",
    var avatar: String = "",
    var resume: String = "",
    @field:JvmField // Boolean field prefixed with 'is'
    var is_available: Boolean = false,
    @field:JvmField // Boolean field prefixed with 'is'
    var is_vetted: Boolean = false,
    var rating: Int = 0,
    var rate: Double = 0.0,
    var security_level: Int = 0,
    var profile: TherapistProfile = TherapistProfile()
): Parcelable