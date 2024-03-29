package io.github.kryszak.healme.patient

import arrow.core.Either
import io.github.kryszak.healme.authentication.TenantId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PatientStore {

    fun savePatient(params: CreatePatientParams): Either<Throwable, Patient>

    fun findPatients(
        tenantId: TenantId,
        pageable: Pageable
    ): Either<Throwable, Page<Patient>>

    fun findPatient(
        tenantId: TenantId,
        patientId: Long
    ): Either<Throwable, Patient>

    fun updatePatient(patient: Patient): Either<Throwable, Patient>

    fun deletePatient(patient: Patient): Either<Throwable, Unit>
}

data class CreatePatientParams(
    val name: String,
    val surname: String,
    val address: String,
    val owner: TenantId
)