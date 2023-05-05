package io.github.kryszak.healme.patient

import arrow.core.Either
import arrow.core.flatMap
import io.github.kryszak.healme.common.TenantStore

class CreatePatientCommand(private val patientStore: PatientStore, private val tenantStore: TenantStore) {

    fun execute(input: Input): Either<Throwable, Unit> =
        tenantStore.getCurrentTenant()
            .flatMap { patientStore.savePatient(CreatePatientParams(input.name, input.surname, input.address, it)) }
            .map { }


    data class Input(
        val name: String,
        val surname: String,
        val address: String,
    )
}