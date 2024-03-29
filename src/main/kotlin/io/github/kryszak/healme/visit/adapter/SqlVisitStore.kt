package io.github.kryszak.healme.visit.adapter

import arrow.core.Either
import io.github.kryszak.healme.authentication.TenantId
import io.github.kryszak.healme.common.exception.DataNotFoundException
import io.github.kryszak.healme.doctor.adapter.DoctorEntity
import io.github.kryszak.healme.patient.adapter.PatientEntity
import io.github.kryszak.healme.visit.CreateVisitParams
import io.github.kryszak.healme.visit.ExistsVisitInTimeWindowParams
import io.github.kryszak.healme.visit.Visit
import io.github.kryszak.healme.visit.VisitStore
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

open class SqlVisitStore(private val visitRepository: VisitRepository) : VisitStore {
    override fun saveVisit(params: CreateVisitParams): Either<Throwable, Visit> =
        VisitEntity.fromParams(params)
            .let { Either.catch { visitRepository.save(it) } }
            .map(VisitEntity::toDomain)

    override fun existsVisitInGivenTimeWindow(params: ExistsVisitInTimeWindowParams): Either<Throwable, Boolean> =
        Either.catch {
            visitRepository.existsInGivenTimeFrame(
                params.startTime,
                params.endTime,
                PatientEntity.fromDomain(params.patient),
                DoctorEntity.fromDomain(params.doctor)
            )
        }

    @Transactional
    override fun findVisit(visitId: Long, tenantId: TenantId): Either<Throwable, Visit> =
        Either.catch {
            visitRepository.findByIdAndOwner(visitId, tenantId.value)
                ?: throw DataNotFoundException("Visit with id={$visitId} not found under owner={$tenantId}")
        }.map(VisitEntity::toDomain)

    @Transactional
    override fun findVisits(tenantId: TenantId, pageable: Pageable): Either<Throwable, Page<Visit>> =
        Either.catch { visitRepository.findAllByOwner(tenantId.value, pageable) }
            .map { it.map(VisitEntity::toDomain) }

    override fun findPatientVisits(
        patientId: Long,
        tenantId: TenantId,
        pageable: Pageable
    ): Either<Throwable, Page<Visit>> =
        Either.catch { visitRepository.findAllByPatientIdAndOwner(patientId, tenantId.value, pageable) }
            .map { it.map(VisitEntity::toDomain) }

    @Transactional
    override fun deleteByPatient(patientId: Long, tenantId: TenantId): Either<Throwable, Unit> =
        Either.catch { visitRepository.deleteByPatientIdAndOwner(patientId, tenantId.value) }

    @Transactional
    override fun deleteByDoctor(doctorId: Long, tenantId: TenantId): Either<Throwable, Unit> =
        Either.catch { visitRepository.deleteByDoctorIdAndOwner(doctorId, tenantId.value) }

    override fun deleteVisit(visit: Visit): Either<Throwable, Unit> =
        Either.catch { visitRepository.delete(VisitEntity.fromDomain(visit)) }

    override fun updateVisit(visit: Visit): Either<Throwable, Visit> = Either.catch {
        VisitEntity.fromDomain(visit)
            .let(visitRepository::save)
    }.map(VisitEntity::toDomain)
}