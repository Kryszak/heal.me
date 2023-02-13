package net.kryszak.healme.patient

import arrow.core.Either
import net.kryszak.healme.authentication.TenantId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PatientStore {

    fun savePatient(params: CreatePatientParams): Either<Throwable, Patient>

    fun findPatients(tenantId: TenantId, pageable: Pageable): Either<Throwable, Page<Patient>>

}

data class CreatePatientParams(
    val name: String,
    val surname: String,
    val address: String,
    val owner: TenantId
)