package net.kryszak.healme.patient

import arrow.core.Either
import arrow.core.flatMap
import net.kryszak.healme.common.TenantStore

class DeletePatientCommand(
    private val patientStore: PatientStore,
    private val tenantStore: TenantStore
) {

    fun execute(input: Input): Either<Throwable, Unit> {
        return tenantStore.getCurrentTenant()
            .flatMap { patientStore.findPatient(it, input.patientId) }
            .flatMap { patientStore.deletePatient(it) }
    }

    data class Input(val patientId: Long)
}