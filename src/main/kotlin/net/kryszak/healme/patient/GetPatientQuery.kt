package net.kryszak.healme.patient

import arrow.core.Either
import arrow.core.flatMap
import net.kryszak.healme.common.TenantStore

class GetPatientQuery(
    private val patientStore: PatientStore,
    private val tenantStore: TenantStore
) {

    fun execute(input: Input): Either<Throwable, Patient> {
        return tenantStore.getCurrentTenant()
            .flatMap { patientStore.findPatient(it, input.patientId) }
    }

    data class Input(val patientId: Long)
}