package com.hospitalfrontend.model

import com.google.gson.annotations.SerializedName

data class Care(
    @SerializedName("id_care")
    val idCare: Int,

    val nurse: Nurse,
    val patient: Patient,

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
    val drainageType: String?,

    @SerializedName("drainage_debit")
    val drainageDebit: Int?,

    @SerializedName("hygine_type")
    val hygieneType: String?,

    val sedation: String?,
    val ambulation: String?,

    @SerializedName("postural_changes")
    val posturalChanges: String?,

    @SerializedName("recorded_at")
    val recordedAt: String,

    val note: String?,

    // Nuevos campos añadidos en el back:
    @SerializedName("diet_texture")
    val dietTexture: String?,

    @SerializedName("diet_type")
    val dietType: String?,

    @SerializedName("diet_autonomy")
    val dietAutonomy: String?,

    val prosthesis: Boolean?,
    val room: Room? = null
)


data class CareResponse(
    val data: List<Care>
)
