package io.github.kryszak.healme.visit

import io.github.kryszak.healme.doctor.Doctor
import io.github.kryszak.healme.patient.Patient
import java.time.LocalDateTime

data class Visit(
    val id: Long,
    val dateTime: LocalDateTime,
    // NOTE: this should be a proper value object, also kept in separate
    // db table. For simplicity reason, it's handled as single string field
    val place: String,
    val doctor: Doctor,
    val patient: Patient,
    val owner: io.github.kryszak.healme.authentication.TenantId,
)
