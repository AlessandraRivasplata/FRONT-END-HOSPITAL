package com.hospitalfrontend.model

import com.google.gson.annotations.SerializedName

data class Care(
    @SerializedName("id_care")
    val idCare: Int,

    @SerializedName("systolic_bp")
    val systolicBp: Int,

    @SerializedName("diastolic_bp")
    val diastolicBp: Int,

    @SerializedName("respiratory_rate")
    val respiratoryRate: Int,

    val pulse: Int,

    @SerializedName("body_temperature")
    val bodyTemperature: Double,

    @SerializedName("oxygen_saturation")
    val oxygenSaturation: Double,

    @SerializedName("drainage_type")
    val drainageType: String,

    @SerializedName("drainage_debit")
    val drainageDebit: Int,

    @SerializedName("hygine_type") // usa esto si el JSON tiene el typo
    val hygieneType: String,

    val sedation: String,
    val ambulation: String,

    @SerializedName("postural_changes")
    val posturalChanges: String,

    @SerializedName("recorded_at")
    val recordedAt: String, // o Date con adaptador

    val note: String,

    val nurse: Nurse,
    val patient: Patient
)

data class CareResponse(
    val data: List<Care>
)
