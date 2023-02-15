package net.kryszak.healme.visit

import java.time.LocalDateTime
import net.kryszak.healme.authentication.TenantId
import net.kryszak.healme.doctor.Doctor
import net.kryszak.healme.patient.Patient

data class Visit(
    val id: Long,
    val dateTime: LocalDateTime,
    // NOTE: this should be a proper value object, also kept in separate
    // db table. For simplicity reason, it's handled as single string field
    val place: String,
    val doctor: Doctor,
    val patient: Patient,
    val owner: TenantId,
)
