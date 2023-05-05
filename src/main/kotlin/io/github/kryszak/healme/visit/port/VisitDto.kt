package io.github.kryszak.healme.visit.port

import io.github.kryszak.healme.doctor.port.DoctorDto
import io.github.kryszak.healme.patient.port.PatientDto
import io.github.kryszak.healme.visit.Visit
import java.time.LocalDate
import java.time.LocalTime

data class VisitDto(
    val id: Long,
    val date: String,
    val time: String,
    val place: String,
    val doctor: DoctorDto,
    val patient: PatientDto,
) {
    companion object {
        fun from(visit: Visit): VisitDto =
            VisitDto(
                visit.id,
                LocalDate.from(visit.dateTime).toString(),
                LocalTime.from(visit.dateTime).toString(),
                visit.place,
                DoctorDto.from(visit.doctor),
                PatientDto.from(visit.patient)
            )
    }
}
