package com.hospitalfrontend.model

data class CreateCare(
    val patient: PatientId?,
    val nurse: NurseId?,
    val systolic_bp: Int?,
    val diastolic_bp: Int?,
    val respiratory_rate: Int?,
    val pulse: Int?,
    val body_temperature: Double?,
    val oxygen_saturation: Double?,
    val drainage_type: String?,
    val drainage_debit: Int?,
    val hygine_type: String?,
    val diet_texture: String?,
    val diet_type: String?,
    val diet_autonomy: String?,
    val prosthesis: String?,
    val sedation: String?,
    val ambulation: String?,
    val postural_changes: String?,
    val recorded_at: String?,
    val note: String?
)

data class PatientId(
    val id_patient: Int?
)

data class NurseId(
    val id: Int?
)
