package io.github.kryszak.healme.visit

import arrow.core.Either
import io.github.kryszak.healme.authentication.TenantId
import io.github.kryszak.healme.doctor.Doctor
import io.github.kryszak.healme.patient.Patient
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

interface VisitStore {

    fun saveVisit(params: CreateVisitParams): Either<Throwable, Visit>

    fun existsVisitInGivenTimeWindow(params: ExistsVisitInTimeWindowParams): Either<Throwable, Boolean>

    fun findVisit(visitId: Long, tenantId: TenantId): Either<Throwable, Visit>

    fun findVisits(tenantId: TenantId, pageable: Pageable): Either<Throwable, Page<Visit>>

    fun findPatientVisits(patientId: Long, tenantId: TenantId, pageable: Pageable): Either<Throwable, Page<Visit>>

    fun deleteByPatient(patientId: Long, tenantId: TenantId): Either<Throwable, Unit>

    fun deleteByDoctor(doctorId: Long, tenantId: TenantId): Either<Throwable, Unit>

    fun deleteVisit(visit: Visit): Either<Throwable, Unit>

    fun updateVisit(visit: Visit): Either<Throwable, Visit>
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