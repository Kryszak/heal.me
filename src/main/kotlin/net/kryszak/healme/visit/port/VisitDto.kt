package net.kryszak.healme.visit.port

import java.time.LocalDateTime
import net.kryszak.healme.doctor.port.DoctorDto
import net.kryszak.healme.patient.port.PatientDto
import net.kryszak.healme.visit.Visit

data class VisitDto(
    val id: Long,
    val dateTime: LocalDateTime,
    val place: String,
    val doctor: DoctorDto,
    val patient: PatientDto,
) {
    companion object {
        fun from(visit: Visit): VisitDto =
            VisitDto(
                visit.id,
                visit.dateTime,
                visit.place,
                DoctorDto.from(visit.doctor),
                PatientDto.from(visit.patient)
            )
    }
}
