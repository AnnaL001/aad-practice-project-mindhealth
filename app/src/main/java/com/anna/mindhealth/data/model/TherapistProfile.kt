package com.anna.mindhealth.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TherapistProfile(
    var short_desc: String = "",
    var country: String = "",
    var gender: String = "",
    var concerns: ArrayList<String> = arrayListOf(),
    var helping_approach: String = "",
    var services_provided: ArrayList<String> = arrayListOf(),
    var working_ages: HashMap<String, Boolean> = hashMapOf(),
    var languages: HashMap<String, Boolean> = hashMapOf(),
    var office_address: String = ""
): Parcelable