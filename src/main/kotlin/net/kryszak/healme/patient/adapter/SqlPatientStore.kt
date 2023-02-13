package net.kryszak.healme.patient.adapter

import arrow.core.Either
import net.kryszak.healme.authentication.TenantId
import net.kryszak.healme.common.exception.DataNotFoundException
import net.kryszak.healme.patient.CreatePatientParams
import net.kryszak.healme.patient.Patient
import net.kryszak.healme.patient.PatientStore
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

class SqlPatientStore(private val patientRepository: PatientRepository) : PatientStore {

    override fun savePatient(params: CreatePatientParams): Either<Throwable, Patient> =
        PatientEntity.fromParams(params)
            .let { Either.catch { patientRepository.save(it) } }
            .map(PatientEntity::toDomain)

    override fun findPatients(tenantId: TenantId, pageable: Pageable): Either<Throwable, Page<Patient>> =
        Either.catch { patientRepository.findAllByOwner(tenantId.value, pageable) }
            .map { it.map(PatientEntity::toDomain) }

    override fun findPatient(tenantId: TenantId, patientId: Long): Either<Throwable, Patient> =
        Either.catch {
            patientRepository.findByIdAndOwner(patientId, tenantId.value)
                ?: throw DataNotFoundException("Patient with id={$patientId} not found under owner={$tenantId}")
        }.map(PatientEntity::toDomain)

    override fun updatePatient(patient: Patient): Either<Throwable, Patient> =
        Either.catch {
            PatientEntity.fromDomain(patient)
                .let(patientRepository::save)
        }.map(PatientEntity::toDomain)

}