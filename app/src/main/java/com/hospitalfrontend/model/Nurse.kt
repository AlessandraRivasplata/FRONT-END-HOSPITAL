package com.hospitalfrontend.model

data class Nurse(
    val id: Int,
    val name: String,
    val password: String,
    val username: String,
    val profileImage: String? = null
)
data class NurseResponse(
    val nurses: List<Nurse>
)