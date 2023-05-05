package io.github.kryszak.healme.patient

import arrow.core.Either
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PatientStore {

    fun savePatient(params: CreatePatientParams): Either<Throwable, Patient>

    fun findPatients(
        tenantId: io.github.kryszak.healme.authentication.TenantId,
        pageable: Pageable
    ): Either<Throwable, Page<Patient>>

    fun findPatient(
        tenantId: io.github.kryszak.healme.authentication.TenantId,
        patientId: Long
    ): Either<Throwable, Patient>

    fun updatePatient(patient: Patient): Either<Throwable, Patient>

    fun deletePatient(patient: Patient): Either<Throwable, Unit>
}

data class CreatePatientParams(
    val name: String,
    val surname: String,
    val address: String,
    val owner: io.github.kryszak.healme.authentication.TenantId
)