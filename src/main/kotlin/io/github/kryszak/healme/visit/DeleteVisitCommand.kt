package io.github.kryszak.healme.visit

import arrow.core.Either
import arrow.core.flatMap
import io.github.kryszak.healme.common.TenantStore

class DeleteVisitCommand(
    private val visitStore: VisitStore,
    private val tenantStore: TenantStore,
) {

    fun execute(input: Input): Either<Throwable, Unit> =
        tenantStore.getCurrentTenant()
            .flatMap { visitStore.findVisit(input.visitId, it) }
            .flatMap(visitStore::deleteVisit)

    data class Input(val visitId: Long)
}