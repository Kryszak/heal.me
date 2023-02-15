package net.kryszak.healme.visit

import arrow.core.Either
import arrow.core.flatMap
import net.kryszak.healme.common.TenantStore
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

class GetPatientVisitsQuery(
    private val visitStore: VisitStore,
    private val tenantStore: TenantStore,
) {

    fun execute(input: Input): Either<Throwable, Page<Visit>> =
        tenantStore.getCurrentTenant()
            .flatMap { visitStore.findPatientVisits(input.patientId, it, input.pageable) }

    data class Input(val patientId: Long, val pageable: Pageable)
}