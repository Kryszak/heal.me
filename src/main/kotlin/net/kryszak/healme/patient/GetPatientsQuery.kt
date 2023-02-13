package net.kryszak.healme.patient

import arrow.core.Either
import arrow.core.flatMap
import net.kryszak.healme.common.TenantStore
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

class GetPatientsQuery(
    private val patientStore: PatientStore,
    private val tenantStore: TenantStore
) {

    fun execute(input: Input): Either<Throwable, Page<Patient>> =
        tenantStore.getCurrentTenant()
            .flatMap { patientStore.findPatients(it, input.pageable) }

    data class Input(
        val pageable: Pageable
    )
}