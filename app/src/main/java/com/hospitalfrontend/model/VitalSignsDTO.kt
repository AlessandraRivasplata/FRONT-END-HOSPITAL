package com.hospitalfrontend.model

import java.math.BigDecimal
import java.util.Date

data class VitalSignsDTO(
    val recordedAt: Date?,
    val systolicBp: Int?,
    val diastolicBp: Int?,
    val bodyTemperature: BigDecimal?,
    val pulse: Int?,
    val respiratoryRate: Int?
)
