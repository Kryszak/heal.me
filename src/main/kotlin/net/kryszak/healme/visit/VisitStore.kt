package net.kryszak.healme.visit

import arrow.core.Either
import java.time.LocalDateTime
import net.kryszak.healme.authentication.TenantId
import net.kryszak.healme.doctor.Doctor
import net.kryszak.healme.patient.Patient

interface VisitStore {

    fun saveVisit(params: CreateVisitParams): Either<Throwable, Visit>

    fun existsVisitInGivenTimeWindow(params: ExistsVisitInTimeWindowParams): Either<Throwable, Boolean>
}

data class CreateVisitParams(
    val dateTime: LocalDateTime,
    val place: String,
    val doctor: Doctor,
    val patient: Patient,
    val owner: TenantId,
)

data class ExistsVisitInTimeWindowParams(
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val patient: Patient,
    val doctor: Doctor
)