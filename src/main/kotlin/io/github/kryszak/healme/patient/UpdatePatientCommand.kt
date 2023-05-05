package io.github.kryszak.healme.patient

import arrow.core.Either
import arrow.core.flatMap
import io.github.kryszak.healme.common.TenantStore
import io.github.kryszak.healme.common.exception.DataMismatchException

class UpdatePatientCommand(
    private val patientStore: PatientStore,
    private val tenantStore: TenantStore
) {
    fun execute(input: Input): Either<Throwable, Patient> =
        Either.catch { validateInput(input) }
            .flatMap { tenantStore.getCurrentTenant() }
            .flatMap { patientStore.findPatient(it, input.id) }
            .map { updatePatient(it, input) }
            .flatMap { patientStore.updatePatient(it) }

    private fun validateInput(input: Input) {
        if (input.id != input.resourceId) {
            throw DataMismatchException()
        }
    }

    private fun updatePatient(patient: Patient, input: Input) =
        patient.copy(
            name = input.name,
            surname = input.surname,
            address = input.address
        )

    data class Input(
        val resourceId: Long,
        val id: Long,
        val name: String,
        val surname: String,
        val address: String,
    )
}