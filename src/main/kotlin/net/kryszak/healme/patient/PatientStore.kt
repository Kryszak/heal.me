package net.kryszak.healme.patient

import arrow.core.Either
import net.kryszak.healme.authentication.TenantId

interface PatientStore {

    fun savePatient(params: CreatePatientParams): Either<Throwable, Patient>


}

data class CreatePatientParams(
    val name: String,
    val surname: String,
    val address: String,
    val owner: TenantId
)