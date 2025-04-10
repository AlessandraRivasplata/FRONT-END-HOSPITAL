package com.hospitalfrontend.model

import com.google.gson.annotations.SerializedName

data class Diagnosis(
    @SerializedName("id_diagnosis") val idDiagnosis: Int,
    @SerializedName("degree_of_dependence") val degreeOfDependence: String,
    @SerializedName("oxygen_carrier") val oxygenCarrier: Boolean,
    @SerializedName("oxygen_carrier_observations") val oxygenCarrierObservations: String?,
    @SerializedName("diaper_carrier") val diaperCarrier: Boolean,
    @SerializedName("diaper_carrier_observations") val diaperCarrierObservations: String?,
    @SerializedName("urinary_catheter") val urinaryCatheter: Boolean,
    @SerializedName("urinary_catheter_observations") val urinaryCatheterObservations: String?,
    @SerializedName("rectal_catheter") val rectalCatheter: Boolean,
    @SerializedName("rectal_catheter_observations") val rectalCatheterObservations: String?,
    @SerializedName("nasogastric_catheter") val nasogastricCatheter: Boolean,
    @SerializedName("nasogastric_catheter_observations") val nasogastricCatheterObservations: String?,
    @SerializedName("diagnosis_date") val diagnosisDate: String // Puede cambiarse a LocalDate si se maneja correctamente
)

data class DiagnosisResponse(
    @SerializedName("id_diagnosis") val idDiagnosis: Int,

    @SerializedName("degree_of_dependence") val degreeOfDependence: String,

    @SerializedName("oxygen_carrier") val oxygenCarrier: Boolean,

    @SerializedName("oxygen_carrier_observations") val oxygenCarrierObservations: String?,

    @SerializedName("diaper_carrier") val diaperCarrier: Boolean,

    @SerializedName("diaper_carrier_observations") val diaperCarrierObservations: String?,

    @SerializedName("urinary_catheter") val urinaryCatheter: Boolean,

    @SerializedName("urinary_catheter_observations") val urinaryCatheterObservations: String?,

    @SerializedName("rectal_catheter") val rectalCatheter: Boolean,

    @SerializedName("rectal_catheter_observations") val rectalCatheterObservations: String?,

    @SerializedName("nasogastric_catheter") val nasogastricCatheter: Boolean,

    @SerializedName("nasogastric_catheter_observations") val nasogastricCatheterObservations: String?,

    @SerializedName("diagnosis_date") val diagnosisDate: String, // Puede cambiarse a LocalDate si da conflictos
)
