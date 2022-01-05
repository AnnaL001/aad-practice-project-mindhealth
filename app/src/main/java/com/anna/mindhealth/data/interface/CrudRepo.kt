package com.anna.mindhealth.data.`interface`

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.DocumentReference

interface CrudRepo {
    fun insert(data: Any)
    fun read(id: String): LiveData<DocumentReference>
}