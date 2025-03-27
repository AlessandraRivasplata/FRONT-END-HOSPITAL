package com.hospitalfrontend.model

data class Patient(
    val id: Int = 0,
    val name: String,
    val surname: String,
    val birthDate: String, // Se puede cambiar a LocalDate si se maneja correctamente en la UI
    val address: String,
    val language: String,
    val medicalHistory: String,
    val allergies: String,
    val hygiene: String,
    val mobilizations: String,
    val posturalChanges: String,
    val sittingTolerance: String,
    val observations: String,
    val familiarInfo: String,
    val idDiagnosis: Int?,
    val idRoom: Int?
)

data class PatientResponse(
    val data: List<Patient>
)
