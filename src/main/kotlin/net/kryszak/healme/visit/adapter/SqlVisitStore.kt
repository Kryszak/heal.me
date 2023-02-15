package net.kryszak.healme.visit.adapter

import arrow.core.Either
import javax.transaction.Transactional
import net.kryszak.healme.authentication.TenantId
import net.kryszak.healme.common.exception.DataNotFoundException
import net.kryszak.healme.doctor.adapter.DoctorEntity
import net.kryszak.healme.patient.adapter.PatientEntity
import net.kryszak.healme.visit.CreateVisitParams
import net.kryszak.healme.visit.ExistsVisitInTimeWindowParams
import net.kryszak.healme.visit.Visit
import net.kryszak.healme.visit.VisitStore
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
}