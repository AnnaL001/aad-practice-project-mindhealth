package com.anna.mindhealth.data.`interface`

import com.anna.mindhealth.data.model.TherapistProfile

interface TherapistProfileRepo {
    fun update(therapistProfile: TherapistProfile)
}
