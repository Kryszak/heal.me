package io.github.kryszak.healme.visit

import arrow.core.Either
import arrow.core.flatMap
import io.github.kryszak.healme.common.TenantStore
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

class GetVisitsQuery(
    private val visitStore: VisitStore,
    private val tenantStore: TenantStore,
) {

    fun execute(input: Input): Either<Throwable, Page<Visit>> =
        tenantStore.getCurrentTenant()
            .flatMap { visitStore.findVisits(it, input.pageable) }

    // NOTE: this domain class should not depend on framework classes(Pageable)
    // This class breaks this rule for following reason:
    // to avoid manual mapping of query parameters coming from port layer
    // so the whole app development process would be faster, as framework
    // provides out-of-the-box functionality for sorting and paging
    data class Input(val pageable: Pageable)
}