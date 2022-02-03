package com.anna.mindhealth.ui.therapist.profile

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import com.anna.mindhealth.base.Utility.THERAPIST_ROLE
import com.anna.mindhealth.data.`interface`.UserRepo
import com.anna.mindhealth.data.model.Therapist
import com.anna.mindhealth.data.model.TherapistProfile
import com.anna.mindhealth.data.repository.TherapistProfileRepository
import com.anna.mindhealth.data.repository.UserRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.ktx.Firebase

class TherapyProfileViewModel(application: Application): AndroidViewModel(application) {
    private val therapistProfileRepository: TherapistProfileRepository
    private val userRepository: UserRepo
    val therapistReference: DocumentReference?

    init {
        therapistProfileRepository = TherapistProfileRepository(application)
        userRepository = UserRepository(application)
        therapistReference = userRepository.read(Firebase.auth.currentUser!!.uid, THERAPIST_ROLE)
    }

    fun updateProfile(therapistProfile: TherapistProfile){
        therapistProfileRepository.update(therapistProfile)
    }

    fun updatePersonalInfo(therapist: Therapist, securityLevel: Int, bitmap: Bitmap){
        userRepository.update(therapist, securityLevel, bitmap)
    }

    fun getTherapyProfile(hashMap: HashMap<String, Any>): TherapistProfile{
        return TherapistProfile().apply {
            this.short_desc = hashMap["short_desc"].toString()
            this.country = hashMap["country"].toString()
            this.gender = hashMap["gender"].toString()
            @Suppress("UNCHECKED_CAST")
            this.concerns = hashMap["concerns"] as ArrayList<String>
            this.helping_approach = hashMap["helping_approach"].toString()
            @Suppress("UNCHECKED_CAST")
            this.services_provided = hashMap["services_provided"] as ArrayList<String>
            @Suppress("UNCHECKED_CAST")
            this.working_ages = hashMap["working_ages"] as HashMap<String, Boolean>
            @Suppress("UNCHECKED_CAST")
            this.languages = hashMap["languages"] as HashMap<String, Boolean>
            this.office_address = hashMap["office_address"].toString()
        }
    }
}