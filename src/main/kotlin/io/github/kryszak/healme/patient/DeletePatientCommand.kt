package io.github.kryszak.healme.patient

import arrow.core.Either
import arrow.core.flatMap
import io.github.kryszak.healme.common.TenantStore

class DeletePatientCommand(
    private val patientStore: PatientStore,
    private val visitStore: VisitStore,
    private val tenantStore: TenantStore
) {

    fun execute(input: Input): Either<Throwable, Unit> {
        return tenantStore.getCurrentTenant()
            .flatMap { patientStore.findPatient(it, input.patientId) }
            .flatMap(::deleteVisitsAssignedToPatient)
            .flatMap { patientStore.deletePatient(it) }
    }

    private fun deleteVisitsAssignedToPatient(patient: Patient): Either<Throwable, Patient> {
        return visitStore.deleteVisits(patient.id)
            .map { patient }
    }

    data class Input(val patientId: Long)
}