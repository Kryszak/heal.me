package net.kryszak.healme.visit

import arrow.core.Either
import arrow.core.flatMap
import net.kryszak.healme.common.TenantStore
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

class GetVisitsQuery(
    private val visitStore: VisitStore,
    private val tenantStore: TenantStore,
) {

    fun execute(input: Input): Either<Throwable, Page<Visit>> =
        tenantStore.getCurrentTenant()
            .flatMap { visitStore.findVisits(it, input.pageable) }

    data class Input(val pageable: Pageable)
}