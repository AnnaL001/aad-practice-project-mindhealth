package com.anna.mindhealth.data.model

data class Assessment(
    val title: String = "",
    val responses: HashMap<String, String> = HashMap()
)