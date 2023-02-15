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

    // NOTE: this domain class should not depend on framework classes(Pageable)
    // This class breaks this rule for following reason:
    // to avoid manual mapping of query parameters coming from port layer
    // so the whole app development process would be faster, as framework
    // provides out-of-the-box functionality for sorting and paging
    data class Input(val patientId: Long, val pageable: Pageable)
}