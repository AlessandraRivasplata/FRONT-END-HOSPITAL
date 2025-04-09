package com.hospitalfrontend.model

import com.google.gson.annotations.SerializedName

data class Patient(
    @SerializedName("id_patient") val idPatient: Int,
    val name: String,
    val surname: String,
    @SerializedName("birth_date") val birthDate: String, // Se puede cambiar a LocalDate si se maneja correctamente en la UI
    val address: String,
    val language: String,
    @SerializedName("medical_history") val medicalHistory: String,
    val allergies: String,
    val hygiene: String,
    val mobilizations: String,
    val posturalChanges: String,
    val sittingTolerance: String,
    val observations: String,
    @SerializedName("familiar_info") val familiarInfo: String,
    val idDiagnosis: Int?,
    val idRoom: Int?
) {
}

data class PatientResponse(
    val data: List<Patient>
) {


}
data class PatientResponseById(
    val room: Room,
    @SerializedName("id_patient") val idPatient: Int,
    val name: String,
    val surname: String,
    @SerializedName("birth_date") val birthDate: String,
    val address: String,
    val language: String,
    @SerializedName("medical_history") val medicalHistory: String,
    val allergies: String,
    val hygiene: String,
    @SerializedName("familiar_info") val familiarInfo: String
)
